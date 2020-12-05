package day04

import (
	"regexp"
	"strconv"
	"strings"
	"unicode"
)

func split(ss []string) [][]string {
	ss = append(ss, "")
	var final [][]string
	var curr []string
	for _, s := range ss {
		if s == "" {
			final = append(final, curr)
			curr = nil
			continue
		}
		curr = append(curr, strings.Split(s, " ")...)
	}
	return final
}

func partOne(ss []string) int {
	passports := split(ss)
	required := []string{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"}
	count := 0
	for _, passport := range passports {
		m := make(map[string]string)
		for _, token := range passport {
			split := strings.Split(token, ":")
			m[split[0]] = split[1]
		}
		bad := false
		for _, req := range required {
			if _, ok := m[req]; !ok {
				bad = true
				break
			}
		}
		if !bad {
			count += 1
		}
	}
	return count
}

func partTwo(ss []string) int {
	passports := split(ss)
	required := make(map[string]func(val string) bool)

	eyeColors := make(map[string]bool)
	for _, color := range []string{"amb", "blu", "brn", "gry", "grn", "hzl", "oth"} {
		eyeColors[color] = true
	}

	var hairColorRegex = regexp.MustCompile(`#[a-f0-9]{6}`)
	required["byr"] = func(val string) bool {
		num, err := strconv.Atoi(val)
		if err != nil {
			return false
		}
		return num >= 1920 && num <= 2002
	}
	required["iyr"] = func(val string) bool {
		num, err := strconv.Atoi(val)
		if err != nil {
			return false
		}
		return num >= 2010 && num <= 2020
	}
	required["eyr"] = func(val string) bool {
		num, err := strconv.Atoi(val)
		if err != nil {
			return false
		}
		return num >= 2020 && num <= 2030
	}
	required["hgt"] = func(val string) bool {
		if val[len(val)-2:] == "cm" {
			num, err := strconv.Atoi(val[:len(val)-2])
			if err != nil {
				return false
			}
			return num >= 150 && num <= 193
		}

		if val[len(val)-2:] == "in" {
			num, err := strconv.Atoi(val[:len(val)-2])
			if err != nil {
				return false
			}
			return num >= 59 && num <= 76
		}

		return false
	}
	required["hcl"] = func(val string) bool {
		return hairColorRegex.MatchString(val)
	}
	required["ecl"] = func(val string) bool {
		return eyeColors[val]
	}
	required["pid"] = func(val string) bool {
		if len(val) != 9 {
			return false
		}
		for _, r := range val {
			if !unicode.IsDigit(r) {
				return false
			}
		}
		return true
	}

	count := 0
	for _, passport := range passports {
		m := make(map[string]string)
		for _, token := range passport {
			split := strings.Split(token, ":")
			m[split[0]] = split[1]
		}
		bad := false
		for field, valid := range required {
			val, ok := m[field]
			if !ok || !valid(val) {
				bad = true
				break
			}
		}
		if !bad {
			count += 1
		}
	}
	return count
}
