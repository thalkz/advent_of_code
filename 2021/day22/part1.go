package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	fileBytes, _ := os.ReadFile("small.txt")
	lines := strings.Split(string(fileBytes), "\n")

	cube := [101][101][101]bool{}

	for _, line := range lines {
		parts := strings.Split(line, " ")
		activate := parts[0]
		coordParts := strings.Split(parts[1], ",")
		start := make([]int, 3)
		end := make([]int, 3)
		start[0], end[0] = parseCoord(coordParts[0])
		start[1], end[1] = parseCoord(coordParts[1])
		start[2], end[2] = parseCoord(coordParts[2])
		fmt.Println(activate, start, end)

		for x := start[0]; x <= end[0]; x++ {
			if x < -50 || x > 50 {
				continue
			}
			for y := start[1]; y <= end[1]; y++ {
				if y < -50 || y > 50 {
					continue
				}
				for z := start[2]; z <= end[2]; z++ {
					if z < -50 || z > 50 {
						continue
					}
					cube[z+50][y+50][x+50] = (activate == "on")
				}
			}
		}
	}

	sum := 0
	for x := 0; x < 101; x++ {
		for y := 0; y < 101; y++ {
			for z := 0; z < 101; z++ {
				if cube[z][y][x] {
					sum++
				}
			}
		}
	}
	fmt.Println("Sum:", sum)
}

func parseCoord(input string) (int, int) {

	parts := strings.Split(input, "=")
	values := strings.Split(parts[1], "..")
	start, _ := strconv.Atoi(values[0])
	end, _ := strconv.Atoi(values[1])
	return start, end
}
