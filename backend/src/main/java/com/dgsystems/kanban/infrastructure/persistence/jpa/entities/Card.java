package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity(name = "Card")
public final class Card {
    @Id
    private UUID id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @ManyToOne
    private Member Member;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_list_id")
    private CardList cardList;

    public CardList getCardList() {
        return cardList;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    public Card(UUID id, String title, String description, Member Member) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.Member = Member;
    }

    public Card() {
        super();
    }

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Member Member() {
        return Member;
    }
}
