package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	positionsString := strings.Split(inputString, ",")
	positions := make([]int, len(positionsString))
	for i, position := range positionsString {
		positions[i], _ = strconv.Atoi(position)
	}
	max := max(positions)
	fmt.Println(positions)

	results := make([]int, max)

	for target := 0; target < max; target++ {
		results[target] = computeTotalFuel(positions, target)
		fmt.Printf("target=%v total=%v\n", target, results[target])
	}

	fmt.Printf("min=%v\n", min(results))
}

func computeTotalFuel(positions []int, target int) int {
	result := 0
	for _, position := range positions {
		n := abs(position - target)
		result += n * (n + 1) / 2
	}
	return result
}

func abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}

func max(arr []int) int {
	result := arr[0]
	for _, value := range arr {
		if value > result {
			result = value
		}
	}
	return result
}

func min(arr []int) int {
	result := arr[0]
	for _, value := range arr {
		if value < result {
			result = value
		}
	}
	return result
}
