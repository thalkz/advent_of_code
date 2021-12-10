package main

import (
	"fmt"
	"os"
	"strings"
)

/*
0 = abc efg (6)
1 =   c  f  (2)
2 = a cde g (5)
3 = a cd fg (5)
4 =  bcd f  (4)
5 =	ab d fg (5)
6 =	ab defg (6)
7 =	a c  f  (3)
8 =	abcdefg (7)
9 =	abcd fg (6)
*/

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")

	result := 0

	for _, line := range lines {
		half := strings.Split(line, " | ")
		available := strings.Split(half[0], " ")
		output := strings.Split(half[1], " ")

		for _, token := range output {
			if len(token) == 2 || len(token) == 3 || len(token) == 4 || len(token) == 7 {
				result++
			}
		}

		fmt.Printf("available=%v output=%v\n", available, output)
	}
	fmt.Printf("result=%v\n", result)
}
