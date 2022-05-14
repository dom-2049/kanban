package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.BoardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BoardSpringRepository extends CrudRepository<BoardEntity, String> {
    BoardEntity findByTitle(String title);
}
