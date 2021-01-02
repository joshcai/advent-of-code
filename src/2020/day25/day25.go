package day25

func partOne(key1, key2 int) int {
	subj := 1
	loop := 0
	for subj != key1 {
		subj *= 7
		subj %= 20201227
		loop += 1
	}

	subj = 1
	for i := 0; i < loop; i++ {
		subj *= key2
		subj %= 20201227
	}
	return subj
}
