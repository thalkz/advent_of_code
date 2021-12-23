package main

import "fmt"

type Player struct {
	id       int
	position int
	score    int
}

var tripleRoll map[int]int
var winningOne = 0
var winningTwo = 0

func main() {
	tripleRoll = map[int]int{
		3: 1,
		4: 3,
		5: 6,
		6: 7,
		7: 6,
		8: 3,
		9: 1,
	}

	one := Player{
		id:       1,
		position: 10,
	}
	two := Player{
		id:       2,
		position: 1,
	}
	next(one, two, 1)

	fmt.Println("Winning One", winningOne)
	fmt.Println("Winning Two", winningTwo)
}

func next(a, b Player, universes int) {
	for roll, count := range tripleRoll {
		newPosition := movePawn(a.position, roll)
		newScore := a.score + newPosition

		if newScore >= 21 {
			if a.id == 1 {
				winningOne += count * universes
			} else {
				winningTwo += count * universes
			}
			continue
		}

		newA := Player{
			id:       a.id,
			position: newPosition,
			score:    newScore,
		}

		next(b, newA, universes*count)
	}
}

func movePawn(start, moves int) int {
	position := start + moves
	position = position % 10
	if position == 0 {
		position = 10
	}
	return position
}
