package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.BoardsDoNotBelongToOwnerException;
import com.jcabi.aspects.Loggable;

import java.util.List;

public class GetAllBoards {
    private final BoardRepository boardRepository;

    public GetAllBoards(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Loggable(prepend = true)
    public List<Board> execute(BoardMember owner) throws BoardsDoNotBelongToOwnerException {
        List<Board> boards = boardRepository.getAllForOwner(owner);
        if (boards.stream().allMatch(b -> b.owner().equals(owner))) {
            BoardSession session = new BoardSession();
            session.load(boards);
            return boards;
        }
        else {
            throw new BoardsDoNotBelongToOwnerException();
        }
    }
}
