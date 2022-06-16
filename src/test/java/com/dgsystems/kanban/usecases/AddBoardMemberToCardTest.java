package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AddBoardMemberToCardTest {
    public static final String BOARD_NAME = "work";
    public static final String LIST_TITLE = "todo";
    private Card card;
    private BoardRepository boardRepository;
    private InMemoryBoardMemberRepository teamMemberRepository;

    @Test
    @DisplayName("Should add team member to card")
    void shouldAddTeamMemberToCard() throws MemberNotInTeamException {
        AddTeamMemberToCard addTeamMemberToCard = new AddTeamMemberToCard(teamMemberRepository, boardRepository);
        BoardMember boardMember = new BoardMember("username");
        addTeamMemberToCard.execute(BOARD_NAME, LIST_TITLE, card, boardMember);
        Optional<BoardMember> memberOptional = Optional.of(boardMember);
        Board newBoard = new GetBoard(boardRepository).execute(BOARD_NAME, memberOptional).orElseThrow();
        assertThat(firstCard(newBoard.cardLists()).teamMember()).isEqualTo(memberOptional);
    }

    @BeforeEach
    public void setup() throws OwnerDoesNotExistException {
        boardRepository = new InMemoryBoardRepository();
        teamMemberRepository = new InMemoryBoardMemberRepository();

        Context.initialize(boardRepository);

        CreateBoard createBoard = new CreateBoard(boardRepository, teamMemberRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        BoardMember owner = new BoardMember("owner");
        teamMemberRepository.save(owner);
        createBoard.execute(BOARD_NAME, Optional.of(owner));
        addCardListToBoard.execute(BOARD_NAME, LIST_TITLE, Optional.of(owner));
        card = new Card(UUID.randomUUID(), "dishes", "do the dishes today", Optional.empty());
        addCardToCardList.execute(BOARD_NAME, LIST_TITLE, card, Optional.of(owner));
        AddMemberToBoard addMemberToBoard = new AddMemberToBoard(teamMemberRepository, boardRepository);
        addMemberToBoard.execute(BOARD_NAME, new BoardMember("username"));
    }

    private Card firstCard(List<CardList> cardListList) {
        return firstCardList(cardListList).cards().get(0);
    }

    private CardList firstCardList(List<CardList> cardListList) {
        return cardListList.get(0);
    }
}
