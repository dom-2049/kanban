package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record AddMemberToBoard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, BoardMember newMember, BoardMember userResponsibleForOperation) {
        boardRepository.getBoard(boardName).map(board -> {
            BoardSession boardSession = new BoardSession();
            Board board1 = boardSession.addMemberToBoard(board, newMember, userResponsibleForOperation);
            boardRepository.save(board1);
            return board1;
        });
    }
}
