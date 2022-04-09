package com.dgsystems.kanban.entities;

public interface MultipleAccessValidator<T> {
    boolean canChange(T t);
}
