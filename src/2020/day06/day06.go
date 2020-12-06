package day06

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
	m := make(map[rune]bool)
	ss = append(ss, "")
	count := 0
	for _, s := range ss {
		if s == "" {
			count += len(m)
			m = make(map[rune]bool)
		}
		for _, r := range s {
			m[r] = true
		}
	}
	return count
}

func partTwo(ss []string) int {
	m := make(map[rune]int)
	ss = append(ss, "")
	count := 0
	curr := 0
	for _, s := range ss {
		if s == "" {
			c := 0
			for _, v := range m {
				if v == curr {
					c += 1
				}
			}
			count += c
			m = make(map[rune]int)
			curr = 0
			continue
		}
		curr += 1
		for _, r := range s {
			m[r] += 1
		}
	}
	return count
}
