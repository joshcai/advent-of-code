package day12

import (
	"log"
	"strconv"
)

var DIRS = [][]int{
	{1, 0},  // east
	{0, -1}, // south
	{-1, 0}, // west
	{0, 1},  // north
}

func abs(x int) int {
	if x < 0 {
		return -1 * x
	}
	return x
}

func partOne(lines []string) int {
	dir := 0
	x := 0
	y := 0
	for _, line := range lines {
		c := line[0]
		numStr := line[1:]
		num, err := strconv.Atoi(numStr)
		if err != nil {
			log.Fatalf("bad number: %v", numStr)
		}
		switch c {
		case 'N':
			y += num
		case 'S':
			y -= num
		case 'E':
			x += num
		case 'W':
			x -= num
		case 'L':
			dir = ((dir - num/90) + 4) % 4
		case 'R':
			dir = ((dir + num/90) + 4) % 4
		case 'F':
			actualDir := DIRS[dir]
			x += actualDir[0] * num
			y += actualDir[1] * num
		}
	}
	return abs(x) + abs(y)
}

func partTwo(lines []string) int {
	x := 0
	y := 0
	wx := 10
	wy := 1
	for _, line := range lines {
		c := line[0]
		numStr := line[1:]
		num, err := strconv.Atoi(numStr)
		if err != nil {
			log.Fatalf("bad number: %v", numStr)
		}
		switch c {
		case 'N':
			wy += num
		case 'S':
			wy -= num
		case 'E':
			wx += num
		case 'W':
			wx -= num
		case 'R':
			num = 360 - num
			fallthrough
		case 'L':
			for i := 0; i < num/90; i++ {
				wx, wy = -1*wy, wx
			}
		case 'F':
			x += num * wx
			y += num * wy
		}
	}
	return abs(x) + abs(y)
}
