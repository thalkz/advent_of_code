# A - Rock
# B - Paper
# C - Sissors

defmodule Solution do
  def compute_score(line) do
    IO.puts(line)
    [him, outcome] = String.split(line, " ")
    outcomeScore = compute_outcome_score(outcome)
    IO.puts("outcomeScore: #{outcomeScore}")
    myMove = compute_my_move(him, outcome)
    myMoveScore = compute_move_score(myMove)
    IO.puts("My move: #{myMove} (score=#{myMoveScore})")
    outcomeScore + myMoveScore
  end

  def compute_outcome_score(me) do
    case me do
      "X" -> 0
      "Y" -> 3
      "Z" -> 6
    end
  end

  def compute_move_score(me) do
    case me do
      "A" -> 1
      "B" -> 2
      "C" -> 3
    end
  end

  def compute_my_move(him, outcome) when outcome == "X" do
    case him do
      "A" -> "C"
      "B" -> "A"
      "C" -> "B"
    end
  end

  def compute_my_move(him, outcome) when outcome == "Y" do
    him
  end

  def compute_my_move(him, outcome) when outcome == "Z" do
    case him do
      "A" -> "B"
      "B" -> "C"
      "C" -> "A"
    end
  end

  def compute_win_score(him, me) do
    cond do
      him == me -> 3
      him == "A" && me == "C" -> 0
      him == "C" && me == "A" -> 6
      him > me -> 0
      true -> 6
    end
  end

  def to_abc(me) do
    case me do
      "X" -> "A"
      "Y" -> "B"
      "Z" -> "C"
    end
  end
end

File.read!("in.txt")
|> String.split("\n")
|> Enum.map(&Solution.compute_score/1)
|> Enum.sum()
|> IO.puts()
