package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type Cube struct {
	minX, maxX, minY, maxY, minZ, maxZ int
	active                             bool
}

func main() {
	fileBytes, _ := os.ReadFile("input.txt")
	lines := strings.Split(string(fileBytes), "\n")

	instructions := make([]Cube, len(lines))
	xSplits := make([]int, 0)
	ySplits := make([]int, 0)
	zSplits := make([]int, 0)

	for i, line := range lines {
		parts := strings.Split(line, " ")
		coordParts := strings.Split(parts[1], ",")
		minX, maxX := parseCoord(coordParts[0])
		xSplits = append(xSplits, minX, maxX)
		minY, maxY := parseCoord(coordParts[1])
		ySplits = append(ySplits, minY, maxY)
		minZ, maxZ := parseCoord(coordParts[2])
		zSplits = append(zSplits, minZ, maxZ)
		instructions[i] = Cube{
			minX:   minX,
			maxX:   maxX,
			minY:   minY,
			maxY:   maxY,
			minZ:   minZ,
			maxZ:   maxZ,
			active: parts[0] == "on",
		}
	}

	sort.Ints(xSplits)
	sort.Ints(ySplits)
	sort.Ints(zSplits)

	activeCells := make([][][]bool, len(zSplits)-1)
	for z := range activeCells {
		activeCells[z] = make([][]bool, len(ySplits)-1)
		for y := range activeCells[z] {
			activeCells[z][y] = make([]bool, len(xSplits)-1)
		}
	}
	fmt.Println("Active cell count:", len(activeCells))

	for _, instr := range instructions {
		for z := 0; z < len(zSplits)-1; z++ {
			if instr.minZ > zSplits[z] || instr.maxZ < zSplits[z] {
				continue
			}
			for y := 0; y < len(ySplits)-1; y++ {
				if instr.minY > ySplits[y] || instr.maxY < ySplits[y] {
					continue
				}
				for x := 0; x < len(xSplits)-1; x++ {
					if instr.minX > xSplits[x] || instr.maxX < xSplits[x] {
						continue
					}
					cell := Cube{
						minX: xSplits[x],
						maxX: xSplits[x+1],
						minY: ySplits[y],
						maxY: ySplits[y+1],
						minZ: zSplits[z],
						maxZ: zSplits[z+1],
					}
					intersection := intersect(cell, instr)
					if intersection != nil {
						activeCells[z][y][x] = instr.active
					}
				}
			}
		}
	}

	total := 0
	for z := range activeCells {
		for y := range activeCells[z] {
			for x := range activeCells[z][y] {
				if activeCells[z][y][x] {
					total += (xSplits[x+1] - xSplits[x]) * (ySplits[y+1] - ySplits[y]) * (zSplits[z+1] - zSplits[z])
				}
			}
		}
	}
	fmt.Println("Total", total)
}

// Expexted result 2758514936282235

func (c *Cube) volume() uint64 {
	return uint64((c.maxX - c.minX) * (c.maxY - c.minY) * (c.maxZ - c.minZ))
}

func intersect(a, b Cube) *Cube {
	minX := max(a.minX, b.minX)
	maxX := min(a.maxX, b.maxX)
	minY := max(a.minY, b.minY)
	maxY := min(a.maxY, b.maxY)
	minZ := max(a.minZ, b.minZ)
	maxZ := min(a.maxZ, b.maxZ)
	if minX >= maxX || minY >= maxY || minZ >= maxZ {
		return nil
	} else {
		return &Cube{
			minX: minX,
			maxX: maxX,
			minY: minY,
			maxY: maxY,
			minZ: minZ,
			maxZ: maxZ,
		}
	}
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func parseCoord(input string) (int, int) {
	parts := strings.Split(input, "=")
	values := strings.Split(parts[1], "..")
	start, _ := strconv.Atoi(values[0])
	end, _ := strconv.Atoi(values[1])
	return start, (end + 1)
}
