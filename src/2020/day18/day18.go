package day18

import (
	"log"
	"strconv"
	"strings"
)

func solve(line string) int {
	line = strings.ReplaceAll(line, " ", "")
	total := 0
	op := '+'
	for i := 0; i < len(line); i++ {
		if line[i] == '+' || line[i] == '*' {
			op = rune(line[i])
			continue
		}
		var val int
		if line[i] == '(' {
			level := 1
			idx := -1
			for j := i + 1; j < len(line); j++ {
				if line[j] == '(' {
					level += 1
				} else if line[j] == ')' {
					level -= 1
					if level == 0 {
						idx = j
						break
					}
				}
			}
			val = solve(line[i+1 : idx])
			i = idx
		} else {
			valNum, err := strconv.Atoi(string(line[i]))
			if err != nil {
				log.Fatalf("invalid number: %v", valNum)
			}
			val = valNum
		}
		if op == '+' {
			total += val
		} else {
			total *= val
		}
	}
	return total
}

func partOne(lines []string) int {
	sum := 0
	for _, line := range lines {
		sum += solve(line)
	}
	return sum
}

func solve2(line string) int {
	line = strings.ReplaceAll(line, " ", "")
	total := 0
	op := '+'
	var sums []int
	for i := 0; i < len(line); i++ {
		if line[i] == '+' || line[i] == '*' {
			op = rune(line[i])
			continue
		}
		var val int
		if line[i] == '(' {
			level := 1
			idx := -1
			for j := i + 1; j < len(line); j++ {
				if line[j] == '(' {
					level += 1
				} else if line[j] == ')' {
					level -= 1
					if level == 0 {
						idx = j
						break
					}
				}
			}
			val = solve2(line[i+1 : idx])
			i = idx
		} else {
			valNum, err := strconv.Atoi(string(line[i]))
			if err != nil {
				log.Fatalf("invalid number: %v", valNum)
			}
			val = valNum
		}
		if op == '+' {
			total += val
		} else {
			sums = append(sums, total)
			total = val
		}
	}
	sums = append(sums, total)
	final := 1
	for _, sum := range sums {
		final *= sum
	}
	return final
}

func partTwo(lines []string) int {
	sum := 0
	for _, line := range lines {
		sum += solve2(line)
	}
	return sum
}
