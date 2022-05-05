package com.dgsystems.kanban.entities;

import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Board(String title, List<CardList> cardLists, List<BoardMember> members) {
    public Board addCardList(CardList cardList) {
        List<CardList> newCardLists = new ArrayList<>(cardLists);
        newCardLists.add(cardList);
        return new Board(title(), newCardLists, members);
    }

    public Board addCard(String cardListTitle, Card card) {
        return new Board(
                title,
                cardLists.stream().map(cl -> cl.title().equals(cardListTitle) ? cl.add(card) : cl)
                        .collect(Collectors.toList()), members
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

        return Right.apply(new Board(title, cardLists, members));
    }

    public Board addMemberToCard(String cardList, Card card, BoardMember boardMember) {
        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if(cl.title().equals(cardList)) {
                List<Card> cards = cl.cards().stream().map(c -> {
                    if(c.title().equals(card.title())) {
                        return c.addMember(boardMember);
                    } else {
                        return c;
                    }
                }).collect(Collectors.toList());

                return new CardList(cl.id(), cl.title(), cards);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return new Board(title, cardLists, members);
    }

    public Board addMember(BoardMember newMember) {
        List<BoardMember> updatedMembers = new ArrayList<>(members);
        updatedMembers.add(newMember);
        return new Board(title(), cardLists(), updatedMembers);
    }

    public List<BoardMember> getAllMembers() {
        return members();
    }
}
