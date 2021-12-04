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
	aim := 0

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
			y += aim * amount
		case "up":
			aim -= amount
		case "down":
			aim += amount
		}
	}
	fmt.Println(x * y)
}
