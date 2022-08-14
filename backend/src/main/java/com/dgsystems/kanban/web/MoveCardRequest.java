package com.dgsystems.kanban.web;

import java.util.UUID;

public record MoveCardRequest(String from, String to, UUID card, int boardHashCode) {
}
