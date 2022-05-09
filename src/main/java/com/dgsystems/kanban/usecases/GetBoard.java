package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.jcabi.aspects.Loggable;

import java.util.Optional;

public record GetBoard(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Optional<Board> execute(String boardName) {
        return boardRepository.getBoard(boardName);
    }
}
