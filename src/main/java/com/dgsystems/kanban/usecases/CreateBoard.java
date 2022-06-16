package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.jcabi.aspects.Loggable;

import java.util.Collections;
import java.util.Optional;

public record CreateBoard(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository) {
    @Loggable(prepend = true)
    public Board execute(String boardName, Optional<BoardMember> boardMember) throws OwnerDoesNotExistException {
        BoardMember owner = boardMember.orElseThrow(OwnerDoesNotExistException::new);

        if(boardMemberRepository.getBy(owner.username()).isEmpty()) {
            throw new OwnerDoesNotExistException();
        }

        BoardSession boardSession = new BoardSession();
        Board board = boardSession.startBoard(boardName, Collections.emptyList(), Collections.singletonList(owner), owner);
        boardRepository.save(board);
        return board;
    }
}
