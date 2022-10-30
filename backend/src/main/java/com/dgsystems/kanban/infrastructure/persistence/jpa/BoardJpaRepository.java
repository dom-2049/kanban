package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Board;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Card;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.CardList;
import com.dgsystems.kanban.usecases.BoardRepository;
import com.jcabi.aspects.Loggable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BoardJpaRepository implements BoardRepository {
    private final BoardSpringRepository boardSpringRepository;

    public BoardJpaRepository(BoardSpringRepository boardSpringRepository) {
        this.boardSpringRepository = boardSpringRepository;
    }

    @Loggable
    @Override
    public Optional<com.dgsystems.kanban.entities.Board> getBoard(String boardName) {
        Board board = boardSpringRepository.findByTitle(boardName);
        if(board == null)
            return Optional.empty();
        else
            return Optional.of(getBoard(board));
    }

    @Loggable
    @Override
    public void save(com.dgsystems.kanban.entities.Board board) {
        Board entity = getBoard(board);
        boardSpringRepository.save(entity);
    }

    @Loggable
    @Override
    public List<com.dgsystems.kanban.entities.Board> getAllForOwner(com.dgsystems.kanban.entities.Member owner) {
        Iterable<Board> boards = boardSpringRepository.findByOwner(new com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member(owner.username()));
        List<com.dgsystems.kanban.entities.Board> result = new ArrayList<>();

        for (Board board : boards) {
            result.add(getBoard(board));
        }

        return result;
    }

    private Board getBoard(com.dgsystems.kanban.entities.Board board) {
        return new Board(board.title(), getCardLists(board.cardLists()), getMembers(board.members()), new com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member(board.owner().username()));
    }

    private Set<com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member> getMembers(List<com.dgsystems.kanban.entities.Member> members) {
        return members.stream().map(m -> new com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member(m.username())).collect(Collectors.toSet());
    }

    private Set<CardList> getCardLists(List<com.dgsystems.kanban.entities.CardList> cardLists) {
        return cardLists.stream().map(this::getCardList).collect(Collectors.toSet());
    }

    private CardList getCardList(com.dgsystems.kanban.entities.CardList cl) {
        return new CardList(cl.id(), cl.title(), getCards(cl.cards()));
    }

    private Collection<Card> getCards(List<com.dgsystems.kanban.entities.Card> cards) {
        return cards.stream().map(c -> new Card(c.id(), c.title(), c.description(), c.teamMember().map(tm -> new com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member(tm.username())).orElse(null))).collect(Collectors.toList());
    }

    private com.dgsystems.kanban.entities.Board getBoard(Board b) {
        if(b != null) {
            return new com.dgsystems.kanban.entities.Board(b.title(), getCardLists(b.cardlists()), getMembers(b.members()), getMember(b.getOwner()));
        }
        return null;
    }

    private List<com.dgsystems.kanban.entities.Member> getMembers(Collection<com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member> members) {
        return members.stream().map(this::getMember).collect(Collectors.toList());
    }

    private List<com.dgsystems.kanban.entities.CardList> getCardLists(Collection<CardList> cardListEntities) {
        if(cardListEntities.size() == 0)
            return Collections.emptyList();
        return cardListEntities.stream().map(this::getCardList).collect(Collectors.toList());
    }

    private com.dgsystems.kanban.entities.CardList getCardList(CardList cl) {
        return new com.dgsystems.kanban.entities.CardList(cl.id(), cl.title(), getCards(cl.cards()));
    }

    private List<com.dgsystems.kanban.entities.Card> getCards(Collection<Card> cards) {
        return cards.stream().map(this::getCard).collect(Collectors.toList());
    }

    private com.dgsystems.kanban.entities.Member getMember(com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member m) {
        return new com.dgsystems.kanban.entities.Member(m.getUsername());
    }

    private com.dgsystems.kanban.entities.Card getCard(Card c) {
        return new com.dgsystems.kanban.entities.Card(c.id(), c.title(), c.description(), c.Member() != null ? Optional.of(new com.dgsystems.kanban.entities.Member(c.Member().getUsername())) : Optional.empty());
    }
}
