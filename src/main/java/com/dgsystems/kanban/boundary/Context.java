package com.dgsystems.kanban.boundary;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Context {
    public static ActorSystem actorSystem;

    public static ActorRef boardSupervisor;

    public static void initialize() {
        ActorSystem actorSystem = ActorSystem.create();
        Context.actorSystem = actorSystem;
        Context.boardSupervisor = actorSystem.actorOf(Props.create(BoardSupervisor.class), "boardSupervisor");
    }
}
