package com.dgsystems.kanban.entities;

import java.util.Optional;
import java.util.UUID;

public record Card(UUID id, String title, String description, Optional<TeamMember> teamMember) {
    public Card addMember(TeamMember teamMember) {
        return new Card(id, title, description, Optional.of(teamMember));
    }
}
