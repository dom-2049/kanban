package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BoardSuiteTest {

    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";
    public static final String TO_DO = "to do";
    public static final String IN_PROGRESS = "in progress";

    public BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        Context.initialize(boardRepository);
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
        addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, new Card(UUID.randomUUID(), "card title", "card description", Optional.empty()));

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();

        assertThat(board.cardLists().get(0).cards()).filteredOn(c -> c.title().equals("card title")).isNotEmpty();
    }

    @Test
    @DisplayName("Should move card between lists")
    void shouldMoveCardBetweenLists() throws BoardAlreadyChangedException {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        Card card = new Card(UUID.randomUUID(),"do the dishes", "must do the dishes!", Optional.empty());

        createBoard.execute(BOARD_NAME);
        MoveCardBetweenLists moveCardFromOneListToAnother = new MoveCardBetweenLists(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, TO_DO);
        addCardListToBoard.execute(BOARD_NAME, IN_PROGRESS);
        addCardToCardList.execute(BOARD_NAME, TO_DO, card);

        Board beforeExecutionBoard = getBoard.execute(BOARD_NAME).orElseThrow();
        moveCardFromOneListToAnother.execute(BOARD_NAME, TO_DO, IN_PROGRESS, card, beforeExecutionBoard.hashCode());

        Board board = getBoard.execute(BOARD_NAME).orElseThrow();
        CardList first = board.cardLists().get(0);
        CardList second = board.cardLists().get(1);

        assertThat(first.cards()).filteredOn(c -> c.title().equals(card.title())).isEmpty();
        assertThat(second.cards()).filteredOn(c -> c.title().equals(card.title())).isNotEmpty();
    }

    @Test
    @DisplayName("Should get board")
    void shouldGetBoard() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);

        createBoard.execute(BOARD_NAME);
        Board response = getBoard.execute(BOARD_NAME).orElseThrow();

        assertThat(response.title()).isEqualTo(BOARD_NAME);
        //TODO: validate card lists are immutable
    }

    @Test
    @DisplayName("Should get all boards")
    void shouldGetAllBoards() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);

        createBoard.execute("work");
        createBoard.execute("hobby");
        List<Board> response = getAllBoards.execute();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).title()).isEqualTo("work");
        assertThat(response.get(1).title()).isEqualTo("hobby");
    }
}