package day21

import (
	"sort"
	"strings"
)

type rule struct {
	ingredients map[string]bool
	allergens   map[string]bool
}

func parseRules(lines []string) []rule {
	var rules []rule
	for _, line := range lines {
		tokens := strings.Split(line, " (")
		ingredients := strings.Split(tokens[0], " ")
		allergens := strings.Split(tokens[1][9:len(tokens[1])-1], ", ")
		im := make(map[string]bool)
		for _, ing := range ingredients {
			im[ing] = true
		}
		m := make(map[string]bool)
		for _, all := range allergens {
			m[all] = true
		}
		rules = append(rules, rule{ingredients: im, allergens: m})
	}
	return rules
}

func findImpossible(rules []rule) (map[string]int, map[string]bool) {
	seen := make(map[string]int)
	couldBe := make(map[string]bool)
	for i, r := range rules {
	ingloop:
		for ing := range r.ingredients {

			for a := range r.allergens {
				possible := true
				for j, rr := range rules {
					if i == j {
						continue
					}
					if rr.allergens[a] && !rr.ingredients[ing] {
						possible = false
						break
					}
				}
				if possible {
					couldBe[ing] = true
					continue ingloop
				}
			}
			seen[ing] += 1
		}
	}
	return seen, couldBe
}

func partOne(lines []string) int {
	rules := parseRules(lines)
	seen, couldBe := findImpossible(rules)
	total := 0
	for k, count := range seen {
		if couldBe[k] {
			continue
		}
		total += count
	}
	return total
}

func copyMap(a map[string]bool) map[string]bool {
	result := make(map[string]bool)
	for k, v := range a {
		result[k] = v
	}
	return result
}

func intersect(a, b map[string]bool) map[string]bool {
	if a == nil || b == nil {
		return nil
	}
	result := make(map[string]bool)
	for k := range a {
		if b[k] {
			result[k] = true
		}
	}
	return result
}

func partTwo(lines []string) string {
	rules := parseRules(lines)
	seen, couldBe := findImpossible(rules)
	final := make(map[string]bool)
	for k := range seen {
		if couldBe[k] {
			continue
		}
		final[k] = true
	}
	var filteredRules []*rule
	for _, r := range rules {
		newIngs := make(map[string]bool)
		for k := range r.ingredients {
			if final[k] {
				continue
			}
			newIngs[k] = true
		}
		filteredRules = append(filteredRules, &rule{ingredients: newIngs, allergens: r.allergens})
	}
	// allergen -> intersection of ingredients in all lines that contain the allergen
	allergyMap := make(map[string]map[string]bool)
	for _, r := range filteredRules {
		for allergy := range r.allergens {
			if val, ok := allergyMap[allergy]; !ok {
				allergyMap[allergy] = copyMap(r.ingredients)
			} else {
				allergyMap[allergy] = intersect(val, r.ingredients)
			}
		}
	}

	// allergen -> ingredient
	finalMap := make(map[string]string)
	var allergens []string
	for len(allergyMap) > 0 {
		var nextAllergen string
		var ingredient string
		for allergen, possible := range allergyMap {
			if len(possible) == 1 {
				nextAllergen = allergen
				for ing := range possible {
					ingredient = ing
				}
				break
			}
		}
		finalMap[nextAllergen] = ingredient
		allergens = append(allergens, nextAllergen)
		delete(allergyMap, nextAllergen)
		for allergen := range allergyMap {
			delete(allergyMap[allergen], ingredient)
		}
	}
	sort.Strings(allergens)
	var badIngs []string
	for _, a := range allergens {
		badIngs = append(badIngs, finalMap[a])
	}
	return strings.Join(badIngs, ",")
}
