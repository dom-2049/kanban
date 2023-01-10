defmodule KanbanEngine.Board do
  alias KanbanEngine.{Board, CardList, Card}

  defstruct [:title, :cardlists, :members, :owner]

  def new(title, owner) do
    %Board{title: title, cardlists: %{}, members: Map.put(%{}, owner.name, owner), owner: owner}
  end

  def add_cardlist(board, cardlist, performer) do
    perform_if_allowed(board, performer,
      fn (board)->
        %{board | cardlists: Map.put(board.cardlists, cardlist.id, cardlist)} end)
  end


  def add_card(board, cardlist_key, card, performer) do
    perform_if_allowed(board, performer,
      fn (board) ->
        updated = Map.update!(board.cardlists, cardlist_key, &(CardList.add(&1, card)))
        %{board | cardlists: updated}
    end)
  end

  def add_member_to_card(board, cardlist_key, card, assignee, performer) do
    case is_performer_allowed?(board, performer) do
      true ->
        updated = Map.update!(board.cardlists, cardlist_key, &(Map.update!(&1.cards, card.id, fn(card) -> Card.assign(card, assignee) end)))
        %{board | cardlists: updated}
      false -> {:error, :performer_not_allowed}
    end
  end

  defp perform_if_allowed(board, performer, action) do
    case is_performer_allowed?(board, performer) do
      true -> action.(board)
      false -> {:error, :performer_not_allowed}
    end
  end

  defp is_performer_allowed?(board, performer) do
    Map.has_key?(board.members, performer.name)
  end
end
