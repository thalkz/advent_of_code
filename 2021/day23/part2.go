package main

import (
	"fmt"
)

type State struct {
	hallway []rune
	rooms   [4][]rune
	cost    int
}

var seen map[string]int
var queue []*State
var toIndex map[rune]int
var toRune map[int]rune
var toEntrance map[int]int
var toCost map[rune]int

func main() {
	initialState := getInitialState()
	seen = map[string]int{}
	toIndex = map[rune]int{
		'A': 0,
		'B': 1,
		'C': 2,
		'D': 3,
	}
	toRune = map[int]rune{
		0: 'A',
		1: 'B',
		2: 'C',
		3: 'D',
	}
	toEntrance = map[int]int{
		0: 2,
		1: 4,
		2: 6,
		3: 8,
	}
	toCost = map[rune]int{
		'A': 1,
		'B': 10,
		'C': 100,
		'D': 1000,
	}

	fmt.Println("Initial state:")
	initialState.display()
	queue = []*State{
		&initialState,
	}

	min := 10000000
	var minState *State

	for {
		if len(queue) == 0 {
			(*minState).display()
			return
		}
		state := queue[0]
		queue = queue[1:]
		nextStates := next(state)
		for _, ns := range nextStates {
			str := ns.String()
			if str == "...........AAAABBBBCCCCDDDD" {
				if ns.cost < min {
					min = ns.cost
					minState = ns
				}
			} else if seen[str] == 0 || ns.cost < seen[str] {
				seen[str] = ns.cost
				queue = append(queue, ns)
			}
		}
	}
}

func isClear(route []rune, a int, b int) bool {
	if a == b {
		return true
	}
	if a < b {
		for i := a + 1; i < b; i++ {
			if route[i] != '.' {
				return false
			}
		}
	} else {
		for i := b + 1; i < a; i++ {
			if route[i] != '.' {
				return false
			}
		}
	}
	return true
}

func next(state *State) []*State {
	// state.display()
	result := make([]*State, 0)

	// Hallway to room
	for _, hIndex := range []int{0, 1, 3, 5, 7, 9, 10} {
		char := rune(state.hallway[hIndex])
		if char != '.' {
			// Compute possible moves of char
			roomIndex := toIndex[char]
			roomClear := !hasStranger(state.rooms[roomIndex], char)
			if !roomClear {
				continue
			}
			target := findTarget(state.rooms[roomIndex])
			entrance := toEntrance[roomIndex]
			hallwayClear := isClear(state.hallway, hIndex, entrance)
			if !hallwayClear {
				continue
			}

			newState := state.copy()
			newState.hallway[hIndex] = '.'
			newState.rooms[roomIndex][target] = char
			newState.cost += (Abs(hIndex-entrance) + target + 1) * toCost[char]
			result = append(result, newState)
		}
	}

	// Room to hallway
	for roomIndex, room := range state.rooms {
		if hasStranger(room, toRune[roomIndex]) {
			for rIndex, char := range room {
				if char != '.' && isClear(room, -1, rIndex) {
					// Compute possible moves of char
					for _, hIndex := range []int{0, 1, 3, 5, 7, 9, 10} {
						entrance := toEntrance[roomIndex]
						hallwayClear := isClear(state.hallway, hIndex, entrance)
						targetClear := state.hallway[hIndex] == '.'
						if !hallwayClear || !targetClear {
							continue
						}
						newState := state.copy()
						newState.hallway[hIndex] = char
						newState.rooms[roomIndex][rIndex] = '.'
						newState.cost += (Abs(hIndex-entrance) + rIndex + 1) * toCost[char]
						result = append(result, newState)
					}
				}
			}
		}
	}
	return result
}

func Abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func findTarget(room []rune) int {
	for rIndex := range room {
		if room[rIndex] != '.' {
			return rIndex - 1
		}
	}
	return len(room) - 1
}

func hasStranger(room []rune, expected rune) bool {
	for _, char := range room {
		if char != '.' && char != expected {
			return true
		}
	}
	return false
}

func getInitialState() State {
	return State{
		hallway: []rune{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
		rooms: [4][]rune{
			{'D', 'D', 'D', 'C'},
			{'B', 'C', 'B', 'C'},
			{'D', 'B', 'A', 'A'},
			{'A', 'A', 'C', 'B'},
		},
		cost: 0,
	}
}

func (s State) String() string {
	return string(s.hallway) + string(s.rooms[0]) + string(s.rooms[1]) + string(s.rooms[2]) + string(s.rooms[3])
}

func (s State) display() {
	for i := 0; i < len(s.hallway); i++ {
		fmt.Printf(" %v ", string(s.hallway[i]))
	}
	fmt.Printf("\n")
	for i := 0; i < len(s.rooms[0]); i++ {
		fmt.Printf("       %v     %v     %v     %v\n", string(s.rooms[0][i]), string(s.rooms[1][i]), string(s.rooms[2][i]), string(s.rooms[3][i]))
	}
	fmt.Println(s.cost)
	fmt.Println("")
}

func (s *State) copy() *State {
	hallway := make([]rune, len(s.hallway))
	copy(hallway, s.hallway)
	rooms := [4][]rune{}
	for i := range rooms {
		rooms[i] = make([]rune, len(s.rooms[0]))
		copy(rooms[i], s.rooms[i])
	}
	return &State{
		hallway: hallway,
		rooms:   rooms,
		cost:    s.cost,
	}
}
