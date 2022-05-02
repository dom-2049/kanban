package com.dgsystems.kanban.entities;

public class MemberNotInTeamException extends Throwable {
    public MemberNotInTeamException(String username) {
        super("Cannot find " + username + " in team.");
    }
}
