package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Board [][]int

func main() {
	bigBoard := generateBoard()
	minDistance := dijkstra(bigBoard)
	fmt.Printf("Shortest path distance: %v\n", minDistance)
}

func generateBoard() Board {
	fileBytes, _ := os.ReadFile("input.txt")
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")
	board := make(Board, 100)
	for y := range lines {
		board[y] = make([]int, 100)
		locations := strings.Split(lines[y], "")
		for x := range lines[y] {
			value, _ := strconv.Atoi(locations[x])
			board[y][x] = value
		}
	}

	bigBoard := make(Board, 500)
	for y := range bigBoard {
		bigBoard[y] = make([]int, 500)
		for x := range bigBoard[y] {
			base := board[y%100][x%100]
			base += (y/100 + x/100)
			bigBoard[y][x] = (base-1)%9 + 1
		}
	}
	return bigBoard
}

func dijkstra(board Board) int {
	unvisited := map[int]bool{}
	distances := map[int]int{}

	for i := 0; i < 250000; i++ {
		unvisited[i] = true
	}
	distances[0] = 0
	unvisited[0] = true

	for {
		closest := getClosest(distances)

		if closest == 249999 {
			return distances[closest]
		}

		for _, neighbor := range getNeighbors(board, closest) {
			if unvisited[neighbor] {
				alt := distances[closest] + board.Get(neighbor)
				if distances[neighbor] == 0 || alt < distances[neighbor] {
					distances[neighbor] = alt
				}
			}
		}
		delete(unvisited, closest)
		delete(distances, closest)
	}
}

func getNeighbors(b Board, index int) []int {
	x := index % 500
	y := index / 500
	result := []int{}
	if x > 0 {
		result = append(result, index-1)
	}
	if x < 499 {
		result = append(result, index+1)
	}
	if y > 0 {
		result = append(result, index-500)
	}
	if y < 249999 {
		result = append(result, index+500)
	}
	return result
}

func getClosest(distances map[int]int) int {
	minDistance := 1 << 32
	minIndex := -1
	for i := range distances {
		if distances[i] < minDistance {
			minIndex = i
			minDistance = distances[i]
		}
	}
	return minIndex
}

func (b Board) Get(index int) int {
	return b[index/500][index%500]
}
