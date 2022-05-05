package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.BoardMember;

import java.util.List;
import java.util.Optional;

public interface BoardMemberRepository {
    Optional<BoardMember> getBy(String username);

    void save(BoardMember boardMember);

    List<BoardMember> getAll();
}
