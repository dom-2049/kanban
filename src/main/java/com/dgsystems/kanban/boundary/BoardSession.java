package com.dgsystems.kanban.boundary;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
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

    public Board startBoard(String boardName, List<CardList> cardLists, List<BoardMember> members, BoardMember owner) {
        ActorRef boardActor = transform(ask(Context.boardSupervisor, new BoardSupervisor.GetOrCreate(boardName, owner.username()), TIMEOUT));
        StartBoard startBoard = new StartBoard(boardName, cardLists, members, owner);
        return transform(ask(boardActor, startBoard, TIMEOUT));
    }

    public Either<MemberNotInTeamException, Board> addCardList(Board board, CardList cardList, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddCardList addCardList = new AddCardList(cardList, userResponsibleForOperation);
        return transform(ask(boardActor, addCardList, TIMEOUT));
    }

    public Either<Throwable, Board> move(Board board, Card card, String from, String to, int previousHashCode, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        Move move = new Move(card, from, to, previousHashCode, userResponsibleForOperation);
        return transform(ask(boardActor, move, TIMEOUT));
    }

    public Either<MemberNotInTeamException, Board> addCardToCardList(Board board, String cardListTitle, Card card, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddCardToCardList addCardToCardList = new AddCardToCardList(cardListTitle, card, userResponsibleForOperation);
        return transform(ask(boardActor, addCardToCardList, TIMEOUT));
    }

    public Either<MemberNotInTeamException, Board> addMemberToCard(Board board, String cardList, Card card, BoardMember boardMember, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddMemberToCard addMemberToCard = new BoardSessionActor.AddMemberToCard(cardList, card, boardMember, userResponsibleForOperation);
        return transform(ask(boardActor, addMemberToCard, TIMEOUT));
    }

    private String actorPath(Board board) {
        return "/user/boardSupervisor/" + board.title().replace(WHITESPACE, UNDERSCORE);
    }

    private <T> T transform(CompletionStage<Object> completionStage) {
        CompletableFuture<Object> boardFuture = completionStage.toCompletableFuture();
        return CompletableFuture
                .completedFuture(boardFuture)
                .thenApply(v -> (T) boardFuture.join())
                .join();
    }

    public Either<MemberNotInTeamException, Board> addMemberToBoard(Board board, BoardMember newMember, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        AddMemberToBoard addMemberToBoard = new BoardSessionActor.AddMemberToBoard(newMember, userResponsibleForOperation);
        return transform(ask(boardActor, addMemberToBoard, TIMEOUT));
    }

    public Either<MemberNotInTeamException, List<BoardMember>> getAllMembers(Board board, BoardMember userResponsibleForOperation) {
        ActorSelection boardActor = Context.actorSystem.actorSelection(actorPath(board));
        GetAllMembers getAllMembers = new BoardSessionActor.GetAllMembers(userResponsibleForOperation);
        return transform(ask(boardActor, getAllMembers, TIMEOUT));
    }

    public void load(List<Board> boards) {
        boards.forEach(b -> startBoard(b.title(), b.cardLists(), b.members(), b.owner()));
    }
}
