package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CreateBoardTest {

    public static final String BOARD_NAME = "new board";

    @Test
    @DisplayName("Should create empty board")
    void shouldCreateEmptyBoard() {
        BoardRepository repository = new InMemoryBoardRepository();
        CreateBoard createBoard = new CreateBoard(repository);
        Board expected = new Board(BOARD_NAME);

        createBoard.execute(BOARD_NAME);
        Optional<Board> board = repository.getBoard(BOARD_NAME);

        assertThat(board).isEqualTo(Optional.of(expected));
    }
}