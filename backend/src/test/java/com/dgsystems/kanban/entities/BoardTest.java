package com.dgsystems.kanban.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BoardTest {
    @Test
    @DisplayName("board should return card")
    public void boardShouldReturnCard() {
        BoardMember owner = new BoardMember("owner");
        Board board = new Board("board", Collections.emptyList(), Collections.emptyList(), owner);
        board.addCardList(new CardList(UUID.randomUUID(), "to do", Collections.emptyList()), owner);
        Card expected = new Card(UUID.randomUUID(), "dishes", "dishes", Optional.of(owner));
        board.addCard("to do", expected, owner);

        Card actual = board.getCard("dishes");
        assertThat(actual).isEqualTo(expected);
    }
}
