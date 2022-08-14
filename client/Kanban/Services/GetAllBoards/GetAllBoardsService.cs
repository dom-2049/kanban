namespace Kanban.Services.GetAllBoards
{
    public class GetAllBoardsService
    {
        private readonly KanbanApi kanbanApi;

        public GetAllBoardsService(KanbanApi kanbanApi)
        {
            this.kanbanApi = kanbanApi;
        }

        public async Task<Board[]> GetAllBoards()
        {
            var boards = await kanbanApi.GetAllBoards();
            return boards.Select(b => new Board(b.Title, b.BoardHashCode)).ToArray();
        }
    }
}
