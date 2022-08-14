using Kanban.Pages.Board.Model;

namespace Kanban.Services.MoveCard;

public class MoveCardService
{
    private readonly KanbanApi kanbanApi;
    private readonly StateContainerService stateService;

    public MoveCardService(KanbanApi kanbanApi, StateContainerService stateService)
    {
        this.kanbanApi = kanbanApi;
        this.stateService = stateService;
    }

    public async Task MoveCard(Board board, Card card, CardList from, CardList to)
    {
        await kanbanApi.MoveCard(board.Title, from.Title, card.Id, new MoveCardRequest(from.Title, to.Title, card.Id, stateService.BoardHashCode));
    }
}