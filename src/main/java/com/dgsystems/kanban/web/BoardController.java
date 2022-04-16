package com.dgsystems.kanban.web;

import com.dgsystems.kanban.infrastructure.InMemoryBoardRepository;
import com.dgsystems.kanban.presenters.GetAllBoardsOutput;
import com.dgsystems.kanban.presenters.GetAllBoardsPresenter;
import com.dgsystems.kanban.presenters.GetBoardOutput;
import com.dgsystems.kanban.presenters.GetBoardPresenter;
import com.dgsystems.kanban.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardController {
    public BoardController() {
        boardRepository = new InMemoryBoardRepository();
    }

    private final BoardRepository boardRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateBoardRequest request) {
        CreateBoard createBoard = new CreateBoard(boardRepository);
        createBoard.execute(request.boardName());
    }

    @GetMapping(value = "/{boardName}")
    public GetBoardOutput get(@PathVariable("boardName") String boardName) {
        GetBoard getBoard = new GetBoard(boardRepository);
        return getBoard.execute(boardName).map(b -> {
            GetBoardPresenter presenter = new GetBoardPresenter();
            return presenter.present(b);
        }).orElseThrow();
    }

    @GetMapping
    public List<GetAllBoardsOutput> getAll() {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        GetAllBoardsPresenter presenter = new GetAllBoardsPresenter();
        return presenter.present(getAllBoards.execute());
    }
}
