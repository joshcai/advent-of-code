package day13

import (
	"log"
	"strconv"
	"strings"
)

func partOne(lines []string) int {
	if len(lines) != 2 {
		log.Fatalf("invalid number of lines: %v", lines)
	}
	earliestStr := lines[0]
	earliest, err := strconv.Atoi(earliestStr)
	if err != nil {
		log.Fatalf("invalid earliest num: %v", earliestStr)
	}

	busesStr := strings.Split(lines[1], ",")
	var buses []int
	for _, busStr := range busesStr {
		if busStr == "x" {
			continue
		}
		bus, err := strconv.Atoi(busStr)
		if err != nil {
			log.Fatalf("invalid bus number: %v", busStr)
		}
		buses = append(buses, bus)
	}
	min := -1
	final := 0
	for _, bus := range buses {
		offset := bus - earliest%bus
		if offset < min || min == -1 {
			min = offset
			final = bus * offset
		}
	}
	return final
}

type busInfo struct {
	bus int
	idx int
}

func partTwo(lines []string) int {
	if len(lines) != 2 {
		log.Fatalf("invalid number of lines: %v", lines)
	}
	busesStr := strings.Split(lines[1], ",")
	var buses []busInfo
	for i, busStr := range busesStr {
		if busStr == "x" {
			continue
		}
		bus, err := strconv.Atoi(busStr)
		if err != nil {
			log.Fatalf("invalid bus number: %v", busStr)
		}
		buses = append(buses, busInfo{bus: bus, idx: i})
	}
	offset := 0
	factor := buses[0].bus
	for i := 1; i < len(buses); i++ {
		newOffset := -1
		newFactor := 0
		bus := buses[i]
		counter := 1
		for {
			start := counter * factor
			if (start+bus.idx+offset)%bus.bus == 0 {
				if newOffset == -1 {
					newOffset = start + offset
				} else {
					newFactor = start + offset - newOffset
					break
				}
			}
			counter += 1
		}
		offset = newOffset
		factor = newFactor
	}
	return offset
}
