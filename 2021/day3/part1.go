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
	length := len(lines[0])

	counter := make([]int, length)

	for _, line := range lines {
		bits := strings.Split(line, "")
		for i, bit := range bits {
			if bit == "1" {
				counter[i]++
			}
		}
	}

	espilon := 0
	gamma := 0

	for i := 0; i < length; i++ {
		gamma += 1 << i
	}

	for i := 0; i < length; i++ {
		if counter[length-i-1] > len(lines)/2 {
			espilon += 1 << i
			gamma -= 1 << i
		}
	}

	fmt.Println(counter)
	fmt.Println(espilon)
	fmt.Println(gamma)
	fmt.Println(gamma * espilon)
}
