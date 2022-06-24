package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity(name = "BoardMember")
public class BoardMemberEntity {
    @Id
    @NotEmpty
    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_title")
    private BoardEntity board;

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public BoardMemberEntity() {
        super();
    }

    public BoardMemberEntity(String username) {
        super();
        this.username = username;
    }

    public String username() {
        return username;
    }
}
