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
	inputString := string(fileBytes)
	parts := strings.Split(inputString, "\n\n")

	// Build polymer as a map of element couples
	polymerStr := parts[0]
	lastElement := string(polymerStr[len(polymerStr)-1])
	polymer := map[string]int{}
	for i := 0; i < len(polymerStr)-1; i++ {
		polymer[string(polymerStr[i:i+2])]++
	}
	fmt.Printf("Polymer: %v\n", polymer)

	instructionLines := strings.Split(parts[1], "\n")
	instructions := map[string]string{}
	for i := range instructionLines {
		tokens := strings.Split(instructionLines[i], " -> ")
		instructions[tokens[0]] = tokens[1]
	}

	fmt.Printf("Instructions : %v\n", instructions)
	fmt.Printf("Polymer: %v\n", polymer)

	for step := 0; step < 40; step++ {
		polymer = grow(polymer, instructions)
		fmt.Printf("Polymer: %v\n", polymer)
	}

	counter := countElements(polymer, lastElement)
	fmt.Printf("Counter : %v\n", counter)

	min := counter["N"]
	max := counter["N"]
	for _, value := range counter {
		if value < min {
			min = value
		}
		if value > max {
			max = value
		}
	}
	fmt.Printf("Result : %v\n", max-min)

}

func grow(polymer map[string]int, instructions map[string]string) map[string]int {
	result := map[string]int{}
	for pair, count := range polymer {
		result[string(pair[0])+instructions[pair]] += count
		result[instructions[pair]+string(pair[1])] += count
	}
	return result
}

func countElements(polymer map[string]int, lastElement string) map[string]int {
	counter := map[string]int{}
	for pair, count := range polymer {
		counter[string(pair[0])] += count
	}
	counter[lastElement]++
	return counter
}
