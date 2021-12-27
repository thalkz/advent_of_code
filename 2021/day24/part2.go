package main

import (
	"fmt"
)

type Operation struct {
	function string
	a        string
	b        string
	value    int
}

var a [14]int
var b [14]int
var c [14]int

func main() {
	a = [14]int{10, 12, 10, 12, 11, -16, 10, -11, -13, 13, -8, -1, -4, -14}
	b = [14]int{12, 7, 8, 8, 15, 12, 8, 13, 3, 13, 3, 9, 4, 13}
	c = [14]int{1, 1, 1, 1, 1, 26, 1, 26, 26, 1, 26, 26, 26, 26}

	step := 0
	for {
		input := generateInput(step)
		// fmt.Println(input)
		z, _ := execute(&input)
		if z == 0 {
			fmt.Println(input)
			return
		}
		step++
	}
}

func generateInput(step int) [14]int {
	input := [14]int{}
	value := fmt.Sprint(11111111 + step)
	for i := range input {
		if i == 11 {
			input[i] = int(value[7] - '0')
		} else if i == 9 {
			input[i] = int(value[6] - '0')
		} else if i == 6 {
			input[i] = int(value[5] - '0')
		} else if i == 4 {
			input[i] = int(value[4] - '0')
		} else if i == 3 {
			input[i] = int(value[3] - '0')
		} else if i == 2 {
			input[i] = int(value[2] - '0')
		} else if i == 1 {
			input[i] = int(value[1] - '0')
		} else if i == 0 {
			input[i] = int(value[0] - '0')
		}
		if input[i] == 0 {
			input[i] = 1
		}
	}
	return input
}

func execute(input *[14]int) (int, bool) {
	z := 0
	for i := range input {

		if i == 5 || i == 7 || i == 8 || i == 10 || i == 12 || i == 13 {
			input[i] = z%26 + a[i]
			if input[i] > 9 || input[i] < 1 {
				return -1, false
			}
		}

		if c[i] == 1 {
			// Program A
			z = z*26 + input[i] + b[i]
		} else if input[i] == z%26+a[i] {
			// Program B-true
			z = z / 26
		} else {
			// Program B-false
			z = (z - z%26) + input[i] + b[i]
		}
	}
	return z, false
}
