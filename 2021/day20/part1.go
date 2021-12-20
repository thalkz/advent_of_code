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

	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")

	algorithm := lines[0]

	imageLines := lines[2:]
	image := map[string]bool{}

	height := len(imageLines)
	width := len(imageLines[0])
	for y := range imageLines {
		for x := range imageLines[y] {
			image[fmt.Sprintf("%v,%v", x, y)] = (imageLines[y][x] == '#')
		}
	}

	fmt.Printf("algorithm: %v\n", algorithm)

	image = next(algorithm, image, width, height, 1)
	image = next(algorithm, image, width, height, 2)

	whiteCount := 0
	for _, white := range image {
		if white {
			whiteCount++
		}
	}

	fmt.Printf("whiteCount: %v\n", whiteCount)
}

func next(algorithm string, i map[string]bool, width int, height int, iteration int) map[string]bool {
	incr := 2
	newImage := map[string]bool{}
	minY := (-incr * iteration)
	maxY := height + (incr * iteration)
	minX := (-incr * iteration)
	maxX := width + (incr * iteration)

	for centerY := minY; centerY < maxY; centerY++ {
		for centerX := minX; centerX < maxX; centerX++ {
			algorithmIndex := 0
			bit := 0
			for y := (centerY - 1); y <= (centerY + 1); y++ {
				for x := (centerX - 1); x <= (centerX + 1); x++ {
					if isOutside(x, y, minX+incr, maxX-incr, minY+incr, maxY-incr) && (iteration%2 == 0) {
						algorithmIndex += 1 << (8 - bit)
					} else if i[toKey(x, y)] {
						algorithmIndex += 1 << (8 - bit)
					}
					bit++
				}
			}

			fmt.Printf("%v", string(algorithm[algorithmIndex]))
			newImage[toKey(centerX, centerY)] = algorithm[algorithmIndex] == '#'
		}
		fmt.Printf("\n")
	}
	return newImage
}

func isOutside(x, y, minX, maxX, minY, maxY int) bool {
	return x < minX || x >= maxX || y < minY || y >= maxY
}

func toCoords(key string) (int, int) {
	coords := strings.Split(key, ",")
	x, _ := strconv.Atoi(coords[0])
	y, _ := strconv.Atoi(coords[1])
	return x, y
}

func toKey(x int, y int) string {
	return fmt.Sprintf("%v,%v", x, y)
}
