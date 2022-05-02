package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.TeamMember;

import java.util.Optional;

public interface TeamMemberRepository {
    Optional<TeamMember> getBy(String username);
}
