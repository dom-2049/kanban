package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class BoardSuiteTest {

    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";

    @Test
    @DisplayName("Should create empty board")
    void shouldCreateEmptyBoard() {
        BoardRepository repository = new InMemoryBoardRepository();
        CreateBoard createBoard = new CreateBoard(repository);
        Board expected = new Board(BOARD_NAME, Collections.emptyList());

        createBoard.execute(BOARD_NAME);
        Optional<Board> board = repository.getBoard(BOARD_NAME);

        assertThat(board).isEqualTo(Optional.of(expected));
    }

    @Test
    @DisplayName("Should add card list to board")
    void shouldAddCardListToBoard() {
        BoardRepository repository = new InMemoryBoardRepository();
        CreateBoard createBoard = new CreateBoard(repository);
        createBoard.execute(BOARD_NAME);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(repository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE);

        Board board = repository.getBoard(BOARD_NAME).get();
        assertThat(board.cardLists()).
                filteredOn(c -> c.title().equals(CARD_LIST_TITLE)).isNotEmpty();
    }

    @Test
    @DisplayName("Should add card to card list")
    void shouldAddCardToCardList() {
        BoardRepository repository = new InMemoryBoardRepository();
        CreateBoard createBoard = new CreateBoard(repository);
        createBoard.execute(BOARD_NAME);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(repository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE);

        AddCardToCardList addCardToCardList = new AddCardToCardList(repository);
        addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, new Card("card title", "card description"));

        Board board = repository.getBoard(BOARD_NAME).get();

        assertThat(board.cardLists().get(0).cards()).
                filteredOn(c -> c.title().equals("card title")).isNotEmpty();
    }
}