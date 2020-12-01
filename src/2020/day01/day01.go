package day01

func partOne(input []int) int {
	m := make(map[int]bool)
	for _, num := range input {
		complement := 2020 - num
		if m[complement] {
			return complement * num
		}
		m[num] = true
	}
	return -1
}

func twoSum(input []int, target int) []int {
	 m := make(map[int]bool)
	 for _, num := range input {
		 complement := target - num
		 if m[complement] {
			 return []int{num, complement}
		 }
		 m[num] = true
	 }
	 return nil
}

func partTwo(input []int) int {
	for i, num := range input {
		complement := 2020 - num
		twos := twoSum(input[i+1:], complement)
		if twos != nil {
			final := num
			for _, n := range twos {
				final *= n
			}
			return final
		}
	}
	return -1
}