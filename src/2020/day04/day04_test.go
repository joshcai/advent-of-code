package day04

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
	got := partOne(inputs)
	want := 2
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

func TestPartTwoExampleInvalid(t *testing.T) {
	inputs := ReadStrings("part2_invalid")
	got := partTwo(inputs)
	want := 0
	if got != want {
		t.Errorf("got %v, want %v", got, want)
	}
}

func TestPartTwoExampleValid(t *testing.T) {
	inputs := ReadStrings("part2_valid")
	got := partTwo(inputs)
	want := 4
	if got != want {
		t.Errorf("got %v, want %v", got, want)
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
