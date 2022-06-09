package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.jcabi.aspects.Loggable;

import java.util.Collections;

public record CreateBoard(BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public Board execute(String boardName, BoardMember owner) {
        BoardSession boardSession = new BoardSession();
        Board board = boardSession.startBoard(boardName, Collections.emptyList(), Collections.emptyList());
        boardRepository.save(board);
        return board;
    }
}
