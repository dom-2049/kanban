package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
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
    public UUID execute(String boardName, String cardListTitle, Optional<BoardMember> boardMember) throws MemberNotInTeamException {
        if (boardMember.isEmpty()) throw new MemberNotInTeamException("");
        Optional<Board> optional = boardRepository.getBoard(boardName);
        UUID id = UUID.randomUUID();
        optional.map(b -> {
            BoardSession boardSession = new BoardSession();
            Board board = boardSession.addCardList(b, new CardList(id, cardListTitle, Collections.emptyList()), boardMember.get());
            boardRepository.save(board);
            return board;
        });

        return id;
    }
}
