package day15

import (
	"fmt"
	"testing"
)

func TestPartOneExample(t *testing.T) {
	got := partOne([]int{0, 3, 6})
	want := 436
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

// func TestPartTwoExample(t *testing.T) {
// 	tests := []struct {
// 		input []int
// 		want  int
// 	}{
// 		{input: "1-3 a: abcde", want: true},
// 		{input: "1-3 b: cdefg", want: false},
// 		{input: "2-9 c: ccccccccc", want: false},
// 	}
// 	for _, tt := range tests {
// 		low, high, c, s := parseLine(tt.input)
// 		got := valid2(low, high, c, s)
// 		if got != tt.want {
// 			t.Errorf("%q: got %v, want %v", tt.input, got, tt.want)
// 		}
// 	}
// }

func TestPartOne(t *testing.T) {
	actual := partOne([]int{8, 11, 0, 19, 1, 2})
	fmt.Printf("part 1: %d\n", actual)
}

func TestPartTwo(t *testing.T) {
	actual := partTwo([]int{8, 11, 0, 19, 1, 2})
	fmt.Printf("part 2: %d\n", actual)
}
