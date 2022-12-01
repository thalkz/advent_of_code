defmodule Solution do
  def process_file(filename) do
    case File.read(filename) do
      {:ok, body} -> process_body(body)
      {:error, reason} -> reason
    end
  end

  def process_body(body) do
    body
    |> String.split("\n\n")
    |> Enum.map(&Solution.process_elf/1)
    |> Enum.sort(:desc)
    |> Enum.take(3)
    |> Enum.sum()
  end

  def process_elf(elf) do
    elf
    |> String.split("\n")
    |> Enum.map(&Integer.parse/1)
    |> Enum.map(fn value -> elem(value, 0) end)
    |> Enum.sum()
  end
end

Solution.process_file("in.txt")
|> IO.puts()
