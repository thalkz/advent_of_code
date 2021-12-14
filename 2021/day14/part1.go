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

	polymer := parts[0]
	instructionLines := strings.Split(parts[1], "\n")

	instructions := map[string]string{}

	for i := range instructionLines {
		tokens := strings.Split(instructionLines[i], " -> ")
		instructions[tokens[0]] = tokens[1]
	}

	fmt.Printf("Instructions : %v\n", instructions)
	fmt.Printf("Polymer: %v\n", polymer)

	for step := 0; step < 10; step++ {
		polymer = grow(polymer, instructions)
		// fmt.Printf("Polymer: %v\n", polymer)
	}

	counter := countElements(polymer)
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

func grow(polymer string, instructions map[string]string) string {
	var buffer strings.Builder
	for i := 0; i < len(polymer)-1; i++ {
		buffer.WriteByte(polymer[i])
		buffer.WriteString(instructions[string(polymer[i:i+2])])
	}
	buffer.WriteByte(polymer[len(polymer)-1])
	return buffer.String()
}

func countElements(polymer string) map[string]int {
	counter := map[string]int{}
	for i := 0; i < len(polymer); i++ {
		counter[string(polymer[i])]++
	}
	return counter
}
