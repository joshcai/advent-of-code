package day03

func partOne(strings []string) int {
	pos := 0
	counter := 0
	for _, s := range strings {
		if rune(s[pos]) == '#' {
			counter += 1
		}
		pos = (pos + 3) % len(s)
	}
	return counter
}

func slope(strings []string, right, down int) int {
	pos := 0
	counter := 0
	for i := 0; i < len(strings); i += down {
		s := strings[i]
		if rune(s[pos]) == '#' {
			counter += 1
		}
		pos = (pos + right) % len(s)
	}
	return counter
}

func partTwo(strings []string) int {
	total := 1
	for _, nums := range [][]int{{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}} {
		total *= slope(strings, nums[0], nums[1])
	}
	return total
}
