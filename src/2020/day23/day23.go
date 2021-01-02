package day23

import (
	"fmt"
	"log"
	"strconv"
)

func in(nums []int, target int) bool {
	for _, num := range nums {
		if num == target {
			return true
		}
	}
	return false
}

func nextNum(n, size int) int {
	n -= 1
	if n < 1 {
		return size
	}
	if n > size {
		return 1
	}
	return n
}

type node struct {
	val  int
	next *node
}

func rounds(head *node, m map[int]*node, steps int) {
	curr := head
	size := len(m)
	for i := 0; i < steps; i++ {
		hold := []*node{curr.next, curr.next.next, curr.next.next.next}
		holdNums := []int{hold[0].val, hold[1].val, hold[2].val}
		target := nextNum(curr.val, size)
		for in(holdNums, target) {
			target = nextNum(target, size)
		}
		targetNode := m[target]
		curr.next = hold[2].next
		hold[2].next = targetNode.next
		targetNode.next = hold[0]
		curr = curr.next
	}
}

func partOne(start string, steps int) string {
	var nums []int
	m := make(map[int]*node)
	var head *node
	var curr *node
	for _, c := range start {
		n, err := strconv.Atoi(string(c))
		if err != nil {
			log.Fatalf("could not convert to num: %v", c)
		}
		nums = append(nums, n)
		next := &node{val: n}
		if head == nil {
			head = next
		}
		if curr == nil {
			curr = next
		} else {
			curr.next = next
			curr = next
		}
		m[n] = curr
	}
	curr.next = head

	rounds(head, m, steps)

	startNode := m[1]
	final := ""
	curr = startNode.next
	for curr.val != 1 {
		final += fmt.Sprintf("%d", curr.val)
		curr = curr.next
	}
	return final
}

func partTwo(start string) int {
	var nums []int
	m := make(map[int]*node)
	var head *node
	var curr *node
	for _, c := range start {
		n, err := strconv.Atoi(string(c))
		if err != nil {
			log.Fatalf("could not convert to num: %v", c)
		}
		nums = append(nums, n)
		next := &node{val: n}
		if head == nil {
			head = next
		}
		if curr == nil {
			curr = next
		} else {
			curr.next = next
			curr = next
		}
		m[n] = curr
	}
	for i := 10; i <= 1000000; i++ {
		next := &node{val: i}
		curr.next = next
		curr = next
		m[i] = curr
	}
	curr.next = head
	curr = head

	rounds(head, m, 10000000)

	startNode := m[1]
	return startNode.next.val * startNode.next.next.val
}
