package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.entities.Member;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BoardSpringRepository extends CrudRepository<Board, String> {
    Board findByTitle(String title);
    List<Board> findByOwner(com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member owner);
}
