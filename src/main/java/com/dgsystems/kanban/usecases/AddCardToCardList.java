package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;

import java.util.Optional;

public record AddCardToCardList(BoardRepository boardRepository) {

    public void execute(String boardName, String cardListTitle, Card card) {
        Optional<Board> optional = boardRepository.getBoard(boardName);

        Board board = optional
                .map(b -> b.addCard(cardListTitle, card))
                .orElseThrow();

        boardRepository.save(board);
    }
}
