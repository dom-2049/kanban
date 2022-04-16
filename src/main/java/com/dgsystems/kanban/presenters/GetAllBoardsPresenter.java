package com.dgsystems.kanban.presenters;

import com.dgsystems.kanban.entities.Board;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllBoardsPresenter {
    public List<GetAllBoardsOutput> present(List<Board> boards) {
        return boards.stream().map(b -> new GetAllBoardsOutput(b.title())).collect(Collectors.toList());
    }
}
