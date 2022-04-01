package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class BoardSuiteTest {

    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";
    public BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
    }

    @Test
    @DisplayName("Should create empty board")
    void shouldCreateEmptyBoard() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        Board expected = new Board(BOARD_NAME, Collections.emptyList());

        createBoard.execute(BOARD_NAME);
        Optional<Board> board = boardRepository.getBoard(BOARD_NAME);

        assertThat(board).isEqualTo(Optional.of(expected));
    }

    @Test
    @DisplayName("Should add card list to board")
    void shouldAddCardListToBoard() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        createBoard.execute(BOARD_NAME);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE);

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();
        assertThat(board.cardLists()).
                filteredOn(c -> c.title().equals(CARD_LIST_TITLE)).isNotEmpty();
    }

    @Test
    @DisplayName("Should add card to card list")
    void shouldAddCardToCardList() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        createBoard.execute(BOARD_NAME);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE);

        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, new Card("card title", "card description"));

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();

        assertThat(board.cardLists().get(0).cards()).filteredOn(c -> c.title().equals("card title")).isNotEmpty();
    }

    @Test
    @DisplayName("Should move card between lists")
    void shouldMoveCardBetweenLists() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        MoveCardBetweenLists moveCardFromOneListToAnother = new MoveCardBetweenLists(boardRepository);
        Card card = new Card("do the dishes", "must do the dishes!");

        createBoard.execute(BOARD_NAME);
        addCardListToBoard.execute(BOARD_NAME, "to do");
        addCardListToBoard.execute(BOARD_NAME, "in progress");
        addCardToCardList.execute(BOARD_NAME, "to do", card);
        moveCardFromOneListToAnother.execute(BOARD_NAME, "to do", "in progress", card);

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();
        CardList first = board.cardLists().get(0);
        CardList second = board.cardLists().get(1);

        assertThat(first.cards()).filteredOn(c -> c.title().equals(card.title())).isEmpty();
        assertThat(second.cards()).filteredOn(c -> c.title().equals(card.title())).isNotEmpty();
    }
}