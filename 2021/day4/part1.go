package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Board = [][]int

func main() {
	fileBytes, err := os.ReadFile("small.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	fileString := string(fileBytes)
	lines := strings.Split(fileString, "\n")

	winningNumbersString := strings.Split(lines[0], ",")
	winningNumbers := []int{}

	for _, value := range winningNumbersString {
		parsed, err := strconv.Atoi(value)
		if err != nil {
			panic(err)
		}
		winningNumbers = append(winningNumbers, parsed)
	}

	boards := []Board{}

	for i := 0; (i*6 + 1) < len(lines); i++ {
		boards = append(boards, parseBoard(lines, i*6+1))
	}

	fmt.Println(winningNumbers)
	fmt.Println(boards)
}

func parseBoard(lines []string, i int) Board {
	board := make([][]int, 5)
	for i := range board {
		board[i] = make([]int, 5)
	}

	for y := 0; y < 5; y++ {
		row := []int{}
		rowString := strings.Split(lines[i+y+1], " ")

		for _, value := range rowString {

			if len(value) > 0 {
				valueInt, err := strconv.Atoi(value)
				if err != nil {
					panic(err)
				}
				row = append(row, valueInt)
			}
		}

		for x := 0; x < 5; x++ {
			board[y][x] = row[x]
		}
	}
	return board
}
