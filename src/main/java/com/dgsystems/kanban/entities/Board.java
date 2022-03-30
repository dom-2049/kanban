package com.dgsystems.kanban.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Board(String title, List<CardList> cardLists) {
    public Board addCardList(CardList cardList) {
        List<CardList> newCardLists = new ArrayList<>(cardLists);
        newCardLists.add(cardList);
        return new Board(title(), newCardLists);
    }
}
