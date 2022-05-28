package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.jcabi.aspects.Loggable;

public record AddMemberToBoard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, BoardMember newMember) {
        boardRepository.getBoard(boardName).map(board -> {
            BoardSession boardSession = new BoardSession();
            Board updated = boardSession.addMemberToBoard(board, newMember);
            boardRepository.save(updated);
            return updated;
        }).orElseThrow();
    }
}
