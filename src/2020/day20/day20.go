package day19

import (
	"log"
	"strconv"
	"strings"
)

func reverse(s string) string {
	rns := []rune(s)
	for i, j := 0, len(rns)-1; i < j; i, j = i+1, j-1 {

		rns[i], rns[j] = rns[j], rns[i]
	}
	return string(rns)
}

type tile struct {
	top, bottom, left, right string
	data                     []string
	id                       int
}

func createTile(data []string, id int) *tile {
	t := &tile{
		data: data,
		id:   id,
	}
	t.setAttributes()
	return t
}

func (t *tile) setAttributes() {
	buffer := t.data
	t.top = buffer[0]
	t.bottom = buffer[len(buffer)-1]
	var left, right string
	for _, l := range buffer {
		left += string(l[0])
		right += string(l[len(l)-1])
	}
	t.left = left
	t.right = right
}

func (t *tile) lines() []string {
	return []string{t.top, t.bottom, t.left, t.right}
}

// tile data without borders
func (t *tile) actualData() []string {
	var result []string
	for i, line := range t.data {
		if i == 0 || i == len(t.data)-1 {
			continue
		}
		result = append(result, line[1:len(line)-1])
	}
	return result
}

func (t *tile) flip() {
	var result []string
	for i := len(t.data) - 1; i >= 0; i-- {
		result = append(result, t.data[i])
	}
	t.data = result
	t.setAttributes()
}

func (t *tile) rotate() {
	var result []string
	for i := range t.data {
		var s string
		for j := 0; j < len(t.data); j++ {
			s += string(t.data[j][i])
		}
		result = append(result, reverse(s))
	}
	t.data = result
	t.setAttributes()
}

func partOne(lines []string) int {
	var buffer []string
	curr := 0
	var tiles []*tile
	tileMap := make(map[string][]*tile)
	// Parse data
	for _, line := range append(lines, "") {
		if line == "" {
			t := createTile(buffer, curr)
			for _, bline := range t.lines() {
				reversed := reverse(bline)
				tileMap[bline] = append(tileMap[bline], t)
				tileMap[reversed] = append(tileMap[reversed], t)
			}
			tiles = append(tiles, t)
			curr = 0
			buffer = []string{}
			continue
		}
		if strings.HasPrefix(line, "Tile ") {
			num, err := strconv.Atoi(line[5:9])
			if err != nil {
				log.Fatalf("invalid number: %s", line[5:9])
			}
			curr = num
			continue
		}
		buffer = append(buffer, line)
	}

	// Find all corners
	num := 1
	for _, t := range tiles {
		curr := 0
		for _, l := range t.lines() {
			// Has no match
			if len(tileMap[l]) == 1 {
				curr += 1
			}
		}
		// Only corners have 2 non-matches out of its 4 edges
		if curr == 2 {
			num *= t.id
		}
	}
	return num
}

var MONSTER_COORDS = [][]int{
	[]int{1, 1},
	[]int{4, 1},
	[]int{5, 0},
	[]int{6, 0},
	[]int{7, 1},
	[]int{10, 1},
	[]int{11, 0},
	[]int{12, 0},
	[]int{13, 1},
	[]int{16, 1},
	[]int{17, 0},
	[]int{18, 0},
	[]int{18, -1},
	[]int{19, 0},
}

// Returns non-zero if it found any monsters.
func (t *tile) getMonsters() int {
	get := func(i, j int) bool {
		if i < 0 || i >= len(t.data) {
			return false
		}
		if j < 0 || j >= len(t.data[0]) {
			return false
		}
		return t.data[i][j] == '#'
	}

	isMonster := func(i, j int) bool {
		for _, coords := range MONSTER_COORDS {
			x, y := coords[0], coords[1]
			if !get(i+y, j+x) {
				return false
			}
		}
		return true
	}

	totalCount := 0
	numMonsters := 0
	for i, row := range t.data {
		for j, cell := range row {
			if cell != '#' {
				continue
			}
			totalCount += 1
			if isMonster(i, j) {
				numMonsters += 1
			}
		}
	}
	if numMonsters == 0 {
		return 0
	}
	return totalCount - 15*numMonsters
}

// Tries to rotate / flip until the condition returns true.
func rotateUntil(currTile *tile, conditionFn func(currTile *tile) bool) {
	for i := 0; i < 4; i++ {
		if conditionFn(currTile) {
			return
		}
		currTile.rotate()
	}
	currTile.flip()
	for i := 0; i < 4; i++ {
		if conditionFn(currTile) {
			return
		}
		currTile.rotate()
	}
	log.Fatalf("couldn't fit piece, id: %d ", currTile.id)
}

func getOtherTile(currTile *tile, tileMap map[string][]*tile, line string) *tile {
	for _, t := range tileMap[line] {
		if t != currTile {
			return t
		}
	}
	return nil
}

func stitch(startTile *tile, tileMap map[string][]*tile, numTiles int) [][]*tile {
	var tiles [][]*tile
	row := 0
	col := 1
	currTile := startTile
	tiles = append(tiles, []*tile{currTile})
	// Rotate the starting piece into the right position.
	rotateUntil(currTile, func(currTile *tile) bool {
		return len(tileMap[currTile.top]) == 1 && len(tileMap[currTile.left]) == 1
	})

	for counter := 1; counter < numTiles; counter += 1 {
		nextTiles := tileMap[currTile.right]

		// Case where the last piece was the right edge. We want to start a new row instead.
		if len(nextTiles) == 1 {
			topTile := tiles[row][0]
			nextTile := getOtherTile(topTile, tileMap, topTile.bottom)
			// Rotating until the left is empty and the top matches the previous row's piece.
			rotateUntil(nextTile, func(nextTile *tile) bool {
				return topTile.bottom == nextTile.top && len(tileMap[nextTile.left]) == 1
			})
			currTile = nextTile
			tiles = append(tiles, []*tile{nextTile})
			row += 1
			col = 1
			continue
		}

		if len(nextTiles) != 2 {
			log.Fatalf("not valid number of tiles: %d", len(nextTiles))
		}

		nextTile := getOtherTile(currTile, tileMap, currTile.right)
		tiles[row] = append(tiles[row], nextTile)

		if row == 0 {
			// Rotating until left matches the current tile's right and the top is empty.
			rotateUntil(nextTile, func(nextTile *tile) bool {
				return currTile.right == nextTile.left && len(tileMap[nextTile.top]) == 1
			})
		} else {
			topMatch := tiles[row-1][col].bottom
			// Rotating until the left matches the current tile's right and the previous row's bottom.
			rotateUntil(nextTile, func(nextTile *tile) bool {
				return currTile.right == nextTile.left && topMatch == nextTile.top
			})
		}
		currTile = nextTile
		col += 1
	}
	return tiles
}

func findMonsters(tiles [][]*tile) int {
	var megaTileData []string
	for _, row := range tiles {
		var rowData []string
		for _, cell := range row {
			if len(rowData) == 0 {
				rowData = cell.actualData()
				continue
			}
			for i, line := range cell.actualData() {
				rowData[i] += line
			}
		}
		for _, finalLine := range rowData {
			megaTileData = append(megaTileData, finalLine)
		}
	}

	megaTile := createTile(megaTileData, -1)
	for i := 0; i < 4; i++ {
		if v := megaTile.getMonsters(); v > 0 {
			return v
		}
		megaTile.rotate()
	}
	megaTile.flip()
	for i := 0; i < 4; i++ {
		if v := megaTile.getMonsters(); v > 0 {
			return v
		}
		megaTile.rotate()
	}

	return -1
}

func partTwo(lines []string) int {
	var buffer []string
	curr := 0
	tileMap := make(map[string][]*tile)
	numTiles := 0
	mNums := make(map[int]*tile)
	for _, line := range append(lines, "") {
		if line == "" {
			tile := createTile(buffer, curr)
			for _, bline := range tile.lines() {
				reversed := reverse(bline)
				tileMap[bline] = append(tileMap[bline], tile)
				tileMap[reversed] = append(tileMap[reversed], tile)
			}
			mNums[curr] = tile
			curr = 0
			buffer = []string{}
			continue
		}
		if strings.HasPrefix(line, "Tile ") {
			numTiles += 1
			num, err := strconv.Atoi(line[5:9])
			if err != nil {
				log.Fatalf("invalid number: %s", line[5:9])
			}
			curr = num
			continue
		}
		buffer = append(buffer, line)
	}
	// Get starting tile
	start := 0
	for n, tile := range mNums {
		curr := 0
		for _, bline := range tile.lines() {
			if len(tileMap[bline]) == 1 {
				curr += 1
			}
		}
		if curr == 2 {
			start = n
			break
		}
	}

	tiles := stitch(mNums[start], tileMap, numTiles)
	return findMonsters(tiles)
}
