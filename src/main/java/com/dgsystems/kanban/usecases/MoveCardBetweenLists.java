package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.Card;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record MoveCardBetweenLists(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, String from, String to, Card card, int previousHashCode) throws BoardAlreadyChangedException {
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        BoardManager boardManager = new BoardManager();
        Either<BoardAlreadyChangedException, Board> either = boardManager.move(board, card, from, to, previousHashCode);

        if (either instanceof Left l) {
            throw (BoardAlreadyChangedException) l.value();
        } else if (either instanceof Right r) {
            boardRepository.save((Board) r.value());
        }
    }
}
