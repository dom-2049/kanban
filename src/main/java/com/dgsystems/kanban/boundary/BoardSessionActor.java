package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.dgsystems.kanban.entities.*;
import com.jcabi.aspects.Loggable;
import scala.util.Either;
import scala.util.Right;

import java.util.List;

class BoardSessionActor extends AbstractActor {
    record Move(Card card, String from, String to, int previousHashCode, BoardMember userResponsibleForOperation) { }
    record AddCardList(CardList cardList, BoardMember userResponsibleForOperation) { }
    record StartBoard(String boardName, List<CardList> cardLists, List<BoardMember> members, BoardMember owner) { }
    record AddCardToCardList(String cardListTitle, Card card, BoardMember userResponsibleForOperation) { }
    record AddMemberToCard(String cardList, Card card, BoardMember boardMember, BoardMember userResponsibleForOperation) { }
    record AddMemberToBoard(BoardMember newMember, BoardMember userResponsibleForOperation) { }
    record GetAllMembers(BoardMember userResponsibleForOperation) { }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Board board;

    @Loggable
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Move.class,
                        m -> {
                            Either<Throwable, Board> either = board.move(m.card, m.from, m.to, m.previousHashCode, m.userResponsibleForOperation);

                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }

                            sender().tell(either, self());
                        })
                .match(
                        StartBoard.class,
                        s -> {
                            board = new Board(s.boardName, s.cardLists, s.members, s.owner);
                            sender().tell(board, self());
                        }
                )
                .match(
                        AddCardList.class,
                        a -> {
                            Either<MemberNotInTeamException, Board> either = board.addCardList(a.cardList(), a.userResponsibleForOperation);
                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }
                            sender().tell(either, self());
                        }
                )
                .match(
                        AddCardToCardList.class,
                        a -> {
                            Either<MemberNotInTeamException, Board> either = board.addCard(a.cardListTitle(), a.card(), a.userResponsibleForOperation);
                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }
                            sender().tell(either, self());
                        }
                )
                .match(
                        AddMemberToCard.class,
                        a -> {
                            Either<MemberNotInTeamException, Board> either = board.addMemberToCard(a.cardList(), a.card(), a.boardMember(), a.userResponsibleForOperation);
                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }
                            sender().tell(either, self());
                        }
                )
                .match(
                        AddMemberToBoard.class,
                        a -> {
                            Either<MemberNotInTeamException, Board> either = board.addMember(a.newMember(), a.userResponsibleForOperation);
                            if (either instanceof Right right) {
                                this.board = (Board) right.value();
                            }
                            sender().tell(either, self());
                        }
                )
                .match(
                        GetAllMembers.class,
                        g -> sender().tell(board.getAllMembers(g.userResponsibleForOperation), self())
                )
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
