package main

import (
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

type Pair struct {
	value int
	left  *Pair
	right *Pair
}

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}

	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")
	maxMagnitude := 0
	var pair *Pair

	for i := 0; i < len(lines); i++ {
		if pair == nil {
			pair = parse(lines[i])
		} else {
			pair = add(pair, parse(lines[i]))
		}
	}

	fmt.Printf("total: %v\n", pair.magnitude())

	for i := 0; i < len(lines); i++ {
		for j := 0; j < len(lines); j++ {
			if i != j {

				magnitude := add(parse(lines[i]), parse(lines[j])).magnitude()
				// fmt.Printf("magnitude: %v\n", magnitude)
				if magnitude > maxMagnitude {
					maxMagnitude = magnitude
				}
			}
		}
	}
	fmt.Printf("max: %v\n", maxMagnitude)
}

func parse(line string) *Pair {
	stack := [](*Pair){}
	level := 0
	for _, char := range line {
		if char == ']' {
			level--
			pair := Pair{
				right: stack[len(stack)-1],
				left:  stack[len(stack)-2],
			}
			stack = stack[:len(stack)-2]
			stack = append(stack, &pair)
		} else if char == '[' {
			level++
		} else if char != ',' {
			value, _ := strconv.Atoi(string(char))
			stack = append(stack, &Pair{value: value})
		}
	}
	return stack[0]
}

func add(a *Pair, b *Pair) *Pair {
	pair := Pair{
		left:  a,
		right: b,
	}
	pair.reduce()
	return &pair
}

func (p *Pair) magnitude() int {
	if p.isValue() {
		return p.value
	} else {
		return p.left.magnitude()*3 + p.right.magnitude()*2
	}
}

func (p *Pair) reduce() {
	// fmt.Printf("To reduce: %v\n", p)
	exploded := true
	splitted := true
	for exploded || splitted {
		exploded, _, _ = p.explode(0)
		if exploded {
			// fmt.Printf("after explode: %v\n", p)
		} else {
			splitted = p.split()
			if splitted {
				// fmt.Printf("after split: %v\n", p)
			}
		}
	}
}

func (p *Pair) explode(level int) (bool, int, int) {
	if p.isValue() {
		return false, 0, 0
	} else if level >= 4 && p.left.isValue() && p.right.isValue() {
		addLeft := p.left.value
		addRight := p.right.value
		p.left = nil
		p.right = nil
		return true, addLeft, addRight
	} else {
		exploded, addLeft, addRight := p.left.explode(level + 1)
		if exploded {
			if addRight > 0 {
				rightPair := p.right
				for !rightPair.isValue() {
					rightPair = rightPair.left
				}
				rightPair.value += addRight
			}
			return exploded, addLeft, 0
		}
		exploded, addLeft, addRight = p.right.explode(level + 1)
		if exploded {
			if addLeft > 0 {
				leftPair := p.left
				for !leftPair.isValue() {
					leftPair = leftPair.right
				}
				leftPair.value += addLeft
			}
			return exploded, 0, addRight
		}
		return false, 0, 0
	}
}

func (p *Pair) split() bool {
	if p.value >= 10 {
		p.left = &Pair{value: int(math.Floor(float64(p.value) / 2.0))}
		p.right = &Pair{value: int(math.Ceil(float64(p.value) / 2.0))}
		p.value = 0
		return true
	} else if p.isValue() {
		return false
	} else if p.left.split() {
		return true
	} else if p.right.split() {
		return true
	}
	return false
}

func (p *Pair) isValue() bool {
	return p.left == nil
}

func (p Pair) String() string {
	if p.isValue() {
		return fmt.Sprintf("%v", p.value)
	} else {
		return fmt.Sprintf("[%v,%v]", p.left, p.right)
	}
}
