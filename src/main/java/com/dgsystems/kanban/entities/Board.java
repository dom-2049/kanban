package com.dgsystems.kanban.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Board(String title, List<CardList> cardLists) {
    public Board addCardList(CardList cardList) {
        List<CardList> newCardLists = new ArrayList<>(cardLists);
        newCardLists.add(cardList);
        return new Board(title(), newCardLists);
    }

    public Board addCard(String cardListTitle, Card card) {
        return new Board(
                title,
                cardLists.stream().map(cl -> cl.title().equals(cardListTitle) ? cl.add(card) : cl)
                        .collect(Collectors.toList())
        );
    }

    public Board move(Card card, String from, String to) {
        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if(cl.title().equals(from)) {
                return cl.remove(card);
            } else if(cl.title().equals(to)) {
                return cl.add(card);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return new Board(title, cardLists);
    }
}
