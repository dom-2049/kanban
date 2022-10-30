package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.usecases.MemberRepository;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member;
import com.jcabi.aspects.Loggable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class MemberJpaRepository implements MemberRepository {
    private final MemberSpringRepository MemberSpringRepository;

    public MemberJpaRepository(MemberSpringRepository MemberSpringRepository) {
        this.MemberSpringRepository = MemberSpringRepository;
    }

    @Loggable
    @Override
    public Optional<com.dgsystems.kanban.entities.Member> getBy(String username) {
        com.dgsystems.kanban.entities.Member member = MemberSpringRepository.findByUsername(username);
        if(member != null) {
            return Optional.of(new com.dgsystems.kanban.entities.Member(member.username()));
        } else {
            return Optional.empty();
        }
    }

    @Loggable
    @Override
    public void save(com.dgsystems.kanban.entities.Member Member) {
        MemberSpringRepository.save(new Member(Member.username()));
    }

    @Loggable
    @Override
    public List<com.dgsystems.kanban.entities.Member> getAll() {
        Iterable<Member> allMembers = MemberSpringRepository.findAll();
        List<com.dgsystems.kanban.entities.Member> result = new ArrayList<>();
        for (Member Member :
                allMembers) {
            result.add(new com.dgsystems.kanban.entities.Member(Member.getUsername()));
        }
        return result;
    }
}
