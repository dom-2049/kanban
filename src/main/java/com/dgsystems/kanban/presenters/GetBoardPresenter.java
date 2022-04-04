package com.dgsystems.kanban.presenters;

import com.dgsystems.kanban.usecases.GetBoardResponse;

public class GetBoardPresenter {
    public GetBoardOutput present(GetBoardResponse getBoardResponse) {
        return new GetBoardOutput(getBoardResponse.title());
    }
}
