package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record AddMemberToBoard(MemberRepository MemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, Member newMember, Member userResponsibleForOperation) {
        boardRepository.getBoard(boardName).map(board -> {
            BoardSession boardSession = new BoardSession();
            Board board1 = boardSession.addMemberToBoard(board, newMember, userResponsibleForOperation);
            boardRepository.save(board1);
            return board1;
        });
    }
}
