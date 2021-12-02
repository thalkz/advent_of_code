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
		panic(err)
	}
	fileString := string(fileBytes)
	lines := strings.Split(fileString, "\n")
	last, err := strconv.Atoi(lines[0])
	if err != nil {
		panic(err)
	}

	result := 0

	for i := 0; i < len(lines); i++ {
		value, err := strconv.Atoi(lines[i])
		if err != nil {
			panic(err)
		}
		if value > last {
			result++
		}
		last = value
	}
	fmt.Println(result)
}
