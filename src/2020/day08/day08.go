package day08

import (
	"errors"
	"log"
	"strconv"
	"strings"
)

func partOne(lines []string) int {
	seen := make(map[int]bool)
	acc := 0
	pc := 0
	for {
		if seen[pc] {
			return acc
		}
		seen[pc] = true
		line := lines[pc]
		tokens := strings.Split(line, " ")
		instruction := tokens[0]
		numStr := tokens[1][1:]
		num, err := strconv.Atoi(numStr)
		if err != nil {
			log.Fatalf("invalid num: %v", numStr)
		}
		if tokens[1][0] == '-' {
			num *= -1
		}
		switch instruction {
		case "nop":
			pc += 1
		case "acc":
			acc += num
			pc += 1
		case "jmp":
			pc += num
		}
	}
	return -1
}

func runOnce(lines []string) (int, error) {
	seen := make(map[int]bool)
	acc := 0
	pc := 0
	for pc < len(lines) {
		if seen[pc] {
			return acc, errors.New("not valid")
		}
		seen[pc] = true
		line := lines[pc]
		tokens := strings.Split(line, " ")
		instruction := tokens[0]
		numStr := tokens[1][1:]
		num, err := strconv.Atoi(numStr)
		if err != nil {
			log.Fatalf("invalid num: %v", numStr)
		}
		if tokens[1][0] == '-' {
			num *= -1
		}
		switch instruction {
		case "nop":
			pc += 1
		case "acc":
			acc += num
			pc += 1
		case "jmp":
			pc += num
		}
	}
	return acc, nil
}

func partTwo(lines []string) int {
	for i, line := range lines {
		if strings.HasPrefix(line, "acc") {
			continue
		}
		var newLine string
		if strings.HasPrefix(line, "nop") {
			newLine = strings.ReplaceAll(line, "nop", "jmp")
		} else if strings.HasPrefix(line, "jmp") {
			newLine = strings.ReplaceAll(line, "jmp", "nop")
		}
		newLines := make([]string, len(lines))
		copy(newLines, lines)
		newLines[i] = newLine
		acc, err := runOnce(newLines)
		if err == nil {
			return acc
		}
	}
	return -1
}
