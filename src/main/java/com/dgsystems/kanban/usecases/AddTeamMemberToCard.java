package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.TeamMember;

import java.util.Optional;

public record AddTeamMemberToCard(TeamMemberRepository teamMemberRepository, BoardRepository boardRepository) {
    public void execute(String boardName, String cardList, Card card, TeamMember teamMember) throws MemberNotInTeamException {
        if(isMemberInTeam(teamMember)) {
            Board board = boardRepository.getBoard(boardName).orElseThrow();
            BoardManager boardManager = new BoardManager();
            Board newBoard = boardManager.addMemberToCard(board, cardList, card, teamMember);
            boardRepository.save(newBoard);
        } else {
            throw new MemberNotInTeamException(teamMember.username());
        }
    }

    private boolean isMemberInTeam(TeamMember teamMember) {
        Optional<TeamMember> optional = teamMemberRepository.getBy(teamMember.username());
        return optional.isPresent();
    }
}
