package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.jcabi.aspects.Loggable;

import java.util.List;
import java.util.Optional;

public record GetBoard(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Optional<Board> execute(String boardName, Optional<BoardMember> boardMember) {

        Optional<Board> board = boardRepository.getBoard(boardName);
        if(board.isPresent()) {
            BoardSession session = new BoardSession();
            session.load(List.of(board.get()));
        }
        return board;
    }
}
