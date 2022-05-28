package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.BoardSession;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.BoardMember;
import com.jcabi.aspects.Loggable;

public record AddTeamMemberToCard(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
    @Loggable(prepend = true)
    public void execute(String boardName, String cardList, Card card, BoardMember boardMember) throws MemberNotInTeamException {
        BoardSession boardSession = new BoardSession();
        Board board = boardRepository.getBoard(boardName).orElseThrow();
        if (isMemberInBoard(boardMember, boardSession, board)) {
            Board newBoard = boardSession.addMemberToCard(board, cardList, card, boardMember);
            boardRepository.save(newBoard);
        } else {
            throw new MemberNotInTeamException(boardMember.username());
        }
    }

    private boolean isMemberInBoard(BoardMember boardMember, BoardSession boardSession, Board board) {
        return boardSession.getAllMembers(board).stream().anyMatch(m -> m.username().equals(boardMember.username()));
    }
}
