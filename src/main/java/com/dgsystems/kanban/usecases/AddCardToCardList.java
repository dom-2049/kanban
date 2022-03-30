package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Card;

public class AddCardToCardList {
    private final BoardRepository boardRepository;

    public AddCardToCardList(BoardRepository boardRepository) {

        this.boardRepository = boardRepository;
    }

    public void execute(String boardName, String cardListTitle, Card card) {

    }
}
