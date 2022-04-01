package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;

public record MoveCardBetweenLists(BoardRepository boardRepository) {
    public void execute(String boardName, String from, String to, Card card) {
        Board board = boardRepository.getBoard(boardName).map(b -> b.move(card, from, to)).orElseThrow();
        boardRepository.save(board);
    }
}
