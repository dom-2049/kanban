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
    MemberRepository MemberRepository;

    @BeforeAll
    public void setup() throws OwnerDoesNotExistException {
        Context.initialize(boardRepository);
        Member owner = new Member("owner");
        MemberRepository.save(owner);
        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        createBoard.execute(BOARD_NAME, Optional.of(owner));
    }

    @Test
    @DisplayName("Should add card list to board after a get all boards")
    void shouldAddCardListToBoardAfterAGetAllBoards() throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException, MemberNotInTeamException {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        Member owner = new Member("owner");
        List<Board> boards = getAllBoards.execute(Optional.of(owner));
        Board board = boards.get(0);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        UUID cardListId = addCardListToBoard.execute(board.title(), CARD_LIST_TITLE, Optional.of(owner));
        GetBoard getBoard = new GetBoard(boardRepository);
        Optional<Board> optionalBoard = getBoard.execute(board.title(), MemberRepository.getBy(owner.username()));
        Board expectedBoard = new Board(BOARD_NAME, List.of(new CardList(cardListId, CARD_LIST_TITLE, Collections.emptyList())), singletonList(owner), owner);
        assertThat(optionalBoard.orElseThrow()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("Should return only boards created by user")
    void shouldReturnOnlyBoardsCreatedByUser() throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException {
        Member user1 = new Member("user1");
        Member user2 = new Member("user2");
        MemberRepository.save(user1);
        MemberRepository.save(user2);

        Board user1Board = new Board("user1Board", Collections.emptyList(), singletonList(user1), user1);
        Board user2Board = new Board("user2Board", Collections.emptyList(), singletonList(user2), user2);

        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        createBoard.execute("user1Board", Optional.of(user1));
        createBoard.execute("user2Board", Optional.of(user2));

        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        List<Board> boardsUser1 = getAllBoards.execute(Optional.of(user1));
        List<Board> boardsUser2 = getAllBoards.execute(Optional.of(user2));

        assertThat(boardsUser1).isEqualTo(singletonList(user1Board));
        assertThat(boardsUser2).isEqualTo(singletonList(user2Board));
    }

    @Test
    @DisplayName("Should get two boards created by same user")
    void shouldGetTwoBoardsCreatedBySameUser() throws BoardsDoNotBelongToOwnerException, OwnerDoesNotExistException {
        Member user1 = new Member("user1");
        MemberRepository.save(user1);

        Board expectedWorkBoard = new Board("work", Collections.emptyList(), singletonList(user1), user1);
        Board expectedHobbyBoard = new Board("hobby", Collections.emptyList(), singletonList(user1), user1);

        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        createBoard.execute("work", Optional.of(user1));
        createBoard.execute("hobby", Optional.of(user1));

        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        List<Board> boardsUser1 = getAllBoards.execute(Optional.of(user1));

        assertThat(boardsUser1).isEqualTo(List.of(expectedWorkBoard, expectedHobbyBoard));
    }
}
