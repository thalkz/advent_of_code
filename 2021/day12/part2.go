package main

import (
	"errors"
	"fmt"
	"os"
	"strings"
)

type Cave struct {
	name  string
	big   bool
	links [](*Cave)
}

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	lines := strings.Split(inputString, "\n")

	// Construct caves graph
	caves := [](*Cave){}
	for _, line := range lines {
		names := strings.Split(line, "-")
		addIfAbsent(&caves, names[0])
		addIfAbsent(&caves, names[1])
		link(&caves, names[0], names[1])
	}
	fmt.Printf("Cave system:\n%v\n", caves)

	// Walk all paths
	startCave, err := find(&caves, "start")
	fmt.Printf("Starting cave : %v\n", startCave)

	total := visit(startCave, []string{}, true)

	fmt.Printf("%v\n", total)
}

func (c Cave) String() string {
	var buffer strings.Builder
	buffer.WriteString(fmt.Sprintf("(%p %v ", &c, c.name))
	for _, link := range c.links {
		buffer.WriteString(fmt.Sprintf("-%v", link.name))
	}
	buffer.WriteString(")")
	return buffer.String()
}

func visit(current *Cave, visited []string, extraTime bool) int {
	if current.name == "end" {
		fmt.Printf("Found the end !\n")
		return 1
	}
	if !current.big {
		visited = append(visited, current.name)
	}
	total := 0

	for i := range current.links {
		if !isVisited(current.links[i].name, visited) {
			total += visit(current.links[i], visited, extraTime)
		} else if extraTime && current.links[i].name != "start" {
			total += visit(current.links[i], visited, false)
		}
	}
	return total
}

func isVisited(name string, visited []string) bool {
	for i := range visited {
		if name == visited[i] {
			return true
		}
	}
	return false
}

func addIfAbsent(caves *[](*Cave), name string) {
	// fmt.Printf("Add %v\n", name)
	if !exists(caves, name) {
		cave := Cave{name: name, big: isUpper(name)}
		*caves = append(*caves, &cave)
	}
}

func isUpper(name string) bool {
	return strings.ToUpper(name) == name
}

func exists(caves *[](*Cave), name string) bool {
	for _, cave := range *caves {
		if cave.name == name {
			return true
		}
	}
	return false
}

func find(caves *[](*Cave), name string) (*Cave, error) {
	for i := range *caves {
		if (*caves)[i].name == name {
			return (*caves)[i], nil
		}
	}
	return &Cave{}, errors.New("Cave not found")
}

func link(caves *[](*Cave), a, b string) {
	// fmt.Printf("Link %v and %v\n", a, b)
	caveA, err := find(caves, a)
	if err != nil {
		panic(err)
	}
	caveB, err := find(caves, b)
	if err != nil {
		panic(err)
	}
	caveA.links = append(caveA.links, caveB)
	caveB.links = append(caveB.links, caveA)
}
