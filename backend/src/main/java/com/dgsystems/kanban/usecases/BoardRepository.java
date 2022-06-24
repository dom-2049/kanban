package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<Board> getBoard(String boardName);

    void save(Board board);

    List<Board> getAllForOwner(BoardMember owner);
}
