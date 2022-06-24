package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BoardMemberSpringRepository extends CrudRepository<BoardMemberEntity, String> {
    BoardMemberEntity findByUsername(String username);
}
