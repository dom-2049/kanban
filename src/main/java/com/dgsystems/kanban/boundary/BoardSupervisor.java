package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class BoardSupervisor extends AbstractActor {
    public static final String WHITESPACE = " ";
    public static final String UNDERSCORE = "_";
    Map<String, ActorRef> children = new HashMap<>();

    record GetOrCreate(String boardName) {}
    @Override
    public Receive createReceive() {
       return receiveBuilder().match(
               GetOrCreate.class,
               g -> {
                    if(children.containsKey(g.boardName)) {
                        sender().tell(children.get(g.boardName), self());
                    }
                    else {
                        ActorRef boardActor = getContext().actorOf(Props.create(BoardSessionActor.class), g.boardName.replace(WHITESPACE, UNDERSCORE));
                        children.put(g.boardName, boardActor);

                        Board board = Context.boardRepository
                                .getBoard(g.boardName)
                                .orElse(new Board(g.boardName, Collections.emptyList(), Collections.emptyList(), new BoardMember("owner")));
                        boardActor.tell(new BoardSessionActor.StartBoard(board.title(), board.cardLists(), board.members()), self());
                        sender().tell(boardActor, self());
                    }
               }
       ).build();
    }
}
