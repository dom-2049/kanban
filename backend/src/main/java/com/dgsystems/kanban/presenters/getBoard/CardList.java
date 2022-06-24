package com.dgsystems.kanban.presenters.getBoard;

import java.util.List;
import java.util.UUID;

public record CardList(UUID id, String title, List<Card> cards) {
}
