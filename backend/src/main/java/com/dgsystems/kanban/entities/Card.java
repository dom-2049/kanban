package com.dgsystems.kanban.entities;

import java.util.Optional;
import java.util.UUID;

public record Card(UUID id, String title, String description, Optional<BoardMember> teamMember) {
    public Card addMember(BoardMember boardMember) {
        return new Card(id, title, description, Optional.of(boardMember));
    }
}
