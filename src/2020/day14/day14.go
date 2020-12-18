package day14

import (
	"log"
	"math"
	"strconv"
	"strings"
)

func maskedValue(mask string, num int) int {
	last := len(mask)
	value := 0
	for i := 0; i < len(mask); i++ {
		pow := int(math.Pow(2, float64(last-i-1)))
		present := false
		if pow <= num {
			num -= pow
			present = true
		}
		switch mask[i] {
		case 'X':
			if present {
				value += pow
			}
		case '1':
			value += pow
		case '0':
		}
	}
	return value
}

func partOne(lines []string) int {
	mem := make(map[int]int)
	var mask string
	for _, line := range lines {
		if strings.HasPrefix(line, "mask") {
			mask = line[7:]
		} else {
			tokens := strings.Split(line, " = ")
			addrStr := tokens[0][4 : len(tokens[0])-1]
			addr, err := strconv.Atoi(addrStr)
			if err != nil {
				log.Fatalf("invalid addr num: %v", addrStr)
			}
			num, err := strconv.Atoi(tokens[1])
			if err != nil {
				log.Fatalf("invalid num: %v", tokens[1])
			}
			mem[addr] = maskedValue(mask, num)
		}
	}
	final := 0
	for _, v := range mem {
		final += v
	}
	return final
}

func maskedValue2(mask string, num int) []int {
	last := len(mask)
	values := []int{0}
	for i := 0; i < len(mask); i++ {
		pow := int(math.Pow(2, float64(last-i-1)))
		present := false
		if pow <= num {
			num -= pow
			present = true
		}
		switch mask[i] {
		case 'X':
			next := make([]int, len(values))
			for i, value := range values {
				next[i] = value + pow
			}
			values = append(values, next...)
		case '1':
			for i, val := range values {
				values[i] = val + pow
			}
		case '0':
			if present {
				for i, val := range values {
					values[i] = val + pow
				}
			}
		}
	}
	return values
}

func partTwo(lines []string) int {
	mem := make(map[int]int)
	var mask string
	for _, line := range lines {
		if strings.HasPrefix(line, "mask") {
			mask = line[7:]
		} else {
			tokens := strings.Split(line, " = ")
			addrStr := tokens[0][4 : len(tokens[0])-1]
			addr, err := strconv.Atoi(addrStr)
			if err != nil {
				log.Fatalf("invalid addr num: %v", addrStr)
			}
			num, err := strconv.Atoi(tokens[1])
			if err != nil {
				log.Fatalf("invalid num: %v", tokens[1])
			}
			for _, val := range maskedValue2(mask, addr) {
				mem[val] = num
			}
		}
	}
	final := 0
	for _, v := range mem {
		final += v
	}
	return final
}
