package day09

import (
	"log"
	"strconv"
)

func partOne(lines []string, preamble int) int {
	var nums []int
	for _, line := range lines {
		num, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalf("invalid number: %v", num)
		}
		nums = append(nums, num)
	}
	m := make(map[int]int)
	for i := 0; i < preamble; i++ {
		for j := i + 1; j < preamble; j++ {
			m[nums[i]+nums[j]] += 1
		}
	}
	for i := preamble; i < len(nums); i++ {
		num := nums[i]
		if m[num] == 0 {
			return num
		}
		old := nums[i-preamble]
		for j := i - preamble + 1; j < i; j++ {
			m[old+nums[j]] -= 1
			m[num+nums[j]] += 1
		}
	}
	return -1
}

func partTwo(lines []string, invalid int) int {
	var nums []int
	sum := 0
	prefix := []int{0}
	for _, line := range lines {
		num, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalf("invalid number: %v", num)
		}
		nums = append(nums, num)
		sum += num
		prefix = append(prefix, sum)
	}
	for i := 0; i < len(prefix); i++ {
		for j := i + 1; j < len(prefix); j++ {
			if prefix[j]-prefix[i] == invalid {
				min := -1
				max := -1
				for k := i; k < j; k++ {
					n := nums[k]
					if min == -1 || n < min {
						min = n
					}
					if n > max {
						max = n
					}
				}
				return min + max
			}
		}
	}
	return -1
}
