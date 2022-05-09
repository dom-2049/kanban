package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.jcabi.aspects.Loggable;

import java.util.Collections;

public record CreateBoard(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Board execute(String boardName) {
        BoardManager boardManager = new BoardManager();
        Board board = boardManager.createBoard(boardName, Collections.emptyList());
        boardRepository.save(board);
        return board;
    }
}
