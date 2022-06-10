package com.dgsystems.kanban.infrastructure.persistence.in_memory;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.usecases.BoardRepository;
import com.jcabi.aspects.Loggable;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class InMemoryBoardRepository implements BoardRepository {
    private final List<Board> boards = new ArrayList<>();

    @Override
    @Loggable
    public Optional<Board> getBoard(String boardName) {
        return boards.stream().filter(b -> b.title().equals(boardName)).findFirst();
    }

    @Override
    @Loggable
    public void save(Board board) {
        List<Board> filtered = boards.stream()
                .filter(b -> Objects.equals(b.title(), board.title()))
                .toList();

        if (filtered.isEmpty()) {
            boards.add(board);
        } else {
            boards.remove(filtered.get(0));
            boards.add(board);
        }
    }

    @Override
    @Loggable
    public List<Board> getAllForOwner(BoardMember owner) {
        return boards.stream()
                .filter(b -> b.owner().equals(owner))
                .collect(unmodifiableListCollector());
    }

    private Collector<Board, Object, List<Board>> unmodifiableListCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                Collections::unmodifiableList
        );
    }
}
