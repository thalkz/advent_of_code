package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Point struct {
	x, y, z int
}

type Scanner struct {
	id     int
	points []Point
}

type Rotation struct {
	x, y, z int
}

func main() {
	scanners := parseScanners()
	// pointsMap := map[string]bool{}
	alinedScanners := map[int](*Scanner){}
	alinedScanners[0] = &scanners[0]
	scannerLocations := map[int](*Point){}
	scannerLocations[0] = &Point{}

	for len(alinedScanners) < len(scanners) {

		for i := 0; i < len(scanners); i++ {
			for j := 0; j < len(scanners); j++ {
				if alinedScanners[i] != nil && alinedScanners[j] == nil {
					match, offset, rotation := match(*alinedScanners[i], scanners[j])
					if match {
						rotated := rotate(scanners[j], rotation)
						translated := translate(rotated, *offset)
						alinedScanners[j] = &translated
						scannerLocations[j] = offset
						fmt.Printf("%v and %v matched (offset: %v, rotation: %v)\n", i, j, offset, rotation)
					}
				}
			}
		}
	}

	fmt.Printf("there are %v aligned scanners\n", len(alinedScanners))
	fmt.Printf("locations %v\n", scannerLocations)

	maxDistance := 0
	for i := 0; i < len(scannerLocations); i++ {
		for j := 0; j < len(scannerLocations); j++ {
			if i < j {
				distance := manhattan(scannerLocations[i], scannerLocations[j])
				if distance > maxDistance {
					maxDistance = distance
				}
			}
		}
	}

	fmt.Printf("Max distance is %v\n", maxDistance)
}

func manhattan(a, b *Point) int {
	x := b.x - a.x
	if x < 0 {
		x = -x
	}
	y := b.y - a.y
	if y < 0 {
		y = -y
	}
	z := b.z - a.z
	if z < 0 {
		z = -z
	}
	return x + y + z
}

func parseScanners() []Scanner {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		panic(err)
	}

	inputString := string(fileBytes)
	parts := strings.Split(inputString, "\n\n")

	scanners := make([]Scanner, len(parts))

	for k, part := range parts {
		lines := strings.Split(part, "\n")
		points := make([]Point, len(lines)-1)
		for i := 1; i < len(lines); i++ {
			coords := strings.Split(lines[i], ",")
			x, _ := strconv.Atoi(coords[0])
			y, _ := strconv.Atoi(coords[1])
			z, _ := strconv.Atoi(coords[2])
			points[i-1] = Point{x: x, y: y, z: z}
		}
		scanners[k] = Scanner{
			id:     k,
			points: points,
		}
	}
	return scanners
}

func match(a, b Scanner) (bool, *Point, string) {
	rotations := getRotations()
	for _, r := range rotations {
		rotatedB := rotate(b, r)
		for i := 0; i < len(a.points)-11; i++ {
			for j := 0; j < len(rotatedB.points)-11; j++ {
				offset := Point{
					x: a.points[i].x - rotatedB.points[j].x,
					y: a.points[i].y - rotatedB.points[j].y,
					z: a.points[i].z - rotatedB.points[j].z,
				}
				translatedB := translate(rotatedB, offset)
				match := countMatch(a, translatedB)
				if match >= 12 {
					return true, &offset, r
				}
			}
		}
	}
	return false, nil, ""
}

func countMatch(a, b Scanner) int {
	pointsMap := map[string]bool{}
	for _, p := range a.points {
		pointsMap[p.String()] = true
	}
	for _, p := range b.points {
		pointsMap[p.String()] = true
	}
	return len(a.points) + len(b.points) - len(pointsMap)
}

func rotate(s Scanner, rotation string) Scanner {
	dimensions := strings.Split(rotation, ",")
	points := make([]Point, len(s.points))
	for i, p := range s.points {
		points[i] = Point{
			x: p.get(dimensions[0]),
			y: p.get(dimensions[1]),
			z: p.get(dimensions[2]),
		}
	}
	return Scanner{
		id:     s.id,
		points: points,
	}
}

func (p *Point) get(dim string) int {
	switch dim {
	case "x":
		return p.x
	case "-x":
		return -p.x
	case "y":
		return p.y
	case "-y":
		return -p.y
	case "z":
		return p.z
	case "-z":
		return -p.z
	}
	return 0
}

func translate(s Scanner, translation Point) Scanner {
	points := make([]Point, len(s.points))
	for i, p := range s.points {
		points[i] = Point{
			x: p.x + translation.x,
			y: p.y + translation.y,
			z: p.z + translation.z,
		}
	}
	return Scanner{
		id:     s.id,
		points: points,
	}
}

func getRotations() []string {
	base := []string{
		"a,b,c",
		"a,-b,-c",
		"a,-c,b",
		"a,c,-b",
		"-a,-b,c",
		"-a,b,-c",
		"-a,c,b",
		"-a,-c,-b",
	}
	rotations := []string{}
	for _, r := range base {
		for _, m := range []string{"xyz", "yzx", "zxy"} {
			tmp := strings.ReplaceAll(r, "a", string(m[0]))
			tmp = strings.ReplaceAll(tmp, "b", string(m[1]))
			tmp = strings.ReplaceAll(tmp, "c", string(m[2]))
			rotations = append(rotations, tmp)
		}
	}
	return rotations
}

func (p *Point) String() string {
	return fmt.Sprintf("(%v,%v,%v)", p.x, p.y, p.z)
}

func (s *Scanner) String() string {
	return fmt.Sprintf("(id: %v, beacons: %v)", s.id, s.points)
}
