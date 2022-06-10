package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardEntity;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardMemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BoardSpringRepository extends CrudRepository<BoardEntity, String> {
    BoardEntity findByTitle(String title);
    List<BoardEntity> findByOwner(BoardMemberEntity owner);
}
