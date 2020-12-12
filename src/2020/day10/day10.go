package day10

import (
	"log"
	"sort"
	"strconv"
)

func partOne(lines []string) int {
	nums := []int{0}
	max := 0
	for _, line := range lines {
		num, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalf("invalid number: %v", num)
		}
		nums = append(nums, num)
		if num > max {
			max = num
		}
	}
	nums = append(nums, max+3)
	sort.Ints(nums)
	diffs := make(map[int]int)
	for i := 1; i < len(nums); i++ {
		diffs[nums[i]-nums[i-1]] += 1
	}
	return diffs[3] * diffs[1]
}

func partTwo(lines []string) int {
	m := make(map[int]bool)
	max := 0
	for _, line := range lines {
		num, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalf("invalid number: %v", num)
		}
		m[num] = true
		if num > max {
			max = num
		}
	}
	m[0] = true
	count := make(map[int]int)
	count[max] = 1
	var dfs func(curr int) int
	dfs = func(curr int) int {
		if !m[curr] {
			return 0
		}
		if val, ok := count[curr]; ok {
			return val
		}
		total := 0
		for i := 1; i <= 3; i++ {
			total += dfs(curr + i)
		}
		count[curr] = total
		return total
	}
	return dfs(0)
}
