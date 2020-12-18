package day15

func solve(nums []int, lastTurn int) int {
	m := make(map[int]int)
	for i, num := range nums {
		m[num] = i + 1
	}
	prev := make(map[int]int)
	curr := nums[len(nums)-1]
	for turn := len(nums) + 1; turn <= lastTurn; turn += 1 {
		v, ok := prev[curr]
		if !ok {
			curr = 0
		} else {
			curr = m[curr] - v
		}
		if prevTurn, ok := m[curr]; !ok {
			m[curr] = turn
		} else {
			prev[curr] = prevTurn
			m[curr] = turn
		}
	}
	return curr
}

func partOne(nums []int) int {
	return solve(nums, 2020)
}

func partTwo(nums []int) int {
	return solve(nums, 30000000)
}
