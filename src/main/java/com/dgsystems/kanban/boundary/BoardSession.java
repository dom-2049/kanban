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

import static com.dgsystems.kanban.boundary.BoardSessionActor.StartBoard;
import static com.dgsystems.kanban.boundary.BoardSessionActor.AddCardList;
import static com.dgsystems.kanban.boundary.BoardSessionActor.Move;
import static com.dgsystems.kanban.boundary.BoardSessionActor.AddCardToCardList;
import static com.dgsystems.kanban.boundary.BoardSessionActor.AddMemberToCard;
import static com.dgsystems.kanban.boundary.BoardSessionActor.AddMemberToBoard;
import static com.dgsystems.kanban.boundary.BoardSessionActor.GetAllMembers;

import static akka.pattern.Patterns.ask;

public class BoardSession {
    public static final String WHITESPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final Duration TIMEOUT = Duration.ofMillis(1000);

    public Board startBoard(String boardName, List<CardList> cardLists, List<BoardMember> members) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(boardName), TIMEOUT));
        StartBoard addCardList = new StartBoard(boardName, cardLists, members);
        return transform(ask(boardActor, addCardList, TIMEOUT));
    }

    public Board addCardList(Board board, CardList cardList) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        AddCardList addCardList = new AddCardList(cardList);
        return transform(ask(boardActor, addCardList, TIMEOUT));
    }

    public Either<BoardAlreadyChangedException, Board> move(Board board, Card card, String from, String to, int previousHashCode) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        Move move = new Move(card, from, to, previousHashCode);
        return transform(ask(boardActor, move, TIMEOUT));
    }

    public Board addCardToCardList(Board board, String cardListTitle, Card card) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        AddCardToCardList addCardToCardList = new AddCardToCardList(cardListTitle, card);
        return transform(ask(boardActor, addCardToCardList, TIMEOUT));
    }

    public Board addMemberToCard(Board board, String cardList, Card card, BoardMember boardMember) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        AddMemberToCard addMemberToCard = new BoardSessionActor.AddMemberToCard(cardList, card, boardMember);
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

    public Board addMemberToBoard(Board board, BoardMember newMember) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        AddMemberToBoard addMemberToBoard = new BoardSessionActor.AddMemberToBoard(newMember);
        return transform(ask(boardActor, addMemberToBoard, TIMEOUT));
    }

    public List<BoardMember> getAllMembers(Board board) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(board.title()), TIMEOUT));
        GetAllMembers getAllMembers = new BoardSessionActor.GetAllMembers();
        return transform(ask(boardActor, getAllMembers, TIMEOUT));
    }
}
