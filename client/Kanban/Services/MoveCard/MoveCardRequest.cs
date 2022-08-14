namespace Kanban.Services.MoveCard;

public record MoveCardRequest(string from, string to, Guid card, int boardHashCode);