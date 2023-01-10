defmodule KanbanEngineTest do
  use ExUnit.Case
  doctest KanbanEngine

  test "greets the world" do
    assert KanbanEngine.hello() == :world
  end
end
