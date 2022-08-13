package com.dgsystems.kanban.web;

public record MoveCardRequest(String from, String to, String card, int boardHashCode) {
}
