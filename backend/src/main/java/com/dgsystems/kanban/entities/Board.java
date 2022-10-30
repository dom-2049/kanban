package com.dgsystems.kanban.entities;

import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Board(String title, List<CardList> cardLists, List<Member> members, Member owner) {
    public Board addCardList(CardList cardList, Member userResponsibleForOperation) throws MemberNotInTeamException {
        if (memberNotInMembers(userResponsibleForOperation))
            throw new MemberNotInTeamException(userResponsibleForOperation.username());

        List<CardList> newCardLists = new ArrayList<>(cardLists);
        newCardLists.add(cardList);
        return new Board(title(), newCardLists, members, owner);
    }

    private boolean memberNotInMembers(Member userResponsibleForOperation) {
        return !members.contains(userResponsibleForOperation);
    }

    public Board addCard(String cardListTitle, Card card, Member userResponsibleForOperation) throws MemberNotInTeamException {
        if (memberNotInMembers(userResponsibleForOperation))
            throw new MemberNotInTeamException(userResponsibleForOperation.username());

        return new Board(
                title,
                cardLists.stream().map(cl -> cl.title().equals(cardListTitle) ? cl.add(card) : cl)
                        .collect(Collectors.toList()), members,
                owner);
    }

    public Board move(Card card, String from, String to, int previousHashCode, Member userResponsibleForOperation) throws MemberNotInTeamException, BoardAlreadyChangedException {
        if (memberNotInMembers(userResponsibleForOperation))
            throw new MemberNotInTeamException(userResponsibleForOperation.username());

        if (previousHashCode != this.hashCode()) {
            throw new BoardAlreadyChangedException();
        }

        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if (cl.title().equals(from)) {
                return cl.remove(card);
            } else if (cl.title().equals(to)) {
                return cl.add(card);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return new Board(title, cardLists, members, owner);
    }

    public Board addMemberToCard(String cardList, Card card, Member Member, Member userResponsibleForOperation) throws MemberNotInTeamException {
        if (memberNotInMembers(userResponsibleForOperation))
            throw new MemberNotInTeamException(userResponsibleForOperation.username());

        List<CardList> cardLists = cardLists().stream().map(cl -> {
            if (cl.title().equals(cardList)) {
                List<Card> cards = cl.cards().stream().map(c -> {
                    if (c.title().equals(card.title())) {
                        return c.addMember(Member);
                    } else {
                        return c;
                    }
                }).collect(Collectors.toList());

                return new CardList(cl.id(), cl.title(), cards);
            } else {
                return cl;
            }
        }).collect(Collectors.toList());

        return new Board(title, cardLists, members, owner);
    }

    public Board addMember(Member newMember, Member userResponsibleForOperation) throws MemberNotInTeamException {
        if (memberNotInMembers(userResponsibleForOperation))
            throw new MemberNotInTeamException(userResponsibleForOperation.username());

        List<Member> updatedMembers = new ArrayList<>(members);
        updatedMembers.add(newMember);
        return new Board(title(), cardLists(), updatedMembers, owner);
    }

    public Either<MemberNotInTeamException, List<Member>> getAllMembers(Member userResponsibleForOperation) {
        if (memberNotInMembers(userResponsibleForOperation)) return Left.apply(new MemberNotInTeamException(userResponsibleForOperation.username()));

        return Right.apply(members());
    }

    public Optional<Card> getCard(UUID cardId) {
        return cardLists()
                .stream()
                .flatMap(cardList -> cardList.cards().stream())
                .filter(c -> c.id().equals(cardId))
                .findFirst();
    }
}
