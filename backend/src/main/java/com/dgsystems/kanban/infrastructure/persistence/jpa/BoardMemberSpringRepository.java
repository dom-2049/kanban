package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.entities.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MemberSpringRepository extends CrudRepository<com.dgsystems.kanban.infrastructure.persistence.jpa.entities.Member, String> {
    Member findByUsername(String username);
}
