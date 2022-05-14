package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity(name = "Card")
public final class CardEntity {
    @Id
    private UUID id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @ManyToOne
    private BoardMemberEntity boardMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_list_id")
    private CardListEntity cardList;

    public CardListEntity getCardList() {
        return cardList;
    }

    public void setCardList(CardListEntity cardList) {
        this.cardList = cardList;
    }

    public CardEntity(UUID id, String title, String description, BoardMemberEntity boardMember) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.boardMember = boardMember;
    }

    public CardEntity() {
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

    public BoardMemberEntity boardMember() {
        return boardMember;
    }
}
