package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

@Entity(name = "CardList")
public class CardListEntity {
    @Id
    private UUID id;
    @NotEmpty
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_title")
    private BoardEntity board;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<CardEntity> cards;

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Collection<CardEntity> cards() {
        return cards;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public CardListEntity() {
        super();
    }

    public CardListEntity(UUID id, String title, Collection<CardEntity> cards) {
        this();
        this.id = id;
        this.title = title;
        this.cards = cards;
    }
}
