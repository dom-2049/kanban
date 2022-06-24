package com.dgsystems.kanban.presenters.getBoard;

import java.util.List;
import java.util.stream.Collectors;

public class GetBoardPresenter {
    public Board present(com.dgsystems.kanban.entities.Board boardResponse) {
        List<CardList> cardLists = boardResponse.cardLists().stream()
                .map(cardList ->
                        new CardList(cardList.id(),
                                cardList.title(),
                                cardList.cards().stream()
                                        .map(card -> new Card(card.id(), card.title())
                                        ).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return new Board(
                boardResponse.title(),
                cardLists,
                cardLists.stream()
                        .map(CardList::id).collect(Collectors.toList()));
    }
}
