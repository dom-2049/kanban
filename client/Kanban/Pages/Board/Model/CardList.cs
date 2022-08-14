namespace Kanban.Pages.Board.Model
{
    public record CardList(Guid Id, string Title, List<Card> Cards);
}
