defmodule KanbanEngine.CardList do
  alias KanbanEngine.CardList

  defstruct [:id, :title, :cards]

  def new(id, title) do
    %CardList{id: id, title: title, cards: %{}}
  end

  def add(cardlist, card) do
    %{cardlist | cards: Map.put(cardlist.cards, card.id, card)}
  end

  def remove(cardlist, card) do
    %{cardlist | cards: Map.delete(cardlist.cards, card.id)}
  end
end
