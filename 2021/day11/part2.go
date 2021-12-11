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

	board := make(Board, 10)
	for y := 0; y < 10; y++ {
		board[y] = make([]int, 10)
		values := strings.Split(lines[y], "")
		for x := 0; x < 10; x++ {
			board[y][x], _ = strconv.Atoi(values[x])
		}
	}

	fmt.Printf("%v\n", board)

	for i := 1; i <= 500; i++ {
		allFlashed := board.nextStep()
		if allFlashed {
			fmt.Printf("All flashed at step %v:\n%v\n", i, board)
			return
		}
	}
}

/// Returns the number of flashes this step
func (b *Board) nextStep() bool {
	for y := 0; y < 10; y++ {
		for x := 0; x < 10; x++ {
			(*b)[y][x]++
		}
	}

	flash := 0
	changed := true

	for changed {
		changed = false
		for y := 0; y < 10; y++ {
			for x := 0; x < 10; x++ {
				if (*b)[y][x] > 9 {
					(*b)[y][x] = 0
					flash++
					changed = true
					b.increment(x+1, y+1)
					b.increment(x+1, y)
					b.increment(x+1, y-1)
					b.increment(x, y+1)
					b.increment(x, y-1)
					b.increment(x-1, y+1)
					b.increment(x-1, y)
					b.increment(x-1, y-1)
				}
			}
		}
	}

	return flash == 100
}

func (b *Board) at(x, y int) int {
	if x < 10 && x >= 0 && y < 10 && y >= 0 {
		return (*b)[y][x]
	}
	return 0
}

func (b *Board) increment(x, y int) bool {
	if b.at(x, y) != 0 {
		(*b)[y][x]++
		return true
	}
	return false
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
