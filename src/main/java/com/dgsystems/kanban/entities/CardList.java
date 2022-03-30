package com.dgsystems.kanban.entities;

import java.util.List;

public record CardList(String title, List<Card> cards) {
}
