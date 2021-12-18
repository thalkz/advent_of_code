package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Area struct {
	minX, maxX, minY, maxY int
}

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	target := parseTarget(string(fileBytes))
	fmt.Printf("target: %v\n", target)

	count := 0

	for xVelocity := 0; xVelocity < 1000; xVelocity++ {
		for yVelocity := -150; yVelocity < 10000; yVelocity++ {
			success := simulateLaunch(&target, xVelocity, yVelocity, 2000)
			if success {
				count++
			}
		}
	}

	fmt.Printf("count: %v\n", count)
}

func simulateLaunch(target *Area, xVelocity int, yVelocity int, steps int) bool {
	x := 0
	y := 0
	highestPoint := 0
	for step := 0; step < steps; step++ {
		x, y, xVelocity, yVelocity = next(x, y, xVelocity, yVelocity)
		if y > highestPoint {
			highestPoint = y
		}
		if target.contains(x, y) {
			return true
		}
	}
	return false
}

func next(x, y, xVelocity, yVelocity int) (int, int, int, int) {
	x += xVelocity
	y += yVelocity
	if xVelocity > 0 {
		xVelocity--
	} else if xVelocity < 0 {
		xVelocity++
	}
	yVelocity--
	return x, y, xVelocity, yVelocity
}

func (a *Area) contains(x, y int) bool {
	return x >= a.minX && x <= a.maxX && y >= a.minY && y <= a.maxY
}

func parseTarget(input string) Area {
	tokens := strings.Split(input[15:], ", y=")
	xTokens := strings.Split(tokens[0], "..")
	yTokens := strings.Split(tokens[1], "..")
	minX, _ := strconv.Atoi(xTokens[0])
	maxX, _ := strconv.Atoi(xTokens[1])
	minY, _ := strconv.Atoi(yTokens[0])
	maxY, _ := strconv.Atoi(yTokens[1])
	return Area{
		minX: minX,
		maxX: maxX,
		minY: minY,
		maxY: maxY,
	}
}
