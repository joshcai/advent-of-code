package day25

import (
	"fmt"
	"testing"
)

func TestPartOneExample(t *testing.T) {
	got := partOne(5764801, 17807724)
	want := 14897079
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

// func TestPartTwoExample(t *testing.T) {
// 	got := partTwo("389125467")
// 	want := 149245887792
// 	if got != want {
// 		t.Errorf("got %v, want %v", got, want)
// 	}
// }

func TestPartOne(t *testing.T) {
	actual := partOne(1717001, 523731)
	fmt.Printf("part 1: %v\n", actual)
}

// func TestPartTwo(t *testing.T) {
// 	actual := partTwo("318946572")
// 	fmt.Printf("part 2: %d\n", actual)
// }
