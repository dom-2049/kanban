package com.dgsystems.kanban.presenters.getBoard;

import java.util.List;
import java.util.UUID;

public record Board(String title, List<CardList> cardLists, List<UUID> listIds) {
}
