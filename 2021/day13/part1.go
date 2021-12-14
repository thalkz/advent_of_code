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

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	parts := strings.Split(inputString, "\n\n")

	inputStr := strings.Split(parts[0], "\n")
	folds := strings.Split(parts[1], "\n")

	points := make([]Point, len(inputStr))
	for i := range inputStr {
		coods := strings.Split(inputStr[i], ",")
		x, _ := strconv.Atoi(coods[0])
		y, _ := strconv.Atoi(coods[1])
		points[i] = Point{
			x: x,
			y: y,
		}
	}

	fmt.Printf("%v\n", points)
	// fmt.Printf("%v\n", folds)

	for i := range folds {
		tokens := strings.Split(folds[i], "=")
		value, _ := strconv.Atoi(tokens[1])
		if tokens[0] == "fold along y" {
			points = foldAlongY(points, value)
			fmt.Printf("%v\n", points)
		} else {
			points = foldAlongX(points, value)
			fmt.Printf("%v\n", points)
		}
		fmt.Printf("After fold %v : %v\n", i+1, len(points))
	}

	printDisplay(points)
}

func printDisplay(points []Point) {
	display := make([][]string, 30)
	for y := 0; y < 30; y++ {
		display[y] = make([]string, 80)
		for x := 0; x < 80; x++ {
			display[y][x] = "."
		}
	}

	for _, point := range points {
		display[point.y][point.x] = "#"
	}

	for y := 0; y < 30; y++ {
		fmt.Printf("%v\n", display[y])
	}
}

func appendIfAbsent(points []Point, toAdd Point) []Point {
	for i := range points {
		if points[i].x == toAdd.x && points[i].y == toAdd.y {
			return points
		}
	}
	return append(points, toAdd)
}

func foldAlongX(points []Point, foldX int) []Point {
	fmt.Printf("Fold along x=%v\n", foldX)
	result := []Point{}
	for _, point := range points {
		if point.x < foldX {
			result = appendIfAbsent(result, point)
		} else if point.x > foldX {
			result = appendIfAbsent(result, Point{
				x: 2*foldX - point.x,
				y: point.y,
			})
		}
	}
	return result
}

func foldAlongY(points []Point, foldY int) []Point {
	fmt.Printf("Fold along y=%v\n", foldY)
	result := []Point{}
	for _, point := range points {
		if point.y < foldY {
			result = appendIfAbsent(result, point)
		} else if point.y > foldY {
			result = appendIfAbsent(result, Point{
				x: point.x,
				y: 2*foldY - point.y,
			})
		}
	}
	return result
}
