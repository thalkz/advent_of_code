package main

import (
	// "bytes"
	"encoding/hex"
	"fmt"
	"os"
)

type Packet struct {
	version    uint
	typeId     uint
	value      uint
	subpackets []Packet
}

func main() {
	fileBytes, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Printf("Failed to open file: %v", err)
		return
	}
	inputString := string(fileBytes)
	parse(inputString)
}

func parse(input string) {
	fmt.Printf("\nInput: %v\n", input)
	inputBytes, err := hex.DecodeString(input)
	if err != nil {
		panic(err)
	}
	// fmt.Printf("%08b\n", inputBytes)
	bits := make([]bool, len(inputBytes)*8)
	for i := range inputBytes {
		for j := 0; j < 8; j++ {
			bits[(i*8)+j] = inputBytes[i]&(1<<(7-j)) > 0
		}
	}
	printBinary(bits)
	packet, length := parsePacket(bits)
	totalVersion := sumVersion(packet)
	fmt.Printf("Packet: %v, length: %v, totalVersion: %v\n", packet, length, totalVersion)
}

func sumVersion(packet Packet) uint {
	total := packet.version
	for _, subpacket := range packet.subpackets {
		total += sumVersion(subpacket)
	}
	return total
}

func parsePacket(bits []bool) (Packet, int) {
	fmt.Printf("Parse packet: ")
	printBinary(bits)

	version := toUInt(bits[0:3])
	typeId := toUInt(bits[3:6])

	if typeId == 4 {
		// Literal value
		cursor := 6
		keepReading := true
		literalBinary := []bool{}
		for keepReading {
			keepReading = bits[cursor]
			literalBinary = append(literalBinary, bits[cursor+1:cursor+5]...)
			cursor += 5
		}
		literal := toUInt(literalBinary)
		return Packet{
			version: version,
			typeId:  typeId,
			value:   literal,
		}, cursor
	} else if bits[6] == false {
		fmt.Printf("Length Type Id = 0\n")
		totalLength := int(toUInt(bits[7:22]) + 22)
		printBinary(bits[7:22])
		fmt.Printf("Sub packets length in bits = %v\n", totalLength)
		cursor := 22
		subpackets := []Packet{}
		for cursor < totalLength {
			packet, length := parsePacket(bits[cursor:])
			subpackets = append(subpackets, packet)
			cursor += length
		}
		return Packet{
			version:    version,
			typeId:     typeId,
			subpackets: subpackets,
		}, cursor
	} else {
		fmt.Printf("Length Type Id == 1\n")
		count := int(toUInt(bits[7:18]))
		subpackets := []Packet{}
		cursor := 18
		for i := 0; i < count; i++ {
			packet, length := parsePacket(bits[cursor:])
			cursor += length
			subpackets = append(subpackets, packet)
		}
		return Packet{
			version:    version,
			typeId:     typeId,
			subpackets: subpackets,
		}, cursor
	}
}

func printBinary(bits []bool) {
	for _, bit := range bits {
		if bit {
			fmt.Printf("1")
		} else {
			fmt.Printf("0")
		}
	}
	fmt.Printf("\n")
}

func toUInt(bits []bool) uint {
	var result uint
	for i := 0; i < len(bits); i++ {
		if bits[i] {
			result += 1 << (len(bits) - 1 - i)
		}
	}
	return result
}
