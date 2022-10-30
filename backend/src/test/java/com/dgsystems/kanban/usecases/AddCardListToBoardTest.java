package com.dgsystems.kanban.usecases;

import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.entities.MemberNotInTeamException;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryMemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.in_memory.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AddCardListToBoardTest {
    public static final String BOARD_NAME = "new board";
    public static final String CARD_LIST_TITLE = "to do";

    public BoardRepository boardRepository;
    private InMemoryMemberRepository MemberRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        MemberRepository = new InMemoryMemberRepository();
        MemberRepository.save(new Member("owner"));
        Context.initialize(boardRepository);
    }

    @Test
    @DisplayName("Should add card list to board")
    void shouldAddCardListToBoard() throws OwnerDoesNotExistException, MemberNotInTeamException {
        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        Member owner = new Member("owner");
        Optional<Member> memberOptional = Optional.of(owner);
        createBoard.execute(BOARD_NAME, memberOptional);

        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE, memberOptional);

        Board board = boardRepository.getBoard(BOARD_NAME).orElseThrow();
        assertThat(board.cardLists()).
                filteredOn(c -> c.title().equals(CARD_LIST_TITLE)).isNotEmpty();
    }
}
