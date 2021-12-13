package main

import "fmt"

type Cave struct {
	name string
}

func main() {
	a := Cave{"minecraft"}
	fmt.Printf("value=%v ref=%p\n", a, &a)
	fmt.Printf("value=%v ref=%p\n", a, &a)

	caves := []Cave{}
	caves = append(caves, a)
	fmt.Printf("value=%v ref=%p\n", caves, &caves)
	fmt.Printf("value=%v ref=%p\n", caves[0], &caves[0])

	cavesPtr := [](*Cave){}

	cavesPtr = append(cavesPtr, &a)

	fmt.Printf("value=%v ref=%p\n", caves[0], cavesPtr[0])

	a.name = ""
}
