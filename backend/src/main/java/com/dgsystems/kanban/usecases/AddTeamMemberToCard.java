package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.BoardMember;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record AddTeamMemberToCard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public BoardMember execute(String boardName, String cardList, Card card, BoardMember boardMember, BoardMember userResponsibleForOperation) throws MemberNotInTeamException {
        BoardSession boardSession = new BoardSession();
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        Either<MemberNotInTeamException, Board> either = boardSession.addMemberToCard(board, cardList, card, boardMember, userResponsibleForOperation);

        if (either instanceof Left l) {
            throw (MemberNotInTeamException) l.value();
        } else if (either instanceof Right r) {
            Board newBoard = (Board) r.value();
            boardRepository.save(newBoard);
            return boardMember;
        } else {
            throw new IllegalStateException();
        }
    }
}
