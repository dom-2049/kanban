package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

public class CreateBoardTest {
    public BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        Context.initialize(boardRepository);
    }

    @Test
    @DisplayName("Should create empty board")
    void shouldCreateEmptyBoard() {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        Board expected = new Board(BoardSuiteTest.BOARD_NAME, Collections.emptyList(), Collections.emptyList());

        createBoard.execute(BoardSuiteTest.BOARD_NAME);
        Optional<Board> board = boardRepository.getBoard(BoardSuiteTest.BOARD_NAME);

        Assertions.assertThat(board).isEqualTo(Optional.of(expected));
    }
}