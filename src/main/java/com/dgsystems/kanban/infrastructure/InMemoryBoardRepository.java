package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.usecases.BoardRepository;

import java.util.*;

public class InMemoryBoardRepository implements BoardRepository {
    private final List<Board> boards = new ArrayList<>();

    @Override
    public Optional<Board> getBoard(String boardName) {
        return boards.stream().filter(b -> b.title().equals(boardName)).findFirst();
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

    @Override
    public List<Board> getAll() {
        return Collections.unmodifiableList(boards);
    }
}
