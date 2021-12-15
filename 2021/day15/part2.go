package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Board [][]int

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")
	board := make(Board, len(lines))
	for y := range lines {
		board[y] = make([]int, len(lines[y]))
		locations := strings.Split(lines[y], "")
		for x := range lines[y] {
			value, _ := strconv.Atoi(locations[x])
			board[y][x] = value
		}
	}

	bigBoard := make(Board, len(board)*5)
	for y := range bigBoard {
		bigBoard[y] = make([]int, len(board[0])*5)
		for x := range bigBoard[y] {
			base := board[y%len(board)][x%len(board[0])]
			base += (y/len(board) + x/len(board[0]))
			bigBoard[y][x] = (base-1)%9 + 1
		}
	}

	// fmt.Printf("%v\n", board)
	// fmt.Printf("%v\n", bigBoard)

	dijkstra(bigBoard)
}

func dijkstra(board Board) {
	visitedCount := 0
	unvisited := map[int]bool{}
	previous := map[int]int{}
	distances := map[int]int{}
	for i := 0; i < board.Size(); i++ {
		unvisited[i] = true
	}

	distances[0] = 0
	unvisited[0] = true

	for {
		u := closest(&distances, &unvisited)
		visitedCount++

		if u == board.Size()-1 {
			// fmt.Printf("distances: %v\n", distances)
			// fmt.Printf("unvisited: %v\n", unvisited)
			// fmt.Printf("previous: %v\n", previous)
			fmt.Printf("Shortest path distance: %v\n", distances[u])
			return
		}

		if visitedCount%100 == 0 {
			fmt.Printf("Visited nodes : %v / %v (distance: %v, u: %v)\n", visitedCount, board.Size(), distances[u], u)
		}

		delete(unvisited, u)

		neighbors := getNeighbors(&board, u)
		for _, v := range neighbors {
			if unvisited[v] {
				alt := distances[u] + board.Get(v)

				if distances[v] == 0 || alt < distances[v] {
					distances[v] = alt
					previous[v] = u
				}
			}
		}

		delete(distances, u)

		// fmt.Printf("distances: %v\n", distances)
		// fmt.Printf("unvisited length : %v\n", len(unvisited))
	}
}

func getNeighbors(b *Board, index int) []int {
	x := index % b.Width()
	y := index / b.Width()
	// fmt.Printf("index: %v (%v,%v)\n", index, x, y)
	result := []int{}
	if x > 0 {
		result = append(result, int(index)-1)
	}
	if x < b.Width()-1 {
		result = append(result, index+1)
	}
	if y > 0 {
		result = append(result, int(index)-b.Width())
	}
	if y < len(*b)-1 {
		result = append(result, int(index)+b.Width())
	}
	return result
}

func closest(distances *map[int]int, unvisited *map[int]bool) int {
	minDistance := 1 << 32
	minIndex := -1
	for i := range *distances {
		if (*unvisited)[i] && (*distances)[i] < minDistance {
			minIndex = i
			minDistance = (*distances)[i]
		}
	}
	return minIndex
}

func (b *Board) Get(index int) int {
	return (*b)[int(index)/b.Width()][int(index)%b.Width()]
}

func (b *Board) Width() int {
	return len((*b)[0])
}

func (b *Board) String() string {
	var buffer strings.Builder
	for y := range *b {
		for x := range (*b)[y] {
			buffer.WriteString(fmt.Sprintf("%v", (*b)[y][x]))
		}
		buffer.WriteString("\n")
	}
	return buffer.String()
}

func (b *Board) Size() int {
	return len(*b) * len((*b)[0])
}
