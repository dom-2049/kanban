package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.List;

public record GetAllMembers(MemberRepository MemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)

    public List<Member> execute(String board, Member userResponsibleForOperation) {
        Either<MemberNotInTeamException, List<Member>> either = boardRepository.getBoard(board).map(b -> new BoardSession().getAllMembers(b, userResponsibleForOperation)).orElseThrow();

        if (either instanceof Left l) {
            throw new RuntimeException((MemberNotInTeamException) l.value());
        } else if (either instanceof Right r) {
            return (List<Member>) r.value();
        }
        else {
            throw new IllegalStateException();
        }
    }
}
