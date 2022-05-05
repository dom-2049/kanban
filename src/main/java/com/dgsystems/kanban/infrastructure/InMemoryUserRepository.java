package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.web.security.UserAccount;

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
