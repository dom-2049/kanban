package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardManager;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.BoardMember;

import java.util.Optional;

public record AddTeamMemberToCard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    public void execute(String boardName, String cardList, Card card, BoardMember boardMember) throws MemberNotInTeamException {
        if(isMemberInBoard(boardMember)) {
            Board board = boardRepository.getBoard(boardName).orElseThrow();
            BoardManager boardManager = new BoardManager();
            Board newBoard = boardManager.addMemberToCard(board, cardList, card, boardMember);
            boardRepository.save(newBoard);
        } else {
            throw new MemberNotInTeamException(boardMember.username());
        }
    }

    private boolean isMemberInBoard(BoardMember boardMember) {
        Optional<BoardMember> optional = boardMemberRepository.getBy(boardMember.username());
        return optional.isPresent();
    }
}
