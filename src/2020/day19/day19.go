package day19

import (
	"log"
	"strconv"
	"strings"
)

func parseRules(ruleStr string) [][]int {
	var rules [][]int
	for _, rule := range strings.Split(ruleStr, "|") {
		var oneSet []int
		for _, numStr := range strings.Split(rule, " ") {
			if numStr == "" {
				continue
			}
			num, err := strconv.Atoi(numStr)
			if err != nil {
				log.Fatalf("invalid num: %s", numStr)
			}
			oneSet = append(oneSet, num)
		}
		rules = append(rules, oneSet)
	}
	return rules
}

func generatePossible(m map[int][][]int, start map[int]string) map[int][]string {
	cache := make(map[int][]string)
	var dfs func(r int) []string
	dfs = func(r int) []string {
		if val, ok := cache[r]; ok {
			return val
		}
		if val, ok := start[r]; ok {
			cache[r] = []string{val}
			return []string{val}
		}
		possibleMap := make(map[string]bool)
		for _, rule := range m[r] {
			rulePoss := []string{""}
			for _, num := range rule {
				numPoss := dfs(num)
				var newRulePoss []string
				for _, r := range rulePoss {
					for _, s := range numPoss {
						newRulePoss = append(newRulePoss, r+s)
					}
				}
				rulePoss = newRulePoss
			}
			for _, poss := range rulePoss {
				possibleMap[poss] = true
			}
		}
		var possible []string
		for k := range possibleMap {
			possible = append(possible, k)
		}
		cache[r] = possible
		// if r == 8 || r == 42 || r == 31 {
		// 	for _, z := range possible {
		// 		fmt.Println(len(z))
		// 	}
		// 	fmt.Printf("%d %v\n", r, possible)
		// }
		return possible
	}
	dfs(0)
	return cache
}

func partOne(lines []string) int {
	rules := true
	m := make(map[int][][]int)
	start := make(map[int]string)
	possible := make(map[string]bool)
	count := 0
	for _, line := range lines {
		if line == "" {
			rules = false
			for _, s := range generatePossible(m, start)[0] {
				possible[s] = true
			}
			continue
		}
		if rules {
			tokens := strings.Split(line, ":")
			num, err := strconv.Atoi(tokens[0])
			if err != nil {
				log.Fatalf("invalid num: %s", tokens[0])
			}
			if strings.Contains(tokens[1], "\"") {
				start[num] = strings.Trim(tokens[1], " \"")
			} else {
				m[num] = parseRules(tokens[1])
			}
			continue
		}
		if possible[line] {
			count += 1
		}
	}
	return count
}

func secondPart(cache map[int][]string, line string) bool {
	fourtwo := make(map[string]bool)
	size := -1
	for _, v := range cache[42] {
		fourtwo[v] = true
		if size != -1 && len(v) != size {
			log.Fatalf("inconsistent size: %d", len(v))
		}
		size = len(v)
	}
	threeone := make(map[string]bool)
	for _, v := range cache[31] {
		threeone[v] = true
		if len(v) != size {
			log.Fatalf("inconsistent size: %d", len(v))
		}
	}
	var secondhalf func(l string) bool
	secondhalf = func(l string) bool {
		if len(l) < 2*size || len(l)/size%2 != 0 {
			return false
		}
		if !fourtwo[l[:size]] || !threeone[l[len(l)-size:]] {
			return false
		}
		if len(l) == 2*size {
			return true
		}
		return secondhalf(l[size : len(l)-size])
	}
	var firsthalf func(l string) bool
	firsthalf = func(l string) bool {
		if len(l) < size || !fourtwo[l[:size]] {
			return false
		}
		return secondhalf(l[size:]) || firsthalf(l[size:])
	}
	return firsthalf(line)
}

func partTwo(lines []string) int {
	rules := true
	m := make(map[int][][]int)
	start := make(map[int]string)
	possible := make(map[string]bool)
	var cache map[int][]string
	count := 0
	for _, line := range lines {
		if line == "" {
			rules = false
			cache = generatePossible(m, start)
			for _, s := range cache[0] {
				possible[s] = true
			}
			continue
		}
		if rules {
			tokens := strings.Split(line, ":")
			num, err := strconv.Atoi(tokens[0])
			if err != nil {
				log.Fatalf("invalid num: %s", tokens[0])
			}
			if strings.Contains(tokens[1], "\"") {
				start[num] = strings.Trim(tokens[1], " \"")
			} else {
				m[num] = parseRules(tokens[1])
			}
			continue
		}
		if possible[line] || secondPart(cache, line) {
			count += 1
			continue
		}
	}
	return count
}
