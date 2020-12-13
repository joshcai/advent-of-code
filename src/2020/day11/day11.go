package day11

var DIRS = [][]int{
	{-1, -1},
	{-1, 0},
	{-1, 1},
	{0, 1},
	{0, -1},
	{1, -1},
	{1, 1},
	{1, 0},
}

func getNext(grid [][]rune, x, y int) rune {
	get := func(i, j int) rune {
		if i < 0 || i >= len(grid) || j < 0 || j >= len(grid[0]) {
			return '?'
		}
		return grid[i][j]
	}
	curr := get(x, y)
	if curr == '.' {
		return '.'
	}
	around := 0
	for _, dir := range DIRS {
		if get(x+dir[0], y+dir[1]) == '#' {
			around += 1
		}
	}
	if around == 0 {
		return '#'
	}

	if around >= 4 {
		return 'L'
	}
	return curr
}

func getNext2(grid [][]rune, x, y int) rune {
	var get func(i, j, dx, dy int) rune
	get = func(i, j, dx, dy int) rune {
		if i < 0 || i >= len(grid) || j < 0 || j >= len(grid[0]) {
			return '?'
		}
		curr := grid[i][j]
		if curr == '.' {
			return get(i+dx, j+dy, dx, dy)
		}
		return curr
	}
	curr := grid[x][y]
	if curr == '.' {
		return '.'
	}
	around := 0
	for _, dir := range DIRS {
		if get(x+dir[0], y+dir[1], dir[0], dir[1]) == '#' {
			around += 1
		}
	}
	if around == 0 {
		return '#'
	}

	if around >= 5 {
		return 'L'
	}
	return curr
}

func partOne(lines []string) int {
	grid := make([][]rune, len(lines))
	for i, line := range lines {
		grid[i] = make([]rune, len(line))
		for j, r := range line {
			grid[i][j] = r
		}
	}
	changed := true
	count := 0
	for changed {
		changed = false
		count = 0
		newGrid := make([][]rune, len(lines))
		for i, line := range grid {
			newGrid[i] = make([]rune, len(line))
			for j, old := range line {
				next := getNext(grid, i, j)
				if next != old {
					changed = true
				}
				if next == '#' {
					count += 1
				}
				newGrid[i][j] = next
			}
		}
		grid = newGrid
	}
	return count
}

func partTwo(lines []string) int {
	grid := make([][]rune, len(lines))
	for i, line := range lines {
		grid[i] = make([]rune, len(line))
		for j, r := range line {
			grid[i][j] = r
		}
	}
	changed := true
	count := 0
	for changed {
		changed = false
		count = 0
		newGrid := make([][]rune, len(lines))
		for i, line := range grid {
			newGrid[i] = make([]rune, len(line))
			for j, old := range line {
				next := getNext2(grid, i, j)
				if next != old {
					changed = true
				}
				if next == '#' {
					count += 1
				}
				newGrid[i][j] = next
			}
		}
		grid = newGrid
	}
	return count
}
