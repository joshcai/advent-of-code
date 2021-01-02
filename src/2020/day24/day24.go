package day24

func buildBlackMap(lines []string) map[[2]int]bool {
	black := make(map[[2]int]bool)
	for _, line := range lines {
		var dirs []string
		for i := 0; i < len(line); i++ {
			if line[i] == 'e' || line[i] == 'w' {
				dirs = append(dirs, string(line[i]))
			} else {
				dirs = append(dirs, string(line[i])+string(line[i+1]))
				i += 1
			}
		}
		x := 0
		y := 0
		for _, dir := range dirs {
			switch dir {
			case "e":
				x += 2
			case "w":
				x -= 2
			case "ne":
				x += 1
				y -= 1
			case "nw":
				x -= 1
				y -= 1
			case "se":
				x += 1
				y += 1
			case "sw":
				x -= 1
				y += 1
			}
		}
		black[[2]int{x, y}] = !black[[2]int{x, y}]
		// This part is only needed for part 2
		for _, dir := range DIRS {
			if _, ok := black[[2]int{x + dir[0], y + dir[1]}]; !ok {
				black[[2]int{x + dir[0], y + dir[1]}] = false
			}
		}
	}
	return black
}

func partOne(lines []string) int {
	black := buildBlackMap(lines)
	count := 0
	for _, v := range black {
		if v {
			count += 1
		}
	}
	return count
}

var DIRS [][]int = [][]int{
	{2, 0},
	{-2, 0},
	{1, 1},
	{1, -1},
	{-1, 1},
	{-1, -1},
}

func partTwo(lines []string) int {
	black := buildBlackMap(lines)
	next := func(x, y int, curr bool) bool {
		count := 0
		for _, dir := range DIRS {
			if black[[2]int{x + dir[0], y + dir[1]}] {
				count += 1
			}
		}
		if curr {
			return count == 1 || count == 2
		} else {
			return count == 2
		}
	}
	for i := 0; i < 100; i++ {
		nextBlack := make(map[[2]int]bool)
		for k, v := range black {
			x, y := k[0], k[1]
			nextBlack[[2]int{x, y}] = next(x, y, v)
			for _, dir := range DIRS {
				if _, ok := black[[2]int{x + dir[0], y + dir[1]}]; !ok {
					nextBlack[[2]int{x + dir[0], y + dir[1]}] = false
				}
			}
		}
		black = nextBlack
	}
	count := 0
	for _, v := range black {
		if v {
			count += 1
		}
	}
	return count
}
