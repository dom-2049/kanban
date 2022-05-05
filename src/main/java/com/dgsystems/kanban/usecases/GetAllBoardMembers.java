package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.BoardMember;

import java.util.List;

public record GetAllBoardMembers(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    public List<BoardMember> execute(String board) {
        return boardRepository.getBoard(board).map(b -> new BoardManager().getAllMembers(b)).orElseThrow();
    }
}
