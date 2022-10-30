package com.dgsystems.kanban.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BoardTest {
    @Test
    @DisplayName("board should return card")
    public void boardShouldReturnCard() throws MemberNotInTeamException {
        Member owner = new Member("owner");
        Card expected = new Card(UUID.randomUUID(), "dishes", "dishes", Optional.of(owner));
        Board board = new Board("board", Collections.emptyList(), List.of(owner), owner)
                .addCardList(new CardList(UUID.randomUUID(), "to do", Collections.emptyList()), owner)
                .addCard("to do", expected, owner);

        Card actual = board.getCard(expected.id()).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
}
