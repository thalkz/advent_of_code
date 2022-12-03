# C > B > A > C
# Z > Y > X > Z

defmodule Solution do
  def compute_score(line) do
    [him, outcome] = String.split(line, " ")
    outcomeScore = compute_outcome_score(outcome)

    myMoveScore =
      compute_my_move(him, outcome)
      |> compute_move_score()

    outcomeScore + myMoveScore
  end

  def compute_outcome_score(outcome) do
    case outcome do
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
end

File.read!("in.txt")
|> String.split("\n")
|> Enum.map(&Solution.compute_score/1)
|> Enum.sum()
|> IO.puts()
