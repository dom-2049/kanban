package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;

public record AddMemberToBoard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    public void execute(String boardName, BoardMember newMember) {
        boardRepository.getBoard(boardName).map(board -> {
            BoardManager boardManager = new BoardManager();
            Board updated = boardManager.addMemberToBoard(board, newMember);
            boardRepository.save(updated);
            return updated;
        }).orElseThrow();
    }
}
