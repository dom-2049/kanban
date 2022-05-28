package com.dgsystems.kanban.boundary;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;

public class BoardSupervisor extends AbstractActor {
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
                        sender().tell(boardActor, self());
                    }
               }
       ).build();
    }
}
