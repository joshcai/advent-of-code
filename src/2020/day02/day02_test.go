package day02

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
		want  bool
	}{
		{input: "1-3 a: abcde", want: true},
		{input: "1-3 b: cdefg", want: false},
		{input: "2-9 c: ccccccccc", want: true},
	}
	for _, tt := range tests {
		low, high, c, s := parseLine(tt.input)
		got := valid(low, high, c, s)
		if got != tt.want {
			t.Errorf("%q: got %v, want %v", tt.input, got, tt.want)
		}
	}
}

func TestPartTwoExample(t *testing.T) {
	tests := []struct {
		input string
		want  bool
	}{
		{input: "1-3 a: abcde", want: true},
		{input: "1-3 b: cdefg", want: false},
		{input: "2-9 c: ccccccccc", want: false},
	}
	for _, tt := range tests {
		low, high, c, s := parseLine(tt.input)
		got := valid2(low, high, c, s)
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
