package com.dgsystems.kanban.presenters;

import com.dgsystems.kanban.entities.Board;

public class GetBoardPresenter {
    public GetBoardOutput present(Board boardResponse) {
        return new GetBoardOutput(boardResponse.title());
    }
}
