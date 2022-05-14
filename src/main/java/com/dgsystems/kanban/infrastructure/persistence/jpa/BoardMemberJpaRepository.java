package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardMemberEntity;
import com.dgsystems.kanban.usecases.BoardMemberRepository;
import com.jcabi.aspects.Loggable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class BoardMemberJpaRepository implements BoardMemberRepository {
    private final BoardMemberSpringRepository boardMemberSpringRepository;

    public BoardMemberJpaRepository(BoardMemberSpringRepository boardMemberSpringRepository) {
        this.boardMemberSpringRepository = boardMemberSpringRepository;
    }

    @Loggable
    @Override
    public Optional<BoardMember> getBy(String username) {
        BoardMemberEntity member = boardMemberSpringRepository.findByUsername(username);
        if(member != null) {
            return Optional.of(new BoardMember(member.username()));
        } else {
            return Optional.empty();
        }
    }

    @Loggable
    @Override
    public void save(BoardMember boardMember) {
        boardMemberSpringRepository.save(new BoardMemberEntity(boardMember.username()));
    }

    @Loggable
    @Override
    public List<BoardMember> getAll() {
        Iterable<BoardMemberEntity> allMembers = boardMemberSpringRepository.findAll();
        List<BoardMember> result = new ArrayList<>();
        for (BoardMemberEntity boardMember :
                allMembers) {
            result.add(new BoardMember(boardMember.username()));
        }
        return result;
    }
}
