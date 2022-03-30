package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;

import java.util.Collections;

public record CreateBoard(BoardRepository boardRepository) {
    public Board execute(String boardName) {
        Board board = new Board(boardName, Collections.emptyList());
        boardRepository.save(board);
        return board;
    }
}
