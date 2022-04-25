package com.dgsystems.kanban.entities;

import java.util.UUID;

public record Card(UUID id, String title, String description) {
}
