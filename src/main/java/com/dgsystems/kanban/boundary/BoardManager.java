package com.dgsystems.kanban.boundary;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import com.dgsystems.kanban.entities.*;
import scala.util.Either;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.dgsystems.kanban.boundary.BoardActor.CreateBoard;
import static com.dgsystems.kanban.boundary.BoardActor.AddCardList;
import static com.dgsystems.kanban.boundary.BoardActor.Move;
import static com.dgsystems.kanban.boundary.BoardActor.AddCardToCardList;
import static com.dgsystems.kanban.boundary.BoardActor.AddMemberToCard;

import static akka.pattern.Patterns.ask;

public class BoardManager {
    public static final String WHITESPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final Duration TIMEOUT = Duration.ofMillis(1000);

    public Board createBoard(String boardName, List<CardList> cardLists) {
        ActorRef boardActor = Context.actorSystem.actorOf(Props.create(BoardActor.class), boardName.replace(WHITESPACE, UNDERSCORE));
        CreateBoard addCardList = new CreateBoard(boardName, cardLists);
        return transform(ask(boardActor, addCardList, TIMEOUT));
    }

    public Board addCardList(Board board, CardList cardList) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddCardList addCardList = new AddCardList(cardList);
        return transform(ask(boardActor, addCardList, TIMEOUT));
    }

    public Either<BoardAlreadyChangedException, Board> move(Board board, Card card, String from, String to, int previousHashCode) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        Move move = new Move(card, from, to, previousHashCode);
        return transform(ask(boardActor, move, TIMEOUT));
    }

    public Board addCardToCardList(Board board, String cardListTitle, Card card) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddCardToCardList addCardToCardList = new AddCardToCardList(cardListTitle, card);
        return transform(ask(boardActor, addCardToCardList, TIMEOUT));
    }

    public Board addMemberToCard(Board board, String cardList, Card card, BoardMember boardMember) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddMemberToCard addMemberToCard = new BoardActor.AddMemberToCard(cardList, card, boardMember);
        return transform(ask(boardActor, addMemberToCard, TIMEOUT));
    }

    private String actorPath(Board board) {
        return "/user/" + board.title().replace(WHITESPACE, UNDERSCORE);
    }

    private <T> T transform(CompletionStage<Object> completionStage) {
        CompletableFuture<Object> boardFuture = completionStage.toCompletableFuture();
        return CompletableFuture
                .completedFuture(boardFuture)
                .thenApply(v -> (T) boardFuture.join())
                .join();
    }

}
