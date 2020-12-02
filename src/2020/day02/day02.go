package day02

import (
	"log"
	"strconv"
	"strings"
)

func parseLine(line string) (low, high int, c rune, s string) {
	tokens := strings.Split(line, " ")
	if len(tokens) != 3 {
		log.Fatalf("Invalid number of tokens for %q, tokens: %v", line, tokens)
	}
	nums := strings.Split(tokens[0], "-")

	low, err := strconv.Atoi(nums[0])
	if err != nil {
		log.Fatalf("Failed to convert to number: %s", nums[0])
	}
	high, err = strconv.Atoi(nums[1])
	if err != nil {
		log.Fatalf("Failed to convert to number: %s", nums[1])
	}
	return low, high, rune(tokens[1][0]), tokens[2]
}

func valid(low, high int, c rune, s string) bool {
	m := make(map[rune]int)
	for _, r := range s {
		m[r] += 1
	}
	n := m[c]
	return n >= low && n <= high
}

func partOne(strings []string) int {
	counter := 0
	for _, ss := range strings {
		low, high, c, s := parseLine(ss)
		if valid(low, high, c, s) {
			counter += 1
		}
	}
	return counter
}

func valid2(low, high int, c rune, s string) bool {
	first := false
	if low <= len(s) {
		first = rune(s[low-1]) == c
	}
	second := false
	if high <= len(s) {
		second = rune(s[high-1]) == c
	}
	return first != second
}

func partTwo(strings []string) int {
	counter := 0
	for _, ss := range strings {
		low, high, c, s := parseLine(ss)
		if valid2(low, high, c, s) {
			counter += 1
		}
	}
	return counter
}
