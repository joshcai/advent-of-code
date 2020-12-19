package day16

import (
	"log"
	"strconv"
	"strings"
)

type info struct {
	min, max   int
	min2, max2 int
}

func parseRange(rangeStr string) (int, int) {
	r := strings.Split(rangeStr, "-")
	min, err := strconv.Atoi(r[0])
	if err != nil {
		log.Fatalf("invalid num: %s", r[0])
	}
	max, err := strconv.Atoi(r[1])
	if err != nil {
		log.Fatalf("invalid num: %s", r[1])
	}
	return min, max
}

func valid(rules map[string]info, num int) bool {
	for _, rule := range rules {
		if num >= rule.min && num <= rule.max || num >= rule.min2 && num <= rule.max2 {
			return true
		}
	}
	return false
}

func partOne(lines []string) int {
	m := make(map[string]info)
	your := false
	nearby := false
	final := 0
	for _, line := range lines {
		if line == "" {
			continue
		}
		if line == "your ticket:" {
			your = true
			continue
		}
		if line == "nearby tickets:" {
			nearby = true
			continue
		}
		if !your && !nearby {
			tokens := strings.Split(line, ": ")
			name := tokens[0]
			nums := strings.Split(tokens[1], " or ")
			min, max := parseRange(nums[0])
			min2, max2 := parseRange(nums[1])
			m[name] = info{min: min, max: max, min2: min2, max2: max2}
		} else if your && !nearby {
			continue
		} else {
			for _, numStr := range strings.Split(line, ",") {
				num, err := strconv.Atoi(numStr)
				if err != nil {
					log.Fatalf("invalid num: %s", numStr)
				}
				if !valid(m, num) {
					final += num
				}
			}
		}
	}
	return final
}

func updatePossible(possible map[string][]bool, rules map[string]info, i, num int) {
	for key, rule := range rules {
		if num >= rule.min && num <= rule.max || num >= rule.min2 && num <= rule.max2 {
			continue
		}
		possible[key][i] = false
	}
}

func index(possible []bool) int {
	idx := -1
	for i, b := range possible {
		if b {
			if idx == -1 {
				idx = i
			} else {
				return -1
			}
		}
	}
	return idx
}

func partTwo(lines []string) map[string]int {
	m := make(map[string]info)
	your := false
	nearby := false
	var yourTicket []int
	possible := make(map[string][]bool)
	for _, line := range lines {
		if line == "" {
			continue
		}
		if line == "your ticket:" {
			your = true
			continue
		}
		if line == "nearby tickets:" {
			nearby = true
			continue
		}
		if !your && !nearby {
			tokens := strings.Split(line, ": ")
			name := tokens[0]
			nums := strings.Split(tokens[1], " or ")
			min, max := parseRange(nums[0])
			min2, max2 := parseRange(nums[1])
			m[name] = info{min: min, max: max, min2: min2, max2: max2}
		} else if your && !nearby {
			for _, numStr := range strings.Split(line, ",") {
				num, err := strconv.Atoi(numStr)
				if err != nil {
					log.Fatalf("invalid num: %s", numStr)
				}
				yourTicket = append(yourTicket, num)
			}
			for k := range m {
				temp := make([]bool, len(yourTicket))
				for i := range temp {
					temp[i] = true
				}
				possible[k] = temp
			}
		} else {
			var nums []int
			invalid := false
			for _, numStr := range strings.Split(line, ",") {
				num, err := strconv.Atoi(numStr)
				if err != nil {
					log.Fatalf("invalid num: %s", numStr)
				}
				if !valid(m, num) {
					invalid = true
					break
				}
				if invalid {
					continue
				}
				nums = append(nums, num)
			}
			for i, num := range nums {
				updatePossible(possible, m, i, num)
			}
		}
	}
	seen := make(map[string]int)
	for len(seen) != len(possible) {
		for key, values := range possible {
			if _, ok := seen[key]; ok {
				continue
			}
			idx := index(values)
			if idx != -1 {
				seen[key] = idx
				for key2, values2 := range possible {
					if key2 == key {
						continue
					}
					values2[idx] = false
					possible[key2] = values2
				}
				break
			}
		}
	}
	final := make(map[string]int)
	for key := range possible {
		final[key] = yourTicket[seen[key]]
	}
	return final
}
