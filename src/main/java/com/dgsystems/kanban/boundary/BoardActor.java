package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dgsystems.kanban.entities.*;
import scala.util.Either;
import scala.util.Right;

import java.util.Collections;
import java.util.List;

public class BoardActor extends AbstractActor {
    record Move(Card card, String from, String to, int previousHashCode) { }
    record AddCardList(CardList cardList) { }
    record CreateBoard(String boardName, List<CardList> cardLists) { }
    record AddCardToCardList(String cardListTitle, Card card) { }
    record AddMemberToCard(String cardList, Card card, BoardMember boardMember) { }
    record AddMemberToBoard(BoardMember newMember) { }
    record GetAllMembers() { }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Board board;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Move.class,
                        m -> {
                            Either<BoardAlreadyChangedException, Board> either = board.move(m.card, m.from, m.to, m.previousHashCode);

                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }

                            sender().tell(either, self());
                        })
                .match(
                        CreateBoard.class,
                        c -> {
                            board = new Board(c.boardName, c.cardLists, Collections.emptyList());
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
                .match(
                        AddCardToCardList.class,
                        a -> {
                            board = board.addCard(a.cardListTitle(), a.card());
                            sender().tell(board, self());
                        }
                )
                .match(
                        AddMemberToCard.class,
                        a -> {
                            board = board.addMemberToCard(a.cardList(), a.card(), a.boardMember());
                            sender().tell(board, self());
                        }
                )
                .match(
                        AddMemberToBoard.class,
                        a -> {
                            board = board.addMember(a.newMember());
                            sender().tell(board, self());
                        }
                )
                .match(
                        GetAllMembers.class,
                        g -> sender().tell(board.getAllMembers(), self())
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
