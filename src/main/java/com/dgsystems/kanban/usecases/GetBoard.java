package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;

import java.util.Optional;

public record GetBoard(BoardRepository boardRepository) {

    public Optional<Board> execute(String boardName) {
        return boardRepository.getBoard(boardName);
    }
}
