package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.entities.BoardMember;

public class AddTeamMember {
    private final BoardMemberRepository boardMemberRepository;

    public AddTeamMember(BoardMemberRepository boardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
    }

    public void execute(BoardMember boardMember) {
        boardMemberRepository.save(boardMember);
    }
}
