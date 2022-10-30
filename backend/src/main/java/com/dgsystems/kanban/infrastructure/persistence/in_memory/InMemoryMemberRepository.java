package com.dgsystems.kanban.infrastructure.persistence.in_memory;

import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.usecases.MemberRepository;
import com.jcabi.aspects.Loggable;

import java.util.*;

public class InMemoryMemberRepository implements MemberRepository {
    @Override
    @Loggable
    public Optional<Member> getBy(String username) {
        return Members.stream().filter(b -> b.username().equals(username)).findFirst();
    }

    private final List<Member> Members = new ArrayList<>();

    @Override
    @Loggable
    public void save(Member Member) {
        List<Member> filtered = Members.stream()
                .filter(b -> Objects.equals(b.username(), Member.username()))
                .toList();

        if(filtered.isEmpty()) {
            Members.add(Member);
        } else {
            Members.remove(filtered.get(0));
            Members.add(Member);
        }
    }

    @Override
    @Loggable
    public List<Member> getAll() {
        return Collections.unmodifiableList(Members);
    }
}
