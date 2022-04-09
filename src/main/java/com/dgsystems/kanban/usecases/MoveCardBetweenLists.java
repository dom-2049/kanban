package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MultipleAccessValidator;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public record MoveCardBetweenLists(BoardRepository boardRepository) {
    public void execute(String boardName, String from, String to, Card card, MultipleAccessValidator<Board> multipleAccessValidator) throws BoardAlreadyChangedException {
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        BoardManager boardManager = new BoardManager();
        Either<BoardAlreadyChangedException, Board> either = boardManager.move(board, card, from, to, multipleAccessValidator);

        if (either instanceof Left l) {
            throw (BoardAlreadyChangedException) l.value();
        } else if (either instanceof Right r) {
            boardRepository.save((Board) r.value());
        }
    }
}
