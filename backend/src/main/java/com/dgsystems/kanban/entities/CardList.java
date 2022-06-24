package com.dgsystems.kanban.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CardList(UUID id, String title, List<Card> cards) {
    public CardList add(Card card) {
        List<Card> updated = new ArrayList<>(cards);
        updated.add(card);
        return new CardList(id, title, updated);
    }

    public CardList remove(Card card) {
        List<Card> updated = new ArrayList<>(cards);
        updated.remove(card);
        return new CardList(id, title, updated);
    }
}
