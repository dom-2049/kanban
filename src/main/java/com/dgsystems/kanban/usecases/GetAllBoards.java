package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;

import java.util.List;

public class GetAllBoards {
    private final BoardRepository boardRepository;

    public GetAllBoards(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> execute() {
        return boardRepository.getAll();
    }
}
