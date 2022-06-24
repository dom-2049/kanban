package com.dgsystems.kanban.web.controllers;

import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.presenters.GetAllBoardsOutput;
import com.dgsystems.kanban.presenters.GetAllBoardsPresenter;
import com.dgsystems.kanban.presenters.getBoard.Board;
import com.dgsystems.kanban.presenters.getBoard.GetBoardPresenter;
import com.dgsystems.kanban.usecases.*;
import com.dgsystems.kanban.web.AddCardListRequest;
import com.dgsystems.kanban.web.AddCardRequest;
import com.jcabi.aspects.Loggable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void create(@RequestBody CreateBoardRequest request, Principal principal) {
        CreateBoard createBoard = new CreateBoard(boardRepository, boardMemberRepository);

        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());

        try {
            createBoard.execute(request.boardName(), boardMember);
        } catch (OwnerDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Loggable
    @GetMapping(value = "/{boardName}")
    public Board get(@PathVariable("boardName") String boardName, Principal principal) throws OwnerDoesNotExistException, MemberNotInTeamException {
        com.dgsystems.kanban.usecases.GetBoard getBoard = new com.dgsystems.kanban.usecases.GetBoard(boardRepository);
        return getBoard.execute(boardName, boardMemberRepository.getBy(principal.getName())).map(b -> {
            GetBoardPresenter presenter = new GetBoardPresenter();
            return presenter.present(b);
        }).orElseThrow();
    }

    @Loggable
    @GetMapping
    public List<GetAllBoardsOutput> getAll(Principal principal) {
        GetAllBoards getAllBoards = new GetAllBoards(boardRepository);
        GetAllBoardsPresenter presenter = new GetAllBoardsPresenter();
        try {
            Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
            return presenter.present(getAllBoards.execute(boardMember));
        } catch (BoardsDoNotBelongToOwnerException | OwnerDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Loggable
    @PostMapping(value = "/{boardName}/cardlist")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCardListToBoard(@RequestBody AddCardListRequest request, @PathVariable String boardName, Principal principal) throws MemberNotInTeamException {
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
        addCardListToBoard.execute(boardName, request.cardList(), boardMember);
    }

    @Loggable
    @PostMapping(value = "/{board}/cardlist/{cardlist}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCardToCardList(@RequestBody AddCardRequest addCardRequest, @PathVariable String board, @PathVariable String cardlist, Principal principal) throws MemberNotInTeamException {
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
        addCardToCardList.execute(board, cardlist, new Card(UUID.randomUUID(), addCardRequest.cardTitle(), "", Optional.empty()), boardMember);
    }
}
