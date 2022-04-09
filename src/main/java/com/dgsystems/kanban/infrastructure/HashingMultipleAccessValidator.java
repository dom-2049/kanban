package com.dgsystems.kanban.infrastructure;

import com.dgsystems.kanban.entities.Board;
import com.dgsystems.kanban.entities.MultipleAccessValidator;

public class HashingMultipleAccessValidator implements MultipleAccessValidator<Board> {
    private final int previousHashCode;

    public HashingMultipleAccessValidator(int previousHashCode) {
        this.previousHashCode = previousHashCode;
    }

    @Override
    public boolean canChange(Board board) {
        return previousHashCode == board.hashCode();
    }
}
