package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.usecases.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InMemoryBoardRepository implements BoardRepository {
    private final List<Board> boards = new ArrayList<>();

    @Override
    public Optional<Board> getBoard(String boardName) {
        return boards.stream().findFirst();
    }

    @Override
    public void save(Board board) {
        List<Board> filtered = boards.stream()
                .filter(b -> Objects.equals(b.title(), board.title()))
                .toList();

        if(filtered.isEmpty()) {
            boards.add(board);
        } else {
            boards.remove(filtered.get(0));
            boards.add(board);
        }
    }
}
