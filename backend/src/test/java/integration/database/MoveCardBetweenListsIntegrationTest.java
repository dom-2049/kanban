package integration.database;

import com.dgsystems.kanban.Application;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.usecases.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveCardBetweenListsIntegrationTest {
    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String BOARD_NAME = UUID.randomUUID().toString();
    private static final String CARD_LIST_TITLE = UUID.randomUUID().toString();
    private static final String DO_THE_DISHES = "do the dishes";
    private static final UUID CARD_ID = UUID.randomUUID();

    @Resource
    BoardMemberRepository boardMemberRepository;
    @Resource
    BoardRepository boardRepository;

    @BeforeEach
    public void setup() {
        Context.initialize(boardRepository);
        boardMemberRepository.save(new BoardMember("owner"));
    }

    @Test
    @DisplayName("Should move card back and forth")
    void shouldMoveCardBackAndForth() throws Throwable {
        BoardMember boardMember = new BoardMember(USERNAME);
        Card card = new Card(CARD_ID, DO_THE_DISHES, DO_THE_DISHES, Optional.empty());
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        MoveCardBetweenLists moveCardBetweenLists = new MoveCardBetweenLists(boardRepository);
        AddTeamMember addTeamMember = new AddTeamMember(boardMemberRepository);
        AddMemberToBoard addMemberToBoard = new AddMemberToBoard(boardMemberRepository, boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);
        BoardMember owner = new BoardMember("owner");

        createBoard.execute(BOARD_NAME, Optional.of(owner));
        addTeamMember.execute(new BoardMember(USERNAME));
        addMemberToBoard.execute(BOARD_NAME, boardMember, owner);
        addCardListToBoard.execute(BOARD_NAME, "to do", Optional.of(owner));
        addCardListToBoard.execute(BOARD_NAME, "in progress", Optional.of(owner));
        addCardToCardList.execute(BOARD_NAME, "to do", card, Optional.of(owner));
        int previousHashCode = getBoard.execute(BOARD_NAME, Optional.of(boardMember)).get().hashCode();
        moveCardBetweenLists.execute(BOARD_NAME, "to do", "in progress", card, previousHashCode, boardMember);
        previousHashCode = getBoard.execute(BOARD_NAME, Optional.of(boardMember)).get().hashCode();
        moveCardBetweenLists.execute(BOARD_NAME, "in progress", "to do", card, previousHashCode, boardMember);

        Board board = getBoard.execute(BOARD_NAME, Optional.of(boardMember)).orElseThrow();
        CardList todo = board.cardLists().stream().filter(cl -> cl.title().equals("to do")).findFirst().orElseThrow();
        CardList inProgress = board.cardLists().stream().filter(cl -> cl.title().equals("in progress")).findFirst().orElseThrow();
        assertThat(todo.cards()).anyMatch(c -> c.equals(card));
        assertThat(inProgress.cards()).noneMatch(c -> c.equals(card));
    }
}
