defmodule KanbanEngine.Card do
  alias __MODULE__
  defstruct [:id, :title, :description, :assignee]

  def new(id, title, description) do
    %Card{id: id, title: title, description: description}
  end

  def assign(card, assignee) do
    %{card | assignee: assignee}
  end
end
