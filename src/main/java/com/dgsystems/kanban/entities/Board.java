package com.dgsystems.kanban.entities;

import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Board(String title, List<CardList> cardLists) {
    public Board addCardList(CardList cardList) {
        List<CardList> newCardLists = new ArrayList<>(cardLists);
        newCardLists.add(cardList);
        return new Board(title(), newCardLists);
    }

    public Board addCard(String cardListTitle, Card card) {
        return new Board(
                title,
                cardLists.stream().map(cl -> cl.title().equals(cardListTitle) ? cl.add(card) : cl)
                        .collect(Collectors.toList())
        );
    }

    public Either<BoardAlreadyChangedException, Board> move(Card card, String from, String to, int previousHashCode) {
        if(previousHashCode != this.hashCode()) {
            return Left.apply(new BoardAlreadyChangedException());
        }

        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if(cl.title().equals(from)) {
                return cl.remove(card);
            } else if(cl.title().equals(to)) {
                return cl.add(card);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return Right.apply( new Board(title, cardLists));
    }

    public Board addMemberToCard(String cardList, Card card, TeamMember teamMember) {
        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if(cl.title().equals(cardList)) {
                List<Card> cards = cl.cards().stream().map(c -> {
                    if(c.title().equals(card.title())) {
                        return c.addMember(teamMember);
                    } else {
                        return c;
                    }
                }).collect(Collectors.toList());

                return new CardList(cl.id(), cl.title(), cards);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return new Board(title, cardLists);
    }
}
