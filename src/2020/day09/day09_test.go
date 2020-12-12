package day09

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
	inputs := ReadStrings("example_input")
	got := partOne(inputs, 5)
	want := 127
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

func TestPartTwoExample(t *testing.T) {
	inputs := ReadStrings("example_input")
	got := partTwo(inputs, 127)
	want := 62
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

func TestPartOne(t *testing.T) {
	inputs := ReadStrings("input")

	actual := partOne(inputs, 25)
	fmt.Printf("part 1: %d\n", actual)
}

func TestPartTwo(t *testing.T) {
	inputs := ReadStrings("input")

	actual := partTwo(inputs, 258585477)
	fmt.Printf("part 2: %d\n", actual)
}
