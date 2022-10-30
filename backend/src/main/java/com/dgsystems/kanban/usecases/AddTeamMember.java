package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.Member;

public class AddTeamMember {
    private final MemberRepository MemberRepository;

    public AddTeamMember(MemberRepository MemberRepository) {
        this.MemberRepository = MemberRepository;
    }

    public void execute(Member Member) {
        MemberRepository.save(Member);
    }
}
