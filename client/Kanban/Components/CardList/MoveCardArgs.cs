using Kanban.Pages.Board.Model;

namespace Kanban.Components.CardList;

public record MoveCardArgs(Pages.Board.Model.Card card, Direction direction);