package com.dgsystems.kanban.presenters;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.presenters.getBoard.Card;
import com.dgsystems.kanban.presenters.getBoard.CardList;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllBoardsPresenter {
    public List<com.dgsystems.kanban.presenters.getBoard.Board> present(List<Board> boards) {
        return boards.stream().map(b -> {
            List<CardList> cardLists = b.cardLists().stream()
                    .map(cardList ->
                            new CardList(cardList.id(),
                                    cardList.title(),
                                    cardList.cards().stream()
                                            .map(card -> new Card(card.id(), card.title())
                                            ).collect(Collectors.toList())))
                    .collect(Collectors.toList());
            return new com.dgsystems.kanban.presenters.getBoard.Board(
                    b.title(),
                    cardLists,
                    cardLists.stream().map(CardList::id).collect(Collectors.toList()),
                    b.hashCode());
        }).collect(Collectors.toList());
    }
}
