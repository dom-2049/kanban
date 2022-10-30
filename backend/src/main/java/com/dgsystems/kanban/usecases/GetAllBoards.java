package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.BoardsDoNotBelongToOwnerException;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.jcabi.aspects.Loggable;

import java.util.List;
import java.util.Optional;

public class GetAllBoards {
    private final BoardRepository boardRepository;

    public GetAllBoards(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Loggable(prepend = true)
    public List<Board> execute(Optional<Member> owner) throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException {
        if(owner.isEmpty()) throw new OwnerDoesNotExistException();
        List<Board> boards = boardRepository.getAllForOwner(owner.get());
        if (boards.stream().allMatch(b -> b.owner().equals(owner.get()))) {
            BoardSession session = new BoardSession();
            session.load(boards);
            return boards;
        }
        else {
            throw new BoardsDoNotBelongToOwnerException();
        }
    }
}
