{_, file} = File.read("in.txt")

f = fn
  x ->
    x
    |> Enum.map(fn value -> Integer.parse(value) end)
    |> Enum.map(fn value -> elem(value, 0) end)
    |> Enum.sum()
end

file
|> String.split("\n\n")
|> Enum.map(fn elf -> String.split(elf, "\n") end)
|> Enum.map(f)
|> Enum.sort(:desc)
|> Enum.take(3)
|> Enum.sum()
|> IO.inspect()
