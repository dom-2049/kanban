package com.dgsystems.kanban.usecases;

import akka.actor.ActorSystem;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import com.dgsystems.kanban.infrastructure.InMemoryTeamMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AddTeamMemberToCardTest {
    public static final String BOARD_NAME = "work";
    public static final String LIST_TITLE = "todo";
    private Card card;
    private BoardRepository boardRepository;
    private InMemoryTeamMemberRepository teamMemberRepository;

    @Test
    @DisplayName("Should add team member to card")
    void shouldAddTeamMemberToCard() throws MemberNotInTeamException {
        AddTeamMemberToCard addTeamMemberToCard = new AddTeamMemberToCard(teamMemberRepository, boardRepository);
        TeamMember teamMember = new TeamMember("username");
        addTeamMemberToCard.execute(BOARD_NAME, LIST_TITLE, card, teamMember);
        Board newBoard = new GetBoard(boardRepository).execute(BOARD_NAME).orElseThrow();
        assertThat(firstCard(newBoard.cardLists()).teamMember()).isEqualTo(Optional.of(teamMember));
    }

    @BeforeEach
    public void setup() {
        Context.actorSystem = ActorSystem.create();

        boardRepository = new InMemoryBoardRepository();

        CreateBoard createBoard = new CreateBoard(boardRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);

        createBoard.execute(BOARD_NAME);
        addCardListToBoard.execute(BOARD_NAME, LIST_TITLE);
        card = new Card(UUID.randomUUID(), "dishes", "do the dishes today", Optional.empty());
        addCardToCardList.execute(BOARD_NAME, LIST_TITLE, card);
        teamMemberRepository = new InMemoryTeamMemberRepository();
        teamMemberRepository.save(new TeamMember("username"));
    }

    private Card firstCard(List<CardList> cardListList) {
        return firstCardList(cardListList).cards().get(0);
    }

    private CardList firstCardList(List<CardList> cardListList) {
        return cardListList.get(0);
    }
}
