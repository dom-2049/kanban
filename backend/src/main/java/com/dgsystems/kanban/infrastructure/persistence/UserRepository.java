package com.dgsystems.kanban.infrastructure.persistence;

import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository {
	Optional<UserAccount> findByUsername(String username);

	UserAccount save(UserAccount newUser);
}
