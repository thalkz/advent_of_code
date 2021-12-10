package main

import (
	"fmt"
	"os"
	"strconv"
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

// Strategy intersection
// Known : 1 4 7 8
// len(6 n 1) = 1 BUT len(9 n 1) = 2 AND len(0 n 1) = 2
// Known : 1 4 6 7 8
// len(3 n 1) = 2 BUT len(2 n 1) = 1 AND len(5 n 1) = 1
// Known : 1 3 4 6 7 8
// len(4 n 9) = 4 BUT len(9 n 0) = 3
// len(4 n 2) = 2 BUT len(4 n 5) = 3
// Known : all

var segments = [7]string{
	"a", "b", "c", "d", "e", "f", "g",
}

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
		half := strings.Split(line, " | ")
		available := strings.Split(half[0], " ")
		outputTokens := strings.Split(half[1], " ")

		solution := map[int]string{
			0: "",
			1: "",
			2: "",
			3: "",
			4: "",
			5: "",
			6: "",
			7: "",
			8: "",
			9: "",
		}

		for _, token := range available {
			if len(token) == 2 {
				solution[1] = token
			} else if len(token) == 3 {
				solution[7] = token
			} else if len(token) == 4 {
				solution[4] = token
			} else if len(token) == 7 {
				solution[8] = token
			}
		}

		for _, token := range available {
			if len(token) == 5 {
				if intersect(token, solution[1]) == 2 {
					solution[3] = token
				} else if intersect(token, solution[4]) == 2 {
					solution[2] = token
				} else if intersect(token, solution[4]) == 3 {
					solution[5] = token
				}
			} else if len(token) == 6 {
				if intersect(token, solution[1]) == 1 {
					solution[6] = token
				} else if intersect(token, solution[4]) == 4 {
					solution[9] = token
				} else if intersect(token, solution[4]) == 3 {
					solution[0] = token
				}
			}
		}
		fmt.Printf("solution=%v\n", solution)

		outputStr := ""
		for _, token := range outputTokens {
			outputStr += strconv.Itoa(findNumber(token, solution))
		}

		output, _ := strconv.Atoi(outputStr)
		fmt.Printf("output=%v\n", outputStr)
		total += output
	}
	fmt.Printf("total=%v\n", total)
}

func findNumber(token string, solutions map[int]string) int {
	for i, value := range solutions {
		if isSame(token, value) {
			return i
		}
	}
	return -1
}

func intersect(a, b string) int {
	result := 0
	for _, segment := range segments {
		if strings.Contains(a, segment) && strings.Contains(b, segment) {
			result++
		}
	}
	return result
}

func isSame(a, b string) bool {
	return len(a) == len(b) && intersect(a, b) == len(a)
}
