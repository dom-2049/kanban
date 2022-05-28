package integration.database;

import com.dgsystems.kanban.Application;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.usecases.AddCardListToBoard;
import com.dgsystems.kanban.usecases.BoardRepository;
import com.dgsystems.kanban.usecases.GetAllBoards;
import com.dgsystems.kanban.usecases.GetBoard;
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

    @BeforeAll
    public void setup() {
        Context.initialize(boardRepository);
        boardRepository.save(new Board(BOARD_NAME, Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    @DisplayName("Should add card list to board after a get all boards")
    void shouldAddCardListToBoardAfterAGetAllBoards() {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        List<Board> boards = getAllBoards.execute();
        Board board = boards.get(0);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        UUID cardListId = addCardListToBoard.execute(board.title(), CARD_LIST_TITLE);
        GetBoard getBoard = new GetBoard(boardRepository);
        Optional<Board> optionalBoard = getBoard.execute(board.title());
        Board expectedBoard = new Board(BOARD_NAME, List.of(new CardList(cardListId, CARD_LIST_TITLE, Collections.emptyList())), Collections.emptyList());
        assertThat(optionalBoard.orElseThrow()).isEqualTo(expectedBoard);
    }
}
