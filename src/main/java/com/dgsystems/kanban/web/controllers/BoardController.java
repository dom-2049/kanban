package com.dgsystems.kanban.web.controllers;

import com.dgsystems.kanban.entities.BoardMember;
import com.dgsystems.kanban.entities.BoardsDoNotBelongToOwnerException;
import com.dgsystems.kanban.entities.Card;
import com.dgsystems.kanban.entities.OwnerDoesNotExistException;
import com.dgsystems.kanban.presenters.GetAllBoardsOutput;
import com.dgsystems.kanban.presenters.GetAllBoardsPresenter;
import com.dgsystems.kanban.presenters.getBoard.Board;
import com.dgsystems.kanban.presenters.getBoard.GetBoardPresenter;
import com.dgsystems.kanban.usecases.*;
import com.dgsystems.kanban.web.AddCardListRequest;
import com.dgsystems.kanban.web.AddCardRequest;
import com.dgsystems.kanban.web.security.JwtTokenUtil;
import com.jcabi.aspects.Loggable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//TODO: Get user from session and validate user owns board

@RestController
@CrossOrigin(origins = "http://localhost:5019")
@RequestMapping("/board")
public class BoardController {
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    public BoardController(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
    }


    @Loggable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateBoardRequest request) {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);



        BoardMember owner = null; //boardMemberRepository.getBy(); // read from jwt token
        try {
            createBoard.execute(request.boardName(), owner);
        } catch (OwnerDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Loggable
    @GetMapping(value = "/{boardName}")
    public Board get(@PathVariable("boardName") String boardName) {
        com.dgsystems.kanban.usecases.GetBoard getBoard = new com.dgsystems.kanban.usecases.GetBoard(boardRepository);
        return getBoard.execute(boardName).map(b -> {
            GetBoardPresenter presenter = new GetBoardPresenter();
            return presenter.present(b);
        }).orElseThrow();
    }

    @Loggable
    @GetMapping
    public List<GetAllBoardsOutput> getAll() {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        GetAllBoardsPresenter presenter = new GetAllBoardsPresenter();
        try {
            BoardMember owner = null; //boardMemberRepository.getBy(); // read from jwt token

            return presenter.present(getAllBoards.execute(owner));
        } catch (BoardsDoNotBelongToOwnerException e) {
            throw new RuntimeException(e);
        }
    }

    @Loggable
    @PostMapping(value = "/{boardName}/cardlist")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCardListToBoard(@RequestBody AddCardListRequest request, @PathVariable String boardName) {
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        addCardListToBoard.execute(boardName, request.cardList());
    }

    @Loggable
    @PostMapping(value = "/{board}/cardlist/{cardlist}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCardToCardList(@RequestBody AddCardRequest addCardRequest, @PathVariable String board, @PathVariable String cardlist) {
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        addCardToCardList.execute(board, cardlist, new Card(UUID.randomUUID(), addCardRequest.cardTitle(), "", Optional.empty()));
    }
}
