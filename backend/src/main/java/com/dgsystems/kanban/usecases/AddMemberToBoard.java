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
            Either<MemberNotInTeamException, Board> either = boardSession.addMemberToBoard(board, newMember, userResponsibleForOperation);

            if (either instanceof Left l) {
                throw new RuntimeException((MemberNotInTeamException) l.value());
            } else if (either instanceof Right r) {
                Board updated = (Board) r.value();
                boardRepository.save(updated);
                return updated;
            } else {
                throw new IllegalStateException();
            }
        }).orElseThrow();
    }
}
