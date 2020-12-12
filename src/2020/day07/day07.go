package day07

import (
	"log"
	"strconv"
	"strings"
)

func makeGraph(lines []string) map[string][]string {
	m := make(map[string][]string)
	for _, line := range lines {
		tokens := strings.Split(line, "contain")
		if len(tokens) != 2 {
			log.Fatalf("incorrect tokens: %v", tokens)
		}
		second := strings.Trim(tokens[1], " ")
		if second == "no other bags." {
			continue
		}
		first := tokens[0][:len(tokens[0])-6]
		bags := strings.Split(second, ",")
		for _, bag := range bags {
			bagTokens := strings.Split(strings.Trim(bag, " "), " ")
			if len(bagTokens) != 4 {
				log.Fatalf("incorrect bag tokens: %v", bagTokens)
			}
			b := bagTokens[1] + " " + bagTokens[2]
			m[b] = append(m[b], first)
		}
	}
	return m
}

func partOne(lines []string) int {
	m := makeGraph(lines)
	seen := make(map[string]bool)
	var dfs func(curr string)
	dfs = func(curr string) {
		next := m[curr]
		for _, n := range next {
			if seen[n] {
				continue
			}
			seen[n] = true
			dfs(n)
		}
	}
	dfs("shiny gold")
	return len(seen)
}

type node struct {
	color string
	num   int
}

func makeGraph2(lines []string) map[string][]node {
	m := make(map[string][]node)
	for _, line := range lines {
		tokens := strings.Split(line, "contain")
		if len(tokens) != 2 {
			log.Fatalf("incorrect tokens: %v", tokens)
		}
		second := strings.Trim(tokens[1], " ")
		if second == "no other bags." {
			continue
		}
		first := tokens[0][:len(tokens[0])-6]
		bags := strings.Split(second, ",")
		for _, bag := range bags {
			bagTokens := strings.Split(strings.Trim(bag, " "), " ")
			if len(bagTokens) != 4 {
				log.Fatalf("incorrect bag tokens: %v", bagTokens)
			}
			num, err := strconv.Atoi(bagTokens[0])
			if err != nil {
				log.Fatalf("invalid number: %v", bagTokens[0])
			}
			b := bagTokens[1] + " " + bagTokens[2]
			m[first] = append(m[first], node{color: b, num: num})
		}
	}
	return m
}

func partTwo(lines []string) int {
	m := makeGraph2(lines)
	count := make(map[string]int)
	var dfs func(curr string) int
	dfs = func(curr string) int {
		next := m[curr]
		sum := 0
		for _, n := range next {
			if v, ok := count[n.color]; ok {
				sum += n.num*v + n.num
				continue
			}
			v := dfs(n.color)
			sum += n.num*v + n.num
		}
		count[curr] = sum
		return sum
	}
	return dfs("shiny gold")
}
