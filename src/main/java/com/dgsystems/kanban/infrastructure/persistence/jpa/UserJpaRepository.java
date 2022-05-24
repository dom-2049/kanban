package com.dgsystems.kanban.infrastructure.persistence.jpa;

import com.dgsystems.kanban.infrastructure.persistence.UserRepository;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.UserAccount;
import com.jcabi.aspects.Loggable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserJpaRepository implements UserRepository {
    private final UserSpringRepository userSpringRepository;

    public UserJpaRepository(UserSpringRepository userSpringRepository) {
        this.userSpringRepository = userSpringRepository;
    }

    @Loggable(skipArgs = true, skipResult = true)
    @Override
    public Optional<UserAccount> findByUsername(String username) {
        UserAccount user = userSpringRepository.findByUsername(username);
        return Optional.of(user);
    }

    @Override
    @Loggable(skipArgs = true, skipResult = true)
    public UserAccount save(UserAccount newUser) {
       return userSpringRepository.save(newUser);
    }
}
