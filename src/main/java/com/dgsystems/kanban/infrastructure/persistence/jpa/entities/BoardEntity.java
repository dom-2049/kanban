package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "Board")
public class BoardEntity {

    @Id
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<CardListEntity> cardlists;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<BoardMemberEntity> members;

    public String title() {
        return title;
    }

    public Collection<CardListEntity> cardlists() {
        return cardlists;
    }

    public Collection<BoardMemberEntity> members() {
        return members;
    }

    public BoardEntity(String title, Collection<CardListEntity> cardlists, Collection<BoardMemberEntity> members) {
        super();
        this.title = title;
        this.cardlists = cardlists;
        this.members = members;
    }

    public BoardEntity() {
        super();
    }
}
