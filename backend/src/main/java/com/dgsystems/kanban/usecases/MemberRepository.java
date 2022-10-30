package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> getBy(String username);

    void save(Member Member);

    List<Member> getAll();
}
