package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;

public record CreateBoard(BoardRepository boardRepository) {
    public Board execute(String boardName) {
        Board board = new Board(boardName);
        boardRepository.save(board);
        return board;
    }
}
