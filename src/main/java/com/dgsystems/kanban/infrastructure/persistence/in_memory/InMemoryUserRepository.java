package com.dgsystems.kanban.infrastructure.persistence.in_memory;

import com.dgsystems.kanban.infrastructure.persistence.UserRepository;
import com.dgsystems.kanban.infrastructure.persistence.jpa.entities.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final List<UserAccount> users = new ArrayList<>();

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return users.stream().filter(b -> b.getUsername().equals(username)).findFirst();
    }

    @Override
    public UserAccount save(UserAccount newUser) {
        List<UserAccount> filtered = users.stream()
                .filter(b -> Objects.equals(b.getUsername(), newUser.getUsername()))
                .toList();

        if(filtered.isEmpty()) {
            users.add(newUser);
        } else {
            users.remove(filtered.get(0));
            users.add(newUser);
        }

        return newUser;
    }
}
