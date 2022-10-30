package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

@Entity(name = "CardList")
public class CardList {
    @Id
    private UUID id;
    @NotEmpty
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_title")
    private Board board;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Card> cards;

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Collection<Card> cards() {
        return cards;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public CardList() {
        super();
    }

    public CardList(UUID id, String title, Collection<Card> cards) {
        this();
        this.id = id;
        this.title = title;
        this.cards = cards;
    }
}
