package com.dgsystems.kanban.usecases;

import java.util.Optional;

public record GetBoard(BoardRepository boardRepository) {

    public Optional<GetBoardResponse> execute(String boardName) {
        return boardRepository.getBoard(boardName).map(b -> new GetBoardResponse(b.title()));
    }
}
