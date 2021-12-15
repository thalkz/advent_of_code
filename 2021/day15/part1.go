package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Board [][]int
type Index int

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
	fmt.Printf("%v\n", board)

	dijkstra(board)
}

func dijkstra(board Board) {
	unvisited := map[Index]bool{}
	previous := map[Index]Index{}
	distances := map[Index]int{}
	for i := 0; i < board.Size(); i++ {
		unvisited[Index(i)] = true
		distances[Index(i)] = 1 << 32
	}

	distances[Index(0)] = 0

	for i := 0; i < board.Size(); i++ {
		u := closest(distances, unvisited)

		if u == Index(board.Size()-1) {
			fmt.Printf("distances: %v\n", distances)
			fmt.Printf("unvisited: %v\n", unvisited)
			fmt.Printf("previous: %v\n", previous)
			fmt.Printf("Shortest path distance: %v\n", distances[u])
			return
		}

		// fmt.Printf("closest : %v\n", u)

		unvisited[u] = false

		neighbors := getNeighbors(board, u)
		for _, v := range neighbors {
			if board.Contains(v) && unvisited[v] {
				alt := distances[u] + board.Get(v)
				if alt < distances[v] {
					distances[v] = alt
					previous[v] = u
				}
			}
		}
	}
}

func getNeighbors(b Board, index Index) []Index {
	x := int(index) % b.Width()
	y := int(index) / b.Width()
	// fmt.Printf("index: %v (%v,%v)\n", index, x, y)
	result := []Index{}
	if x > 0 {
		result = append(result, Index(int(index)-1))
	}
	if x < b.Width()-1 {
		result = append(result, Index(int(index)+1))
	}
	if y > 0 {
		result = append(result, Index(int(index)-b.Width()))
	}
	if y < len(b)-1 {
		result = append(result, Index(int(index)+b.Width()))
	}
	return result
}

func (u Index) String() string {
	return fmt.Sprintf("%v", int(u))
}

func closest(distances map[Index]int, unvisited map[Index]bool) Index {
	minIndex := Index(len(distances) - 1)
	for index, ok := range unvisited {
		if ok && distances[index] < distances[minIndex] {
			minIndex = index
		}
	}
	return minIndex
}

func (b Board) Get(index Index) int {
	return b[int(index)/b.Width()][int(index)%b.Width()]
}

func (b Board) Width() int {
	return len(b[0])
}

func (b Board) String() string {
	var buffer strings.Builder
	for y := range b {
		for x := range b[y] {
			buffer.WriteString(fmt.Sprintf("%v", b[y][x]))
		}
		buffer.WriteString("\n")
	}
	return buffer.String()
}

func (b Board) Size() int {
	return len(b) * len(b[0])
}

func (b Board) Contains(index Index) bool {
	return int(index) >= 0 && int(index) < b.Size()
}
