package com.dgsystems.kanban.entities;

import java.util.ArrayList;
import java.util.List;

public record CardList(String title, List<Card> cards) {
    public CardList add(Card card) {
        List<Card> updated = new ArrayList<>(cards);
        updated.add(card);
        return new CardList(title, updated);
    }

    public CardList remove(Card card) {
        List<Card> updated = new ArrayList<>(cards);
        updated.remove(card);
        return new CardList(title, updated);
    }
}
