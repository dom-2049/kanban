package com.dgsystems.kanban.infrastructure.persistence.in_memory;

import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.usecases.BoardMemberRepository;
import com.jcabi.aspects.Loggable;

import java.util.*;

public class InMemoryBoardMemberRepository implements BoardMemberRepository {
    @Override
    @Loggable
    public Optional<BoardMember> getBy(String username) {
        return boardMembers.stream().filter(b -> b.username().equals(username)).findFirst();
    }

    private final List<BoardMember> boardMembers = new ArrayList<>();

    @Override
    @Loggable
    public void save(BoardMember boardMember) {
        List<BoardMember> filtered = boardMembers.stream()
                .filter(b -> Objects.equals(b.username(), boardMember.username()))
                .toList();

        if(filtered.isEmpty()) {
            boardMembers.add(boardMember);
        } else {
            boardMembers.remove(filtered.get(0));
            boardMembers.add(boardMember);
        }
    }

    @Override
    @Loggable
    public List<BoardMember> getAll() {
        return Collections.unmodifiableList(boardMembers);
    }
}
