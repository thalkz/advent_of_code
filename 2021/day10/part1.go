package main

import (
	"fmt"
	"os"
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

	total := 0

	for _, line := range lines {
		total += computeScore(line)
	}

	fmt.Printf("%v\n", total)
}

func computeScore(line string) int {
	fmt.Printf("%v (INPUT LINE)\n", line)

	stack := []string{}

	for _, char := range strings.Split(line, "") {
		if isOpening(char) {
			stack = append(stack, char)
		} else if isMatch(stack[len(stack)-1], char) {
			stack = stack[:len(stack)-1]
		} else {
			fmt.Printf("Illegal character found: %v\n", char)
			return charScore(char)
		}
		fmt.Printf("%v\n", stack)
	}

	return 0
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
		return 3
	case "]":
		return 57
	case "}":
		return 1197
	case ">":
		return 25137
	}
	return 0
}
