package com.dgsystems.kanban.entities;

import java.util.Optional;
import java.util.UUID;

public record Card(UUID id, String title, String description, Optional<Member> teamMember) {
    public Card addMember(Member Member) {
        return new Card(id, title, description, Optional.of(Member));
    }
}
