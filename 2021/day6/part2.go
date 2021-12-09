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
	initial := strings.Split(fileString, ",")

	population := make(map[int]int)

	for _, i := range initial {
		left, _ := strconv.Atoi(i)
		population[left]++
	}

	fmt.Println(population)

	for i := 0; i < 256; i++ {
		population = computeNextDay(population)
		fmt.Println(population)
	}

	fmt.Println(computeTotal(population))
}

func computeNextDay(population map[int]int) map[int]int {
	nextPopulation := make(map[int]int)

	for gen, amount := range population {
		if gen > 0 {
			nextPopulation[gen-1] += amount
		} else {
			nextPopulation[6] += amount
			nextPopulation[8] = amount
		}
	}
	return nextPopulation
}

func computeTotal(population map[int]int) int {
	total := 0
	for _, amount := range population {
		total += amount
	}
	return total
}
