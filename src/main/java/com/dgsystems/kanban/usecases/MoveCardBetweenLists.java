package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.Card;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record MoveCardBetweenLists(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, String from, String to, Card card, int previousHashCode, BoardMember userResponsibleForOperation) throws Throwable {
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        BoardSession boardSession = new BoardSession();
        Either<Throwable, Board> either = boardSession.move(board, card, from, to, previousHashCode, userResponsibleForOperation);

        if (either instanceof Left l) {
            throw (Throwable) l.value();
        } else if (either instanceof Right r) {
            boardRepository.save((Board) r.value());
        }
    }
}
