package com.dgsystems.kanban.boundary;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.dgsystems.kanban.usecases.BoardRepository;

public class Context {
    public static ActorSystem actorSystem;
    public static ActorRef boardSupervisor;
    public static BoardRepository boardRepository;

    public static void initialize(BoardRepository boardRepository) {
        if(boardRepository == null) throw new RuntimeException("board repository can't be null!");
        Context.boardRepository = boardRepository;
        ActorSystem actorSystem = ActorSystem.create();
        Context.actorSystem = actorSystem;
        Context.boardSupervisor = actorSystem.actorOf(Props.create(BoardSupervisor.class), "boardSupervisor");
    }
}
