package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class MoveCardBetweenListsTest {

    public static final String BOARD_NAME = "new board";
    public static final String TO_DO = "to do";
    public static final String IN_PROGRESS = "in progress";

    public BoardRepository boardRepository;
    private InMemoryMemberRepository MemberRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        MemberRepository = new InMemoryMemberRepository();
        MemberRepository.save(new Member("owner"));
        Context.initialize(boardRepository);
    }

    @Test
    @DisplayName("Should move card between lists")
    void shouldMoveCardBetweenLists() throws Throwable {
        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        Card card = new Card(UUID.randomUUID(),"do the dishes", "must do the dishes!", Optional.empty());
        Member owner = new Member("owner");

        Optional<Member> memberOptional = Optional.of(owner);
        createBoard.execute(BOARD_NAME, memberOptional);
        MoveCardBetweenLists moveCardFromOneListToAnother = new MoveCardBetweenLists(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, TO_DO, memberOptional);
        addCardListToBoard.execute(BOARD_NAME, IN_PROGRESS, memberOptional);
        addCardToCardList.execute(BOARD_NAME, TO_DO, card, memberOptional);

        Board beforeExecutionBoard = getBoard.execute(BOARD_NAME, MemberRepository.getBy(owner.username())).orElseThrow();
        moveCardFromOneListToAnother.execute(BOARD_NAME, TO_DO, IN_PROGRESS, card, beforeExecutionBoard.hashCode(), owner);

        Board board = getBoard.execute(BOARD_NAME, MemberRepository.getBy(owner.username())).orElseThrow();
        CardList first = board.cardLists().get(0);
        CardList second = board.cardLists().get(1);

        assertThat(first.cards()).filteredOn(c -> c.title().equals(card.title())).isEmpty();
        assertThat(second.cards()).filteredOn(c -> c.title().equals(card.title())).isNotEmpty();
    }

    @Test
    @DisplayName("Should not move card when user is not in members list")
    void shouldNotMoveCardWhenUserIsNotInMembersList() throws MemberNotInTeamException, OwnerDoesNotExistException {
        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        Card card = new Card(UUID.randomUUID(),"do the dishes", "must do the dishes!", Optional.empty());
        Member owner = new Member("owner");

        Optional<Member> memberOptional = Optional.of(owner);
        createBoard.execute(BOARD_NAME, memberOptional);
        MoveCardBetweenLists moveCardFromOneListToAnother = new MoveCardBetweenLists(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, TO_DO, memberOptional);
        addCardListToBoard.execute(BOARD_NAME, IN_PROGRESS, memberOptional);
        addCardToCardList.execute(BOARD_NAME, TO_DO, card, memberOptional);

        Board beforeExecutionBoard = getBoard.execute(BOARD_NAME, MemberRepository.getBy(owner.username())).orElseThrow();
        assertThrows(CompletionException.class, () -> moveCardFromOneListToAnother.execute(BOARD_NAME, TO_DO, IN_PROGRESS, card, beforeExecutionBoard.hashCode(), new Member("invalid_user")));
        //TODO: assert card is not moved
    }
}