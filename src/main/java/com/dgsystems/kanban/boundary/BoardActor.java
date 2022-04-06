package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;

import java.util.List;

public class BoardActor extends AbstractActor {
    record Move(Card card, String from, String to) { }
    record AddCardList(CardList cardList) { }
    record CreateBoard(String boardName, List<CardList> cardLists) { }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Board board;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Move.class,
                        m -> {
                            Board newBoard = board.move(m.card, m.from, m.to);
                            sender().tell(newBoard, self());
                        })
                .match(
                        CreateBoard.class,
                        c -> {
                            board = new Board(c.boardName, c.cardLists);
                            sender().tell(board, self());
                        }
                )
                .match(
                        AddCardList.class,
                        a -> {
                            board = board.addCardList(a.cardList());
                            sender().tell(board, self());
                        }
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }


}
