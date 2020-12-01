package day01

func partOne(input []int) int {
	m := make(map[int]bool)
	for _, num := range input {
		complement := 2020 - num
		if m[complement] {
			return complement * num
		}
		m[complement] = true
	}
	return -1
}