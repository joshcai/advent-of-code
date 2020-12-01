package day01

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"testing"
)

func ReadInts(path string) []int {
	file, err := os.Open(path)
	if err != nil {
		log.Fatalf("Could not read file %s", path)
	}
	defer file.Close()

	var input []int
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		value, err := strconv.Atoi(scanner.Text())
		if err != nil {
			log.Fatal(err)
		}
		input = append(input, value)
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	return input
}

func TestPartOneExample(t *testing.T) {
	inputs := []int{
		1721,
		979,
		366,
		299,
		675,
		1456,
	}

	actual := partOne(inputs)
	expected := 514579

	if actual != expected {
		t.Errorf("got %d, want %d", actual, expected)
	}
}

func TestPartOne(t *testing.T) {
	inputs := ReadInts("input")
	actual := partOne(inputs)
	fmt.Printf("part 1: %d\n", actual)
}

func TestPartTwo(t *testing.T) {
	inputs := ReadInts("input")
	actual := partTwo(inputs)
	fmt.Printf("part 2: %d\n", actual)
}
