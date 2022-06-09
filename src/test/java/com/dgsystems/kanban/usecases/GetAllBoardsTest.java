package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class GetAllBoardsTest {
    public BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        Context.initialize(boardRepository);
    }

    @Test
    @DisplayName("Should return only boards created by user")
    void shouldReturnOnlyBoardsCreatedByUser() {
        BoardMember user1 = new BoardMember("user1");
        BoardMember user2 = new BoardMember("user2");

        Board user1Board = new Board("user1Board", Collections.emptyList(), singletonList(user1), user1);
        Board user2Board = new Board("user2Board", Collections.emptyList(), singletonList(user2), user2);

        CreateBoard createBoard = new CreateBoard(boardRepository);
        createBoard.execute("user1Board", user1);
        createBoard.execute("user2Board", user2);

        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        List<Board> boardsUser1 = getAllBoards.execute(user1);
        List<Board> boardsUser2 = getAllBoards.execute(user2);

        assertThat(boardsUser1).isEqualTo(singletonList(user1Board));
        assertThat(boardsUser2).isEqualTo(singletonList(user2Board));
    }
}