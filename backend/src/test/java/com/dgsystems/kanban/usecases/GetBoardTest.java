package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetBoardTest {

    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";
    public static final String TO_DO = "to do";
    public static final String IN_PROGRESS = "in progress";

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
    @DisplayName("Should get board")
    void shouldGetBoard() throws OwnerDoesNotExistException, MemberNotInTeamException {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        BoardMember owner = new BoardMember("owner");

        createBoard.execute(BOARD_NAME, Optional.of(owner));
        Board response = getBoard.execute(BOARD_NAME, boardMemberRepository.getBy(owner.username())).orElseThrow();

        assertThat(response.title()).isEqualTo(BOARD_NAME);
        //TODO: validate card lists are immutable
    }

    @Test
    @DisplayName("Should throw error when trying to get board but member not in board members")
    void shouldThrowErrorWhenTryingToGetBoardButMemberNotInBoardMembers() throws OwnerDoesNotExistException {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        BoardMember owner = new BoardMember("owner");

        createBoard.execute(BOARD_NAME, Optional.of(owner));
        assertThrows(MemberNotInTeamException.class, () -> getBoard.execute(BOARD_NAME, Optional.of(new BoardMember("invalid_user"))));
    }
}