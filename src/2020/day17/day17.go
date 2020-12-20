package day17

func partOne(lines []string) int {
	coords := make(map[int]map[int]map[int]bool)
	for x, line := range lines {
		if coords[x] == nil {
			coords[x] = make(map[int]map[int]bool)
		}
		for y, c := range line {
			if coords[x][y] == nil {
				coords[x][y] = make(map[int]bool)
			}
			state := c == '#'
			coords[x][y][0] = state
		}
	}
	get := func(x, y, z int) bool {
		yz, ok := coords[x]
		if !ok {
			return false
		}
		zVal, ok := yz[y]
		if !ok {
			return false
		}
		val, ok := zVal[z]
		if !ok {
			return false
		}
		return val
	}
	for t := 0; t < 6; t++ {
		active := make(map[int]map[int]map[int]int)
		for x, yzVals := range coords {
			for y, zVals := range yzVals {
				for z, val := range zVals {
					for i := -1; i < 2; i++ {
						for j := -1; j < 2; j++ {
							for k := -1; k < 2; k++ {
								if i == 0 && j == 0 && k == 0 || !val {
									continue
								}
								if active[x+i] == nil {
									active[x+i] = make(map[int]map[int]int)
								}
								if active[x+i][y+j] == nil {
									active[x+i][y+j] = make(map[int]int)
								}
								active[x+i][y+j][z+k] += 1
							}
						}
					}
				}
			}
		}
		newCoords := make(map[int]map[int]map[int]bool)
		for x, yzVals := range active {
			for y, zVals := range yzVals {
				for z, val := range zVals {
					state := false
					if get(x, y, z) {
						state = val == 2 || val == 3
					} else {
						state = val == 3
					}
					if newCoords[x] == nil {
						newCoords[x] = make(map[int]map[int]bool)
					}
					if newCoords[x][y] == nil {
						newCoords[x][y] = make(map[int]bool)
					}
					newCoords[x][y][z] = state
				}
			}
		}
		coords = newCoords
	}

	total := 0
	for _, yzVals := range coords {
		for _, zVals := range yzVals {
			for _, val := range zVals {
				if val {
					total += 1
				}
			}
		}
	}
	return total
}

func partTwo(lines []string) int {
	coords := make(map[int]map[int]map[int]map[int]bool)
	for x, line := range lines {
		if coords[x] == nil {
			coords[x] = make(map[int]map[int]map[int]bool)
		}
		for y, c := range line {
			if coords[x][y] == nil {
				coords[x][y] = make(map[int]map[int]bool)
			}
			if coords[x][y][0] == nil {
				coords[x][y][0] = make(map[int]bool)
			}
			state := c == '#'
			coords[x][y][0][0] = state
		}
	}
	get := func(x, y, z, w int) bool {
		yz, ok := coords[x]
		if !ok {
			return false
		}
		zVal, ok := yz[y]
		if !ok {
			return false
		}
		wVal, ok := zVal[z]
		if !ok {
			return false
		}
		val, ok := wVal[w]
		if !ok {
			return false
		}
		return val
	}
	for t := 0; t < 6; t++ {
		active := make(map[int]map[int]map[int]map[int]int)
		for x, yzVals := range coords {
			for y, zVals := range yzVals {
				for z, wVals := range zVals {
					for w, val := range wVals {
						for i := -1; i < 2; i++ {
							for j := -1; j < 2; j++ {
								for k := -1; k < 2; k++ {
									for l := -1; l < 2; l++ {
										if i == 0 && j == 0 && k == 0 && l == 0 || !val {
											continue
										}
										if active[x+i] == nil {
											active[x+i] = make(map[int]map[int]map[int]int)
										}
										if active[x+i][y+j] == nil {
											active[x+i][y+j] = make(map[int]map[int]int)
										}
										if active[x+i][y+j][z+k] == nil {
											active[x+i][y+j][z+k] = make(map[int]int)
										}
										active[x+i][y+j][z+k][w+l] += 1
									}
								}
							}
						}
					}
				}
			}
		}
		newCoords := make(map[int]map[int]map[int]map[int]bool)
		for x, yzVals := range active {
			for y, zVals := range yzVals {
				for z, wVals := range zVals {
					for w, val := range wVals {
						state := false
						if get(x, y, z, w) {
							state = val == 2 || val == 3
						} else {
							state = val == 3
						}
						if newCoords[x] == nil {
							newCoords[x] = make(map[int]map[int]map[int]bool)
						}
						if newCoords[x][y] == nil {
							newCoords[x][y] = make(map[int]map[int]bool)
						}
						if newCoords[x][y][z] == nil {
							newCoords[x][y][z] = make(map[int]bool)
						}
						newCoords[x][y][z][w] = state
					}
				}
			}
		}
		coords = newCoords
	}

	total := 0
	for _, yzVals := range coords {
		for _, zVals := range yzVals {
			for _, wVals := range zVals {
				for _, val := range wVals {
					if val {
						total += 1
					}
				}
			}
		}
	}
	return total
}
