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

func TestPartOne(t *testing.T) {
	actual := partOne([]int{8, 11, 0, 19, 1, 2})
	fmt.Printf("part 1: %d\n", actual)
}

func TestPartTwo(t *testing.T) {
	actual := partTwo([]int{8, 11, 0, 19, 1, 2})
	fmt.Printf("part 2: %d\n", actual)
}
