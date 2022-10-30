package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class AddCardListToBoard {
    private final BoardRepository boardRepository;

    public AddCardListToBoard(BoardRepository repository) {
        boardRepository = repository;
    }

    @Loggable(prepend = true)
    public UUID execute(String boardName, String cardListTitle, Optional<Member> Member) throws MemberNotInTeamException {
        if (Member.isEmpty()) throw new MemberNotInTeamException("");
        Optional<Board> optional = boardRepository.getBoard(boardName);
        UUID id = UUID.randomUUID();
        optional.map(b -> {
            BoardSession boardSession = new BoardSession();
            Board board = boardSession.addCardList(b, new CardList(id, cardListTitle, Collections.emptyList()), Member.get());
            boardRepository.save(board);
            return board;
        });

        return id;
    }
}
