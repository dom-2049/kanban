package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "Board")
public class BoardEntity {

    @Id
    private String title;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<CardListEntity> cardlists;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<BoardMemberEntity> members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private BoardMemberEntity owner;

    public String title() {
        return title;
    }

    public BoardMemberEntity getOwner() {
        return owner;
    }

    public Collection<CardListEntity> cardlists() {
        return cardlists;
    }

    public Collection<BoardMemberEntity> members() {
        return members;
    }

    public BoardEntity(String title, Collection<CardListEntity> cardlists, Collection<BoardMemberEntity> members, BoardMemberEntity owner) {
        super();
        this.title = title;
        this.cardlists = cardlists;
        this.members = members;
        this.owner = owner;
    }

    public BoardEntity() {
        super();
    }
}
