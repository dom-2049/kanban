package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.Member;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class BoardSupervisor extends AbstractActor {
    public static final String WHITESPACE = " ";
    public static final String UNDERSCORE = "_";
    Map<String, ActorRef> children = new HashMap<>();

    record GetOrCreate(String boardName, String username) {}
    @Override
    public Receive createReceive() {
       return receiveBuilder().match(
               GetOrCreate.class,
               g -> {
                    if(children.containsKey(g.boardName)) {
                        sender().tell(children.get(g.boardName), self());
                    }
                    else {
                        ActorRef boardSessionActor = getContext().actorOf(Props.create(BoardSessionActor.class), g.boardName.replace(WHITESPACE, UNDERSCORE));
                        children.put(g.boardName, boardSessionActor);

                        Board board = Context.boardRepository
                                .getBoard(g.boardName)
                                .orElse(new Board(g.boardName, Collections.emptyList(), Collections.emptyList(), new Member(g.username)));
                        boardSessionActor.tell(new BoardSessionActor.StartBoard(board.title(), board.cardLists(), board.members(), board.owner()), self());
                        sender().tell(boardSessionActor, self());
                    }
               }
       ).build();
    }
}
