package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.List;

public record GetAllBoardMembers(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)

    public List<BoardMember> execute(String board, BoardMember userResponsibleForOperation) {
        Either<MemberNotInTeamException, List<BoardMember>> either = boardRepository.getBoard(board).map(b -> new BoardSession().getAllMembers(b, userResponsibleForOperation)).orElseThrow();

        if (either instanceof Left l) {
            throw new RuntimeException((MemberNotInTeamException) l.value());
        } else if (either instanceof Right r) {
            return (List<BoardMember>) r.value();
        }
        else {
            throw new IllegalStateException();
        }
    }
}
