package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.jcabi.aspects.Loggable;

import java.util.List;

public class GetAllBoards {
    private final BoardRepository boardRepository;

    public GetAllBoards(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Loggable(prepend = true)
    public List<Board> execute() {
        return boardRepository.getAll();
    }
}
