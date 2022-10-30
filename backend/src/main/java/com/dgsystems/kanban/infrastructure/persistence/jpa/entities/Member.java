package com.dgsystems.kanban.infrastructure.persistence.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity(name = "Member")
public final class Member {
    public Member(String username) {
        this.username = username;
    }

    public Member() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @NotEmpty
    private String username;

    @ManyToMany(mappedBy = "members")
    private Set<Board> participatingBoards;
}
