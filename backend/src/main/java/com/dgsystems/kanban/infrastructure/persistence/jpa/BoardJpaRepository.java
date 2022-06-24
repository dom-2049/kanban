package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.CardList;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardEntity;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardMemberEntity;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.CardEntity;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.CardListEntity;
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
    public Optional<Board> getBoard(String boardName) {
        BoardEntity boardEntity = boardSpringRepository.findByTitle(boardName);
        if(boardEntity == null)
            return Optional.empty();
        else
            return Optional.of(getBoard(boardEntity));
    }

    @Loggable
    @Override
    public void save(Board board) {
        BoardEntity entity = getBoard(board);
        boardSpringRepository.save(entity);
    }

    @Loggable
    @Override
    public List<Board> getAllForOwner(BoardMember owner) {
        Iterable<BoardEntity> boards = boardSpringRepository.findByOwner(new BoardMemberEntity(owner.username()));
        List<Board> result = new ArrayList<>();

        for (BoardEntity boardEntity : boards) {
            result.add(getBoard(boardEntity));
        }

        return result;
    }

    private BoardEntity getBoard(Board board) {
        return new BoardEntity(board.title(), getCardLists(board.cardLists()), getMembers(board.members()), new BoardMemberEntity(board.owner().username()));
    }

    private Collection<BoardMemberEntity> getMembers(List<BoardMember> members) {
        return members.stream().map(m -> new BoardMemberEntity(m.username())).collect(Collectors.toList());
    }

    private Collection<CardListEntity> getCardLists(List<CardList> cardLists) {
        return cardLists.stream().map(this::getCardList).collect(Collectors.toList());
    }

    private CardListEntity getCardList(CardList cl) {
        return new CardListEntity(cl.id(), cl.title(), getCards(cl.cards()));
    }

    private Collection<CardEntity> getCards(List<Card> cards) {
        return cards.stream().map(c -> new CardEntity(c.id(), c.title(), c.description(), c.teamMember().map(tm -> new BoardMemberEntity(tm.username())).orElse(null))).collect(Collectors.toList());
    }

    private Board getBoard(BoardEntity b) {
        if(b != null) {
            return new Board(b.title(), getCardLists(b.cardlists()), getMembers(b.members()), getBoardMember(b.getOwner()));
        }
        return null;
    }

    private List<BoardMember> getMembers(Collection<BoardMemberEntity> members) {
        return members.stream().map(this::getBoardMember).collect(Collectors.toList());
    }

    private List<CardList> getCardLists(Collection<CardListEntity> cardListEntities) {
        if(cardListEntities.size() == 0)
            return Collections.emptyList();
        return cardListEntities.stream().map(this::getCardList).collect(Collectors.toList());
    }

    private CardList getCardList(CardListEntity cl) {
        return new CardList(cl.id(), cl.title(), getCards(cl.cards()));
    }

    private List<Card> getCards(Collection<CardEntity> cards) {
        return cards.stream().map(this::getCard).collect(Collectors.toList());
    }

    private BoardMember getBoardMember(BoardMemberEntity m) {
        return new BoardMember(m.username());
    }

    private Card getCard(CardEntity c) {
        return new Card(c.id(), c.title(), c.description(), c.boardMember() != null ? Optional.of(new BoardMember(c.boardMember().username())) : Optional.empty());
    }
}
