package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	fileString := string(fileBytes)
	lines := strings.Split(fileString, "\n")

	oxygen := compute(lines, true)
	co2 := compute(lines, false)

	fmt.Printf("oxygen=%v\n", oxygen)
	fmt.Printf("co2=%v\n", co2)
	fmt.Printf("total=%v\n", co2*oxygen)
}

func compute(lines []string, mostCommon bool) int {
	length := len(lines[0])
	for row := 0; row < length; row++ {
		lines = filterLines(lines, row, mostCommon)
		if len(lines) == 1 {
			return toDecimal(lines[0])
		}
	}
	return -1
}

func toDecimal(line string) int {
	result := 0
	for i := 0; i < len(line); i++ {
		if line[len(line)-i-1] == '1' {
			result += 1 << i
		}
	}
	return result
}

func filterLines(lines []string, row int, mostCommon bool) []string {
	// Count amount of '1'
	counter := 0
	for _, line := range lines {
		if line[row] == '1' {
			counter++
		}
	}

	// Find out which bit we need to keep
	var keep byte
	if mostCommon {
		if float64(counter) >= float64(len(lines))/2.0 {
			keep = '1'
		} else {
			keep = '0'
		}
	} else {
		if float64(counter) < float64(len(lines))/2.0 {
			keep = '1'
		} else {
			keep = '0'
		}
	}

	// Only keep lines with the `keep` bit
	result := make([]string, 0)
	for _, line := range lines {
		if line[row] == keep {
			result = append(result, line)
		}
	}

	return result
}
