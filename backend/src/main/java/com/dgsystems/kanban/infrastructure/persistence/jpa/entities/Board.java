package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity(name = "Board")
public class Board {

    @Id
    private String title;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CardList> cardlists;

    @ManyToMany
    @JoinTable(
            name = "boards_members",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "title")
    )
    private Set<Member> members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private Member owner;

    public String title() {
        return title;
    }

    public Member getOwner() {
        return owner;
    }

    public Collection<CardList> cardlists() {
        return cardlists;
    }

    public Collection<Member> members() {
        return members;
    }

    public Board(String title, Set<CardList> cardlists, Set<Member> members, Member owner) {
        super();
        this.title = title;
        this.cardlists = cardlists;
        this.members = members;
        this.owner = owner;
    }

    public Board() {
        super();
    }
}
