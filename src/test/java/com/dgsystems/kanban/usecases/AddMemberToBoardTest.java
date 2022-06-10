package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class AddMemberToBoardTest {
    public static final String BOARD_NAME = "work";
    private BoardMemberRepository boardMemberRepository;
    private BoardRepository boardRepository;
    private BoardMember owner;

    @Test
    @DisplayName("Should add new member to board")
    void shouldAddNewMemberToBoard() {
        BoardMember newMember = new BoardMember("new_member");
        AddMemberToBoard addMemberToBoard = new AddMemberToBoard(boardMemberRepository, boardRepository);
        addMemberToBoard.execute(BOARD_NAME, newMember);

        List<BoardMember> members = new GetAllBoardMembers(boardMemberRepository, boardRepository).execute(BOARD_NAME);

        assertThat(members).hasSameElementsAs(List.of(owner, newMember));
    }

    @BeforeEach
    void setup() throws OwnerDoesNotExistException {
        boardMemberRepository = new InMemoryBoardMemberRepository();
        boardRepository = new InMemoryBoardRepository();
        Context.initialize(boardRepository);

        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        owner = new BoardMember("owner");
        boardMemberRepository.save(owner);
        createBoard.execute(BOARD_NAME, owner);
    }
}
