package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	bytes, _ := os.ReadFile("input.txt")
	lines := strings.Split(string(bytes), "\n")
	board := newBoard(len(lines[0]), len(lines))
	for i := range lines {
		board[i] = []byte(lines[i])
	}
	printBoard(board)

	updatedRight := true
	updatedBottom := true
	step := 0
	for updatedRight || updatedBottom {
		step++
		board, updatedRight = nextRight(board, len(board[0]), len(board))
		board, updatedBottom = nextBottom(board, len(board[0]), len(board))
	}

	printBoard(board)
	fmt.Println(step)
}

func nextBottom(b [][]byte, width int, height int) ([][]byte, bool) {
	updated := false
	board := newBoard(width, height)
	for x := 0; x < width; x++ {
		for y := 0; y < height; y++ {
			if b[y][x] == 'v' {
				if b[(y+1)%height][x] == '.' {
					board[y][x] = '.'
					board[(y+1)%height][x] = 'v'
					updated = true
				} else {
					board[y][x] = 'v'
				}
			} else if b[y][x] == '>' {
				board[y][x] = b[y][x]
			}
		}
	}
	return board, updated
}

func nextRight(b [][]byte, width int, height int) ([][]byte, bool) {
	updated := false
	board := newBoard(width, height)
	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			if b[y][x] == '>' {
				if b[y][(x+1)%width] == '.' {
					board[y][x] = '.'
					board[y][(x+1)%width] = '>'
					updated = true
				} else {
					board[y][x] = '>'
				}
			} else if b[y][x] == 'v' {
				board[y][x] = b[y][x]
			}
		}
	}
	return board, updated
}

func newBoard(width int, height int) [][]byte {
	board := make([][]byte, height)
	for y := 0; y < height; y++ {
		board[y] = make([]byte, width)
		for x := 0; x < width; x++ {
			board[y][x] = '.'
		}
	}
	return board
}

func printBoard(board [][]byte) {
	fmt.Println("Board:")
	for i := range board {
		fmt.Println(string(board[i]))
	}
}
