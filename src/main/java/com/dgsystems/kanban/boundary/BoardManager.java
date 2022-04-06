package com.dgsystems.kanban.boundary;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.Patterns.ask;

public class BoardManager {
public BoardManager() {
    //TODO: Create actor only on createBoard, get the reference for the actor afterwards.
        boardActor = Context.actorSystem.actorOf(Props.create(BoardActor.class));
    }

    private final ActorRef boardActor;

    public Board createBoard(String boardName, List<CardList> cardLists) {
        BoardActor.CreateBoard addCardList = new BoardActor.CreateBoard(boardName, cardLists);
        CompletableFuture<Object> boardFuture = ask(boardActor, addCardList, Duration.ofMillis(1000)).toCompletableFuture();
        CompletableFuture<Board> transformed =
                CompletableFuture.completedFuture(boardFuture)
                        .thenApply(v -> (Board) boardFuture.join());
        return transformed.join();
    }

    public Board addCardList(Board board, CardList cardList) {
        BoardActor.AddCardList addCardList = new BoardActor.AddCardList(cardList);
        CompletableFuture<Object> boardFuture = ask(boardActor, addCardList, Duration.ofMillis(1000)).toCompletableFuture();
        CompletableFuture<Board> transformed =
                CompletableFuture.completedFuture(boardFuture)
                        .thenApply(v -> (Board) boardFuture.join());
        return transformed.join();
    }

    public Board move(Board board, Card card, String from, String to) {
        // actor selection or other way to get the actor by the board name
        BoardActor.Move move = new BoardActor.Move(card, from, to);
        CompletableFuture<Object> boardFuture = ask(boardActor, move, Duration.ofMillis(1000)).toCompletableFuture();
        CompletableFuture<Board> transformed =
                CompletableFuture.completedFuture(boardFuture)
                    .thenApply(v -> (Board) boardFuture.join());
        return transformed.join();
    }
}
