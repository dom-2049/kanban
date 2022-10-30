package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.jcabi.aspects.Loggable;

import java.util.List;
import java.util.Optional;

public record GetBoard(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Optional<Board> execute(String boardName, Optional<Member> Member) throws OwnerDoesNotExistException, MemberNotInTeamException {
        if(Member.isEmpty()) throw new OwnerDoesNotExistException();

        Optional<Board> board = boardRepository.getBoard(boardName);
        if (board.isPresent()) {
            if(!board.get().members().contains(Member.get())) {
                throw new MemberNotInTeamException(Member.get().username());
            }

            if (!board.get().members().contains(Member.get())) {
                BoardSession session = new BoardSession();
                session.load(List.of(board.get()));
            }
        }
        return board;
    }
}
