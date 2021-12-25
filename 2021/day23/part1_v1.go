package main

import "fmt"

/*
Input
#############
#...........#
###D#B#D#A###
  #C#C#A#B#
  #########

####################################
#  0  1  2  3  4  5  6  7  8  9 10 #
####### 11 ## 13 ## 15 ## 17 #######
  	  # 12 ## 14 ## 16 ## 18 #
      ########################
*/

type Node struct {
	category rune
	links    []int
}

var maze []Node
var rooms map[rune][]int
var min int

func main() {
	maze = generateMaze()
	rooms = map[rune][]int{
		'A': {11, 12},
		'B': {13, 14},
		'C': {15, 16},
		'D': {17, 18},
	}
	initialState := generateInitialState()
	// fmt.Println("Maze:", maze)
	display(initialState, 0, 0)

	min = 1500000
	next(initialState, 0, 0)
	fmt.Println(min)
}

func display(state []rune, depth int, cost int) {
	fmt.Printf("##################################### depth: %v\n", depth)
	fmt.Print("# ")
	for i := 0; i <= 10; i++ {
		fmt.Printf(" %v ", string(state[i]))
	}
	fmt.Printf(" # cost: %v\n", cost)
	fmt.Printf("######## %v ### %v ### %v ### %v ########\n", string(state[11]), string(state[13]), string(state[15]), string(state[17]))
	fmt.Printf("######## %v ### %v ### %v ### %v ########\n", string(state[12]), string(state[14]), string(state[16]), string(state[18]))
	fmt.Printf("#####################################\n\n")
}

func isCompleted(state []rune) bool {
	for pod, room := range rooms {
		for _, index := range room {
			if state[index] != pod {
				return false
			}
		}
	}
	return true
}

func containsAnotherPod(state []rune, room []int, pod rune) bool {
	for _, index := range room {
		if state[index] != '.' && state[index] != pod {
			return true
		}
	}
	return false
}

func next(state []rune, totalCost int, depth int) {
	// display(state, depth, totalCost)
	if totalCost >= min {
		return
	}
	if isCompleted(state) {
		if totalCost < min {
			min = totalCost
			display(state, depth, totalCost)
		}
		return
	}

	for i, pod := range state {
		if pod != '.' {
			if i <= 10 {
				// Go to room
				for _, j := range rooms[pod] {
					if containsAnotherPod(state, rooms[pod], pod) {
						continue
					}
					moves, ok := canMoveTo(&state, i, j)
					if ok {
						newState := make([]rune, 19)
						copy(newState, state)
						newState[i] = '.'
						newState[j] = pod
						// fmt.Printf("%s: %v -> %v (room). %v\n", pod, i, j, newState)
						next(newState, totalCost+(moves*energyCost(pod)), depth+1)
					}
				}
			} else {
				// Go to hallway
				for _, j := range []int{0, 1, 3, 5, 7, 9, 10} {
					if maze[i].category == pod && !containsAnotherPod(state, rooms[pod], pod) {
						// If the pod is in the right room and does not block, stay
						continue
					}
					moves, ok := canMoveTo(&state, i, j)
					if ok {
						newState := make([]rune, 19)
						copy(newState, state)
						newState[i] = '.'
						newState[j] = pod
						// fmt.Printf("%s %v -> %v (hallway) %v\n", pod, i, j, newState)
						next(newState, totalCost+(moves*energyCost(pod)), depth+1)
					}
				}
			}
		}
	}
}

func energyCost(pod rune) int {
	switch pod {
	case 'A':
		return 1
	case 'B':
		return 10
	case 'C':
		return 100
	case 'D':
		return 1000
	}
	return 0
}

func canMoveTo(state *[]rune, start int, end int) (int, bool) {
	visited := make([]bool, 19)
	moves, ok := explore(state, visited, start, end, 1)
	return moves, ok
}

func explore(state *[]rune, visited []bool, start int, end int, distance int) (int, bool) {
	for _, index := range maze[start].links {
		if !visited[index] && (*state)[index] == '.' {
			visited[index] = true
			if index == end {
				return distance, true
			} else {
				distance, ok := explore(state, visited, index, end, distance+1)
				if ok {
					return distance, ok
				}
			}
		}
	}
	return 0, false
}

func generateInitialState() []rune {
	state := make([]rune, 19)
	for i := range state {
		state[i] = '.'
	}
	state[11] = 'D'
	state[12] = 'C'
	state[13] = 'B'
	state[14] = 'C'
	state[15] = 'D'
	state[16] = 'A'
	state[17] = 'A'
	state[18] = 'B'
	return state
}

func generateMaze() []Node {
	maze := make([]Node, 19)
	// Side rooms are marked with the amphipod that want to go in it
	maze[11].category = 'A'
	maze[12].category = 'A'
	maze[13].category = 'B'
	maze[14].category = 'B'
	maze[15].category = 'C'
	maze[16].category = 'C'
	maze[17].category = 'D'
	maze[18].category = 'D'

	for i := 0; i < 10; i++ {
		maze[i].links = append(maze[i].links, i+1)
	}
	for i := 1; i <= 10; i++ {
		maze[i].links = append(maze[i].links, i-1)
	}
	maze[2].links = append(maze[2].links, 11)
	maze[4].links = append(maze[4].links, 13)
	maze[6].links = append(maze[6].links, 15)
	maze[8].links = append(maze[8].links, 17)

	maze[11].links = []int{2, 12}
	maze[12].links = []int{11}
	maze[13].links = []int{4, 14}
	maze[14].links = []int{13}
	maze[15].links = []int{6, 16}
	maze[16].links = []int{15}
	maze[17].links = []int{8, 18}
	maze[18].links = []int{17}
	return maze
}
