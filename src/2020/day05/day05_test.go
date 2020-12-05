package day05

import (
	"fmt"
	"io/ioutil"
	"log"
	"strings"
	"testing"
)

func ReadStrings(path string) []string {
	content, err := ioutil.ReadFile(path)
	if err != nil {
		log.Fatalf("Could not read file %s", path)
	}
	return strings.Split(string(content), "\n")
}

func TestSeatID(t *testing.T) {
	tests := []struct {
		input string
		want  int
	}{
		{input: "BFFFBBFRRR", want: 567},
		{input: "FFFBBBFRRR", want: 119},
		{input: "BBFFBBFRLL", want: 820},
	}
	for _, tt := range tests {
		got := seatID(tt.input)
		if got != tt.want {
			t.Errorf("%q: got %v, want %v", tt.input, got, tt.want)
		}
	}
}

func TestPartOne(t *testing.T) {
	inputs := ReadStrings("input")

	actual := partOne(inputs)
	fmt.Printf("part 1: %d\n", actual)
}

func TestPartTwo(t *testing.T) {
	inputs := ReadStrings("input")

	actual := partTwo(inputs)
	fmt.Printf("part 2: %d\n", actual)
}
