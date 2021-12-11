package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type Heightmap [][]int

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")

	heightmap := make(Heightmap, len(lines))
	for y := range heightmap {
		heightmap[y] = make([]int, len(lines[y]))
		characters := strings.Split(lines[y], "")
		for x, char := range characters {
			height, _ := strconv.Atoi(char)
			heightmap[y][x] = height
		}
	}
	fmt.Printf("%v\n", heightmap)

	basinSizes := []int{}

	for y := range heightmap {
		for x := range heightmap[y] {
			if heightmap.isLowPoint(x, y) {
				basinSizes = append(basinSizes, heightmap.getBasinSize(x, y))
			}
		}
	}

	sort.Ints(basinSizes)
	fmt.Printf("%v\n", basinSizes)
	topSizes := basinSizes[len(basinSizes)-3:]
	fmt.Printf("%v\n", topSizes)

	total := 1
	for _, size := range topSizes {
		total *= size
	}
	fmt.Printf("%v\n", total)
}

func (heightmap Heightmap) String() string {
	var buffer strings.Builder
	for y := range heightmap {
		for x := range heightmap[y] {
			buffer.WriteString(fmt.Sprintf("%v", heightmap[y][x]))
		}
		buffer.WriteString("\n")
	}
	return buffer.String()
}

func (heightmap Heightmap) isLowPoint(x, y int) bool {
	point := heightmap[y][x]
	if (y == 0 || point < heightmap[y-1][x]) && (y == len(heightmap)-1 || point < heightmap[y+1][x]) && (x == 0 || point < heightmap[y][x-1]) && (x == len(heightmap[y])-1 || point < heightmap[y][x+1]) {
		return true
	} else {
		return false
	}
}

func (heightmap Heightmap) maxX() int {
	return len(heightmap[0]) - 1
}

func (heightmap Heightmap) maxY() int {
	return len(heightmap) - 1
}

func (heightmap Heightmap) getBasinSize(x, y int) int {
	basinSize := checkLocation(&heightmap, x, y, 0)
	fmt.Printf("%v\n", heightmap)
	return basinSize
}

func checkLocation(heightmap *Heightmap, x int, y int, total int) int {
	(*heightmap)[y][x] = 9
	if (x+1) <= heightmap.maxX() && (*heightmap)[y][x+1] < 9 {
		total = checkLocation(heightmap, x+1, y, total)
	}
	if (x-1) >= 0 && (*heightmap)[y][x-1] < 9 {
		total = checkLocation(heightmap, x-1, y, total)
	}
	if (y+1) <= heightmap.maxY() && (*heightmap)[y+1][x] < 9 {
		total = checkLocation(heightmap, x, y+1, total)
	}
	if (y-1) >= 0 && (*heightmap)[y-1][x] < 9 {
		total = checkLocation(heightmap, x, y-1, total)
	}
	return total + 1
}
