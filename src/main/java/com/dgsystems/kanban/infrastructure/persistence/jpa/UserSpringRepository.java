package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserSpringRepository extends CrudRepository<UserAccount, Integer> {

    UserAccount findByUsername(String username);
}
