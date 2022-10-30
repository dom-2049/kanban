package integration.database;

import com.dgsystems.kanban.Application;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddMemberToCardIntegrationTest {
    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String BOARD_NAME = UUID.randomUUID().toString();
    private static final String CARD_LIST_TITLE = UUID.randomUUID().toString();
    private static final String DO_THE_DISHES = "do the dishes";
    private static final UUID CARD_ID = UUID.randomUUID();

    @Resource
    MemberRepository MemberRepository;
    @Resource
    BoardRepository boardRepository;

    @BeforeEach
    public void setup() {
        Context.initialize(boardRepository);
        MemberRepository.save(new Member("owner"));
    }

    @Test
    @DisplayName("Should add team member to card in integration with database")
    void shouldAddTeamMemberToCardInIntegrationWithDatabase() throws MemberNotInTeamException, OwnerDoesNotExistException {
        Member Member = new Member(USERNAME);
        Card card = new Card(CARD_ID, DO_THE_DISHES, DO_THE_DISHES, Optional.empty());
        CreateBoard createBoard = new CreateBoard(boardRepository, MemberRepository);
        AddTeamMember addTeamMember = new AddTeamMember(MemberRepository);
        AddMemberToBoard addMemberToBoard = new AddMemberToBoard(MemberRepository, boardRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        AddTeamMemberToCard addTeamMemberToCard = new AddTeamMemberToCard(MemberRepository, boardRepository);
        Member owner = new Member("owner");

        createBoard.execute(BOARD_NAME, Optional.of(owner));
        addTeamMember.execute(new Member(USERNAME));
        addMemberToBoard.execute(BOARD_NAME, Member, owner);
        UUID cardListId = addCardListToBoard.execute(BOARD_NAME, CARD_LIST_TITLE, Optional.of(owner));
        addCardToCardList.execute(BOARD_NAME, CARD_LIST_TITLE, card, Optional.of(owner));
        addTeamMemberToCard.execute(BOARD_NAME, CARD_LIST_TITLE, card, Member, owner);

        GetBoard getBoard = new GetBoard(boardRepository);
        Board board = getBoard.execute(BOARD_NAME, MemberRepository.getBy(owner.username())).orElseThrow();

        Board expectedBoard = expectedBoard(Member, cardListId);
        assertThat(board).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedBoard);
    }

    private Board expectedBoard(Member Member, UUID cardListId) {
        Card expectedCard = new Card(CARD_ID, DO_THE_DISHES, DO_THE_DISHES, Optional.of(Member));
        CardList expectedCardList = new CardList(cardListId, CARD_LIST_TITLE, List.of(expectedCard));
        return new Board(BOARD_NAME, List.of(expectedCardList), List.of(Member, new Member("owner")), new Member("owner"));
    }
}
