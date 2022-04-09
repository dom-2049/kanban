package com.dgsystems.kanban.usecases;

import akka.actor.ActorSystem;
import com.dgsystems.kanban.boundary.Context;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardAlreadyChangedException;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.MultipleAccessValidator;
import com.dgsystems.kanban.infrastructure.HashingMultipleAccessValidator;
import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveCardBetweenListsInParallelTest {
    public static final String BOARD_NAME = "new board";
    public static final String TO_DO = "to do";
    public static final String IN_PROGRESS = "in progress";
    public static final String DONE = "done";
    public BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardRepository = new InMemoryBoardRepository();
        Context.actorSystem = ActorSystem.create();
    }

    @Test
    @DisplayName("Should not execute last movement when card is moved in parallel")
    void shouldNotExecuteLastMovementWhenCardIsMovedInParallel() throws BrokenBarrierException, InterruptedException {
        Card card = new Card("do the dishes", "must do the dishes!");

        CreateBoard createBoard = new CreateBoard(boardRepository);
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        GetBoard getBoard = new GetBoard(boardRepository);

        createBoard.execute(BOARD_NAME);
        addCardListToBoard.execute(BOARD_NAME, TO_DO);
        addCardListToBoard.execute(BOARD_NAME, IN_PROGRESS);
        addCardListToBoard.execute(BOARD_NAME, DONE);
        addCardToCardList.execute(BOARD_NAME, IN_PROGRESS,card);

        Board beforeExecutionBoard = getBoard.execute(BOARD_NAME).orElseThrow();

        final CyclicBarrier gate = new CyclicBarrier(3);

        Thread t1 = new Thread(new ParallelCardMove(2, card, IN_PROGRESS, TO_DO, gate, beforeExecutionBoard));
        Thread t2 = new Thread(new ParallelCardMove(4, card, IN_PROGRESS, DONE, gate, beforeExecutionBoard));

        t1.start();
        t2.start();

        gate.await(); // start threads in parallel after third gate is unlocked

        t1.join();
        t2.join();

        Board board = getBoard.execute(BOARD_NAME).orElseThrow();

        assertThat(board.cardLists().get(0).cards()).isNotEmpty();
        assertThat(board.cardLists().get(1).cards()).isEmpty();
        assertThat(board.cardLists().get(2).cards()).isEmpty();
    }

    class ParallelCardMove implements Runnable {
        private final int delay;
        private final Card card;
        private final String from;
        private final String to;
        private final CyclicBarrier gate;
        private final Board beforeExecutionBoard;

        public ParallelCardMove(int delay, Card card, String from, String to, CyclicBarrier gate, Board beforeExecutionBoard) {
            this.delay = delay;
            this.card = card;
            this.from = from;
            this.to = to;
            this.gate = gate;
            this.beforeExecutionBoard = beforeExecutionBoard;
        }

        @Override
        public void run() {
            MoveCardBetweenLists moveCardBetweenLists = new MoveCardBetweenLists(boardRepository);
            try {
                gate.await();
                Thread.sleep(delay * 1000);
                MultipleAccessValidator<Board> multipleAccessValidator = new HashingMultipleAccessValidator(beforeExecutionBoard.hashCode());
                moveCardBetweenLists.execute(BOARD_NAME, from, to, card, multipleAccessValidator);
            } catch (InterruptedException | BrokenBarrierException | BoardAlreadyChangedException e) {
                e.printStackTrace();
            }
        }
    }
}
































