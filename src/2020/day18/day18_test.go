package day18

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

func TestPartOneExample(t *testing.T) {
	tests := []struct {
		input string
		want  int
	}{
		{input: "1 + 2 * 3 + 4 * 5 + 6", want: 71},
		{input: "1 + (2 * 3) + (4 * (5 + 6))", want: 51},
		{input: "2 * 3 + (4 * 5)", want: 26},
		{input: "5 + (8 * 3 + 9 + 3 * 4 * 3)", want: 437},
		{input: "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", want: 12240},
		{input: "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", want: 13632},
	}
	for _, tt := range tests {
		got := solve(tt.input)
		if got != tt.want {
			t.Errorf("%q: got %v, want %v", tt.input, got, tt.want)
		}
	}
}

func TestPartTwoExample(t *testing.T) {
	tests := []struct {
		input string
		want  int
	}{
		{input: "1 + 2 * 3 + 4 * 5 + 6", want: 231},
		{input: "1 + (2 * 3) + (4 * (5 + 6))", want: 51},
		{input: "2 * 3 + (4 * 5)", want: 46},
		{input: "5 + (8 * 3 + 9 + 3 * 4 * 3)", want: 1445},
		{input: "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", want: 669060},
		{input: "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", want: 23340},
	}
	for _, tt := range tests {
		got := solve2(tt.input)
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
