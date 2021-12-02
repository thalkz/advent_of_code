package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	fileBytes, e := os.ReadFile("input.txt")
	if e != nil {
		panic(e)
	}
	fileString := string(fileBytes)
	linesString := strings.Split(fileString, "\n")
	var averages []int
	var lines []int

	for i := 0; i < len(linesString); i++ {
		value, err := strconv.Atoi(linesString[i])
		lines = append(lines, value)
		if err != nil {
			panic(err)
		}
	}

	for i := 0; i < len(lines)-2; i++ {
		averages = append(averages, lines[i]+lines[i+1]+lines[i+2])
	}

	last := averages[0]
	result := 0

	for i := 0; i < len(averages); i++ {
		if averages[i] > last {
			result++
		}
		last = averages[i]
	}
	fmt.Println(result)
}
