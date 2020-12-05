package day05

import (
	"log"
	"strconv"
	"strings"
)

func seatID(seat string) int {
	replace := map[string]string{
		"F": "0",
		"B": "1",
		"L": "0",
		"R": "1",
	}
	for k, v := range replace {
		seat = strings.ReplaceAll(seat, k, v)
	}
	row := seat[:7]
	col := seat[7:]
	rowNum, err := strconv.ParseInt(row, 2, 64)
	if err != nil {
		log.Fatalf("could not parse row: %s", row)
	}
	colNum, err := strconv.ParseInt(col, 2, 64)
	if err != nil {
		log.Fatalf("could not parse col: %s", col)
	}
	return int(rowNum*8 + colNum)
}

func partOne(ss []string) int {
	max := 0
	for _, s := range ss {
		curr := seatID(s)
		if curr > max {
			max = curr
		}
	}
	return max
}

func partTwo(ss []string) int {
	max := 0
	min := -1
	m := make(map[int]bool)
	for _, s := range ss {
		curr := seatID(s)
		if curr > max {
			max = curr
		}
		if min == -1 || curr < min {
			min = curr
		}
		m[curr] = true
	}
	for i := min + 1; i < max; i++ {
		if !m[i] && m[i-1] && m[i+1] {
			return i
		}
	}
	return -1
}
