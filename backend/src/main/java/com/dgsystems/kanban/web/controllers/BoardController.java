package com.dgsystems.kanban.web.controllers;

import com.dgsystems.kanban.entities.*;
import com.dgsystems.kanban.presenters.GetAllBoardsOutput;
import com.dgsystems.kanban.presenters.GetAllBoardsPresenter;
import com.dgsystems.kanban.presenters.getBoard.Board;
import com.dgsystems.kanban.presenters.getBoard.GetBoardPresenter;
import com.dgsystems.kanban.usecases.*;
import com.dgsystems.kanban.web.AddCardListRequest;
import com.dgsystems.kanban.web.AddCardRequest;
import com.dgsystems.kanban.web.MoveCardRequest;
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
        return getBoard(boardName, principal);
    }

    private Board getBoard(String boardName, Principal principal) throws OwnerDoesNotExistException, MemberNotInTeamException {
        GetBoard getBoard = new GetBoard(boardRepository);
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
    public Board addCardListToBoard(@RequestBody AddCardListRequest request, @PathVariable String boardName, Principal principal) throws MemberNotInTeamException, OwnerDoesNotExistException {
        AddCardListToBoard addCardListToBoard = new AddCardListToBoard(boardRepository);
        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
        addCardListToBoard.execute(boardName, request.cardList(), boardMember);
        return getBoard(boardName, principal);
    }

    @Loggable
    @PostMapping(value = "/{board}/cardlist/{cardlist}")
    @ResponseStatus(HttpStatus.CREATED)
    public Board addCardToCardList(@RequestBody AddCardRequest addCardRequest, @PathVariable String board, @PathVariable String cardlist, Principal principal) throws MemberNotInTeamException, OwnerDoesNotExistException {
        AddCardToCardList addCardToCardList = new AddCardToCardList(boardRepository);
        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
        addCardToCardList.execute(board, cardlist, new Card(UUID.randomUUID(), addCardRequest.cardTitle(), "", Optional.empty()), boardMember);
        return getBoard(board, principal);
    }

    @Loggable
    @PostMapping(value = "/{board}/cardlist/{cardlist}/cards/{card}/move")
    @ResponseStatus(HttpStatus.OK)
    public Board moveCard(@RequestBody MoveCardRequest moveCardRequest, @PathVariable String board, @PathVariable String cardlist, Principal principal) throws Throwable {
        MoveCardBetweenLists moveCardBetweenLists = new MoveCardBetweenLists(boardRepository);
        Optional<BoardMember> boardMember = boardMemberRepository.getBy(principal.getName());
        Card card = boardRepository.getBoard(board).map(b -> b.getCard(moveCardRequest.card())).orElseThrow().orElseThrow();
        moveCardBetweenLists.execute(board, moveCardRequest.from(), moveCardRequest.to(), card, moveCardRequest.boardHashCode(), boardMember.get());
        return getBoard(board, principal);
    }
}
