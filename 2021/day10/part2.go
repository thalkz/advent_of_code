package main

import (
	"fmt"
	"os"
	"sort"
	"strings"
)

type Heightmap [][]int

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")

	total := []int{}

	for _, line := range lines {
		score := computeScore(line)
		if score > 0 {
			total = append(total, score)
		}
	}

	sort.Ints(total)

	fmt.Printf("%v\n", total)
	fmt.Printf("%v\n", len(total)/2)
	fmt.Printf("%v\n", total[len(total)/2])
}

func computeScore(line string) int {
	stack := []string{}
	for _, char := range strings.Split(line, "") {
		if isOpening(char) {
			stack = append(stack, char)
		} else if isMatch(stack[len(stack)-1], char) {
			stack = stack[:len(stack)-1]
		} else {
			return 0
		}
	}
	// Compute missing
	missing := make([]string, len(stack))
	for i := 0; i < len(stack); i++ {
		missing[i] = getMatch(stack[len(stack)-1-i])
	}

	fmt.Printf("%v (MISSING)\n", missing)

	return computeAutocompleteScore(missing)
}

func computeAutocompleteScore(missing []string) int {
	total := 0
	for _, char := range missing {
		total *= 5
		total += charScore(char)
	}
	return total
}

func isOpening(char string) bool {
	return char == "<" || char == "(" || char == "{" || char == "["
}

func isMatch(a, b string) bool {
	return (a == "<" && b == ">") || (a == "(" && b == ")") || (a == "{" && b == "}") || (a == "[" && b == "]")
}

func charScore(char string) int {
	switch char {
	case ")":
		return 1
	case "]":
		return 2
	case "}":
		return 3
	case ">":
		return 4
	}
	return 0
}

func getMatch(char string) string {
	switch char {
	case "(":
		return ")"
	case "[":
		return "]"
	case "{":
		return "}"
	case "<":
		return ">"
	}
	return ""
}
