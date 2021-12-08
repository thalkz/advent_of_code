package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Point struct {
	x, y int
}

type Board [][]int

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	fileString := string(fileBytes)
	lines := strings.Split(fileString, "\n")

	startPoints := make([]Point, len(lines))
	endPoints := make([]Point, len(lines))

	maxX := 0
	maxY := 0

	for i, line := range lines {
		points := strings.Split(line, " -> ")
		startPoints[i] = parsePoint(points[0])
		endPoints[i] = parsePoint(points[1])
		fmt.Printf("start %v, end %v\n", startPoints[i], endPoints[i])

		if maxX < startPoints[i].x {
			maxX = startPoints[i].x
		}
		if maxX < endPoints[i].x {
			maxX = endPoints[i].x
		}
		if maxY < startPoints[i].y {
			maxY = startPoints[i].y
		}
		if maxY < endPoints[i].y {
			maxY = endPoints[i].y
		}
	}

	board := make(Board, maxY+1)
	for i := range board {
		board[i] = make([]int, maxX+1)
	}

	for i := range startPoints {
		startPoint := startPoints[i]
		endPoint := endPoints[i]
		// fmt.Printf("%v", startPoint)
		if startPoint.x == endPoint.x {
			min, max := getMinMax(startPoint.y, endPoint.y)
			for k := min; k <= max; k++ {
				board[k][startPoint.x]++
			}
		} else if startPoint.y == endPoint.y {
			min, max := getMinMax(startPoint.x, endPoint.x)
			for k := min; k <= max; k++ {
				board[startPoint.y][k]++
			}
		} else if startPoint.y < endPoint.y {
			for k := 0; k <= endPoint.y-startPoint.y; k++ {
				if startPoint.x > endPoint.x {
					board[startPoint.y+k][startPoint.x-k]++
				} else {
					board[startPoint.y+k][startPoint.x+k]++
				}
			}
		} else if startPoint.y > endPoint.y {
			for k := 0; k <= startPoint.y-endPoint.y; k++ {
				if startPoint.x > endPoint.x {
					board[startPoint.y-k][startPoint.x-k]++
				} else {
					board[startPoint.y-k][startPoint.x+k]++
				}
			}
		}
	}

	// fmt.Printf("\n%v", board)
	fmt.Printf("result: %v\n", board.countCrossedLines())
}

func getMinMax(a int, b int) (min int, max int) {
	if a < b {
		return a, b
	} else {
		return b, a
	}
}

func (board Board) String() string {
	result := ""
	for i := range board {
		result += fmt.Sprintf("%v\n", board[i])
	}
	return result
}

func (board Board) countCrossedLines() int {
	result := 0
	for y := range board {
		for x := range board[y] {
			if board[y][x] > 1 {
				result++
			}
		}
	}
	return result
}

func parsePoint(input string) Point {
	values := strings.Split(input, ",")
	x, _ := strconv.Atoi(values[0])
	y, _ := strconv.Atoi(values[1])
	return Point{
		x: x,
		y: y,
	}
}
