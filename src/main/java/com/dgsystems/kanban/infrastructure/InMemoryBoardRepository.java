package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.usecases.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBoardRepository implements BoardRepository {
    private final List<Board> boards = new ArrayList<>();

    @Override
    public Optional<Board> getBoard(String boardName) {
        return boards.stream().findFirst();
    }

    @Override
    public void save(Board board) {
        boards.add(board);
    }
}
