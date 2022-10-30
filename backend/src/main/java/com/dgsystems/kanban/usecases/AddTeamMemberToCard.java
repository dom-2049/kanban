package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.Member;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record AddTeamMemberToCard(MemberRepository MemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Member execute(String boardName, String cardList, Card card, Member Member, Member userResponsibleForOperation) throws MemberNotInTeamException {
        BoardSession boardSession = new BoardSession();
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        Board either = boardSession.addMemberToCard(board, cardList, card, Member, userResponsibleForOperation);
            boardRepository.save(either);
            return Member;
    }
}
