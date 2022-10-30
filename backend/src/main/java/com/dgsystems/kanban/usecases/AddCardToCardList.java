package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.Optional;

public record AddCardToCardList(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, String cardListTitle, Card card, Optional<Member> Member) throws MemberNotInTeamException {
        if (Member.isEmpty()) throw new MemberNotInTeamException("");
        Optional<Board> optional = boardRepository.getBoard(boardName);
        BoardSession boardSession = new BoardSession();

        optional.map(b -> {
            Board updated = boardSession.addCardToCardList(b, cardListTitle, card, Member.get());
            boardRepository.save(updated);
            return null;
        });
    }
}
