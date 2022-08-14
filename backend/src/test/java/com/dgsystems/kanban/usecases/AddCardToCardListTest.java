package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddCardToCardListTest {
    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";

    public BoardRepository boardRepository;
    private InMemoryBoardMemberRepository boardMemberRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        boardMemberRepository = new InMemoryBoardMemberRepository();
        boardMemberRepository.save(new BoardMember("owner"));
        Context.initialize(boardRepository);
    }

    @Test
    @DisplayName("Should add card to card list")
    void shouldAddCardToCardList() throws OwnerDoesNotExistException, MemberNotInTeamException {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        BoardMember owner = new BoardMember("owner");
        Optional<BoardMember> memberOptional = Optional.of(owner);
        createBoard.execute(BOARD_NAME, memberOptional);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE, memberOptional);

        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, new Card(UUID.randomUUID(), "card title", "card description", Optional.empty()), memberOptional);

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();

        assertThat(board.cardLists().get(0).cards()).filteredOn(c -> c.title().equals("card title")).isNotEmpty();
    }

    @Test
    @DisplayName("Should not add card to list when member not in members")
    void shouldNotAddCardToListWhenMemberNotInMembers() throws MemberNotInTeamException, OwnerDoesNotExistException {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        BoardMember owner = new BoardMember("owner");
        Optional<BoardMember> memberOptional = Optional.of(owner);
        createBoard.execute(BOARD_NAME, memberOptional);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE, memberOptional);

        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        Optional<BoardMember> invalidUser = Optional.of(new BoardMember("invalid_user"));
        assertThrows(CompletionException.class, () -> addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, new Card(UUID.randomUUID(), "card title", "card description", Optional.empty()), invalidUser));
        //TODO: assert card is not added
    }
}
