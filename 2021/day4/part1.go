package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Board = [][]int

func main() {
	fileBytes, err := os.ReadFile("input.txt")
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

	for i := 0; i < len(winningNumbers); i++ {
		// 1. Update boards
		for _, board := range boards {
			for y := 0; y < 5; y++ {
				for x := 0; x < 5; x++ {
					if board[y][x] == winningNumbers[i] {
						board[y][x] = -1
					}
				}
			}
		}

		// 2. Check winner
		for _, board := range boards {
			for y := 0; y < 5; y++ {
				if board[y][0] == -1 && board[y][1] == -1 && board[y][2] == -1 && board[y][3] == -1 && board[y][4] == -1 {
					fmt.Printf("Winner: %v\n", board)
					computeResult(board, winningNumbers[i])
					return
				}
			}

			for x := 0; x < 5; x++ {
				if board[0][x] == -1 && board[1][x] == -1 && board[2][x] == -1 && board[3][x] == -1 && board[4][x] == -1 {
					fmt.Printf("Winner: %v\n", board)
					computeResult(board, winningNumbers[i])
					return
				}
			}
		}
	}
}

func computeResult(board Board, lastWinningNumer int) {
	result := 0
	for y := 0; y < 5; y++ {
		for x := 0; x < 5; x++ {
			if board[y][x] != -1 {
				result += board[y][x]
			}
		}
	}
	fmt.Printf("Result %v", result*lastWinningNumer)
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
