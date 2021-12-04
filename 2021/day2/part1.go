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
	fileString := string(fileBytes)
	lines := strings.Split(fileString, "\n")

	x := 0
	y := 0

	for _, line := range lines {
		tokens := strings.Split(line, " ")
		command := tokens[0]
		amount, err := strconv.Atoi(tokens[1])

		if err != nil {
			fmt.Printf("Failed to parse integer: %v", err)
			return
		}

		switch command {
		case "forward":
			x += amount
		case "up":
			y -= amount
		case "down":
			y += amount
		}
	}
	fmt.Println(x * y)
}
