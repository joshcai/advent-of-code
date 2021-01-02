package day23

import (
	"fmt"
	"testing"
)

func TestPartOneExample(t *testing.T) {
	tests := []struct {
		steps int
		want  string
	}{
		{steps: 10, want: "92658374"},
		{steps: 100, want: "67384529"},
	}
	for _, tt := range tests {
		got := partOne("389125467", tt.steps)
		if got != tt.want {
			t.Errorf("%v: got %v, want %v", tt.steps, got, tt.want)
		}
	}
}

func TestPartTwoExample(t *testing.T) {
	got := partTwo("389125467")
	want := 149245887792
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

func TestPartOne(t *testing.T) {
	actual := partOne("318946572", 100)
	fmt.Printf("part 1: %v\n", actual)
}

func TestPartTwo(t *testing.T) {
	actual := partTwo("318946572")
	fmt.Printf("part 2: %d\n", actual)
}
