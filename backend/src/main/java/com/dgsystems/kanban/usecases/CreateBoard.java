package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.jcabi.aspects.Loggable;

import java.util.Collections;
import java.util.Optional;

public record CreateBoard(BoardRepository boardRepository, MemberRepository MemberRepository) {
    @Loggable(prepend = true)
    public Board execute(String boardName, Optional<Member> Member) throws OwnerDoesNotExistException {
        Member owner = Member.orElseThrow(OwnerDoesNotExistException::new);

        if(MemberRepository.getBy(owner.username()).isEmpty()) {
            throw new OwnerDoesNotExistException();
        }

        BoardSession boardSession = new BoardSession();
        Board board = boardSession.startBoard(boardName, Collections.emptyList(), Collections.singletonList(owner), owner);
        boardRepository.save(board);
        return board;
    }
}
