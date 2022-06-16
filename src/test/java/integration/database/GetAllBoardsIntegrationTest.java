package integration.database;

import com.dgsystems.kanban.Application;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.usecases.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetAllBoardsIntegrationTest {
    private static final String BOARD_NAME = "work";
    private static final String CARD_LIST_TITLE = "in progress";

    @Resource
    BoardRepository boardRepository;

    @Resource
    BoardMemberRepository boardMemberRepository;

    @BeforeAll
    public void setup() {
        Context.initialize(boardRepository);
        BoardMember owner = new BoardMember("owner");
        boardMemberRepository.save(owner);
        boardRepository.save(new Board(BOARD_NAME, Collections.emptyList(), Collections.emptyList(), owner));
    }

    @Test
    @DisplayName("Should add card list to board after a get all boards")
    void shouldAddCardListToBoardAfterAGetAllBoards() throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        BoardMember owner = new BoardMember("owner");
        List<Board> boards = getAllBoards.execute(Optional.of(owner));
        Board board = boards.get(0);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        UUID cardListId = addCardListToBoard.execute(board.title(), CARD_LIST_TITLE, Optional.of(owner));
        GetBoard getBoard = new GetBoard(boardRepository);
        Optional<Board> optionalBoard = getBoard.execute(board.title(), boardMemberRepository.getBy(owner.username()));
        Board expectedBoard = new Board(BOARD_NAME, List.of(new CardList(cardListId, CARD_LIST_TITLE, Collections.emptyList())), Collections.emptyList(), owner);
        assertThat(optionalBoard.orElseThrow()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("Should return only boards created by user")
    void shouldReturnOnlyBoardsCreatedByUser() throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException {
        BoardMember user1 = new BoardMember("user1");
        BoardMember user2 = new BoardMember("user2");
        boardMemberRepository.save(user1);
        boardMemberRepository.save(user2);

        Board user1Board = new Board("user1Board", Collections.emptyList(), singletonList(user1), user1);
        Board user2Board = new Board("user2Board", Collections.emptyList(), singletonList(user2), user2);

        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        createBoard.execute("user1Board", Optional.of(user1));
        createBoard.execute("user2Board", Optional.of(user2));

        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        List<Board> boardsUser1 = getAllBoards.execute(Optional.of(user1));
        List<Board> boardsUser2 = getAllBoards.execute(Optional.of(user2));

        assertThat(boardsUser1).isEqualTo(singletonList(user1Board));
        assertThat(boardsUser2).isEqualTo(singletonList(user2Board));
    }
}
