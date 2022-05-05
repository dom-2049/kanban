package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.usecases.BoardMemberRepository;

import java.util.*;

public class InMemoryBoardMemberRepository implements BoardMemberRepository {
    @Override
    public Optional<BoardMember> getBy(String username) {
        return boardMembers.stream().filter(b -> b.username().equals(username)).findFirst();
    }

    private final List<BoardMember> boardMembers = new ArrayList<>();

    @Override
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
    public List<BoardMember> getAll() {
        return Collections.unmodifiableList(boardMembers);
    }
}
