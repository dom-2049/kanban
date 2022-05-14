package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddTeamMemberTest {
    @Test
    @DisplayName("should add new team member")
    void shouldAddNewTeamMember() {
        InMemoryBoardMemberRepository boardMemberRepository = new InMemoryBoardMemberRepository();
        AddTeamMember addTeamMember = new AddTeamMember(boardMemberRepository);
        addTeamMember.execute(new BoardMember("new member"));

        List<BoardMember> members = boardMemberRepository.getAll();

        assertThat(members.get(0).username()).isEqualTo("new member");
    }
}
