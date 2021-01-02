package day22

import (
	"log"
	"strconv"
	"strings"
)

func atoi(s string) int {
	v, err := strconv.Atoi(s)
	if err != nil {
		log.Fatalf("could not parse string into number: %s", s)
	}
	return v
}

type player struct {
	data []int
}

func (p *player) empty() bool {
	return len(p.data) == 0
}

func (p *player) pop() int {
	if p.empty() {
		return -1
	}
	a := p.data[0]
	p.data = p.data[1:]
	return a
}

func (p *player) add(a, b int) {
	p.data = append(p.data, a)
	p.data = append(p.data, b)
}

func parsePlayers(lines []string) (*player, *player) {
	parseP1 := true
	var p1Buff []int
	var p2Buff []int
	for _, line := range lines {
		if strings.HasPrefix(line, "Player") {
			continue
		}
		if line == "" {
			parseP1 = false
			continue
		}
		if parseP1 {
			p1Buff = append(p1Buff, atoi(line))
		} else {
			p2Buff = append(p2Buff, atoi(line))
		}
	}
	p1 := &player{data: p1Buff}
	p2 := &player{data: p2Buff}
	return p1, p2
}

func partOne(lines []string) int {
	p1, p2 := parsePlayers(lines)
	for !p1.empty() && !p2.empty() {
		a, b := p1.pop(), p2.pop()
		if a > b {
			p1.add(a, b)
		} else {
			p2.add(b, a)
		}
	}
	var final []int
	if p1.empty() {
		final = p2.data
	} else {
		final = p1.data
	}
	score := 0
	size := len(final)
	for i, val := range final {
		score += (size - i) * val
	}
	return score
}

func copySlice(a []int, num int) []int {
	var b []int
	for _, z := range a {
		if len(b) == num {
			break
		}
		b = append(b, z)
	}
	return b
}

// true if P1 wins
func playGame(p1, p2 *player) bool {
	seen := make(map[[60]int]bool)
	for !p1.empty() && !p2.empty() {
		var state [60]int
		for i, x := range p1.data {
			state[i] = x
		}
		state[len(p1.data)] = -1
		for j, y := range p2.data {
			state[j+len(p1.data)+1] = y
		}
		if seen[state] {
			return true
		}
		seen[state] = true
		a, b := p1.pop(), p2.pop()
		if a <= len(p1.data) && b <= len(p2.data) {
			newP1 := &player{data: copySlice(p1.data, a)}
			newP2 := &player{data: copySlice(p2.data, b)}
			if playGame(newP1, newP2) {
				p1.add(a, b)
			} else {
				p2.add(b, a)
			}
			continue
		}
		if a > b {
			p1.add(a, b)
		} else {
			p2.add(b, a)
		}
	}
	return !p1.empty()
}

func partTwo(lines []string) int {
	p1, p2 := parsePlayers(lines)
	var final []int
	if playGame(p1, p2) {
		final = p1.data
	} else {
		final = p2.data
	}
	score := 0
	size := len(final)
	for i, val := range final {
		score += (size - i) * val
	}
	return score
}
