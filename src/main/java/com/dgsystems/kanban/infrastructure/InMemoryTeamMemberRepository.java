package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.TeamMember;
import com.dgsystems.kanban.usecases.TeamMemberRepository;

import java.util.*;

public class InMemoryTeamMemberRepository implements TeamMemberRepository {
    @Override
    public Optional<TeamMember> getBy(String username) {
        return teamMembers.stream().filter(b -> b.username().equals(username)).findFirst();
    }

    private final List<TeamMember> teamMembers = new ArrayList<>();

    @Override
    public void save(TeamMember teamMember) {
        List<TeamMember> filtered = teamMembers.stream()
                .filter(b -> Objects.equals(b.username(), teamMember.username()))
                .toList();

        if(filtered.isEmpty()) {
            teamMembers.add(teamMember);
        } else {
            teamMembers.remove(filtered.get(0));
            teamMembers.add(teamMember);
        }
    }

    @Override
    public List<TeamMember> getAll() {
        return Collections.unmodifiableList(teamMembers);
    }
}
