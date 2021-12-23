package main

import "fmt"

func main() {
	isPlayerA := true
	scoreA := 0
	scoreB := 0
	movePawnA := createBoard(10)
	movePawnB := createBoard(1)
	roll := createDice()
	totalRolls := 0
	for scoreA < 1000 && scoreB < 1000 {
		moves := roll()
		totalRolls += 3
		if isPlayerA {
			scoreA += movePawnA(moves)
		} else {
			scoreB += movePawnB(moves)
		}
		isPlayerA = !isPlayerA
	}
	fmt.Println("Player a", scoreA)
	fmt.Println("Player b", scoreB)
	looserPoints := scoreA
	if scoreB < scoreA {
		looserPoints = scoreB
	}
	fmt.Println("Result:", looserPoints*totalRolls)
}

func createBoard(start int) func(moves int) int {
	position := start
	return func(moves int) int {
		position += moves
		position = position % 10
		if position == 0 {
			position = 10
		}
		return position
	}
}

func createDice() func() int {
	face := 1
	min := 1
	max := 100
	rolls := 3
	return func() int {
		total := 0
		fmt.Print("[")
		for i := 0; i < rolls; i++ {
			if face > max {
				face = min
			}
			fmt.Print(face, ",")
			total += face
			face++
		}
		fmt.Println("]", total)
		return total
	}
}
