package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddTeamMemberTest {
    @Test
    @DisplayName("should add new team member")
    void shouldAddNewTeamMember() {
        InMemoryMemberRepository MemberRepository = new InMemoryMemberRepository();
        AddTeamMember addTeamMember = new AddTeamMember(MemberRepository);
        addTeamMember.execute(new Member("new member"));

        List<Member> members = MemberRepository.getAll();

        assertThat(members.get(0).username()).isEqualTo("new member");
    }
}
