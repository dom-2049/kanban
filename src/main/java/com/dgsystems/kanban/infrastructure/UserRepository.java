package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.web.security.UserAccount;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository {
	UserAccount findByUsername(String username);

	UserAccount save(UserAccount newUser);
}
