using Kanban.Pages.Board.Model;

namespace Kanban.Services.MoveCard;

public class MoveCardService
{
    private readonly KanbanApi kanbanApi;

    public MoveCardService(KanbanApi kanbanApi)
    {
        this.kanbanApi = kanbanApi;
    }

    public async Task MoveCard(Board board, Card card, CardList from, CardList to)
    {
        await Task.CompletedTask;
    }
}