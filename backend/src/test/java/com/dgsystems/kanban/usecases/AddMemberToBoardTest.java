package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AddMemberToBoardTest {
    public static final String BOARD_NAME = "work";
    private MemberRepository MemberRepository;
    private BoardRepository boardRepository;
    private Member owner;

    @Test
    @DisplayName("Should add new member to board")
    void shouldAddNewMemberToBoard() {
        Member newMember = new Member("new_member");
        AddMemberToBoard addMemberToBoard = new AddMemberToBoard(MemberRepository, boardRepository);
        addMemberToBoard.execute(BOARD_NAME, newMember, owner);

        List<Member> members = new GetAllMembers(MemberRepository, boardRepository).execute(BOARD_NAME, owner);

        assertThat(members).hasSameElementsAs(List.of(owner, newMember));
    }

    @BeforeEach
    void setup() throws OwnerDoesNotExistException {
        MemberRepository = new InMemoryMemberRepository();
        boardRepository = new InMemoryBoardRepository();
        Context.initialize(boardRepository);

        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        owner = new Member("owner");
        MemberRepository.save(owner);
        createBoard.execute(BOARD_NAME, Optional.ofNullable(owner));
    }
}
