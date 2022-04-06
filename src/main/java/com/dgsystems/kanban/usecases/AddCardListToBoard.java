package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.CardList;

import java.util.Collections;
import java.util.Optional;

public class AddCardListToBoard {
    private final BoardRepository boardRepository;

    public AddCardListToBoard(BoardRepository repository) {
        boardRepository = repository;
    }

    public void execute(String boardName, String cardListTitle) {
        Optional<Board> optional = boardRepository.getBoard(boardName);
        optional.map(b -> {
            BoardManager boardManager = new BoardManager();
            Board updated = boardManager.addCardList(b, new CardList(cardListTitle, Collections.emptyList()));
            boardRepository.save(updated);
            return updated;
        }).orElseThrow();
    }
}
