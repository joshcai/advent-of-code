use std::collections::HashMap;
use std::cmp;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

#[derive(Clone, Debug)]
struct Condition {
    name: char,
    op: char,
    value: u64,
    dest: String
}

struct Rule {
    conditions: Vec<Condition>,
    final_dest: String
}

fn get_rules(rules: &Vec<String>) -> HashMap<String, Rule> {
    let mut rules_map = HashMap::new();
    for rule in rules.into_iter() {
        let parts = rule.strip_suffix("}").unwrap().split('{').collect::<Vec<_>>();
        let mut conditions = Vec::new();
        let split_parts = parts[1].split(',').collect::<Vec<_>>();
        let split_len = split_parts.len();
        for cond in &split_parts[0..split_len-1] {
            let op = cond.chars().nth(1).unwrap();
            let name = cond.chars().nth(0).unwrap();
            let cond_parts = cond.split(&[':', '<', '>'][..]).collect::<Vec<_>>();
            // println!("{:?}", cond_parts);
            let value = cond_parts[1].parse::<u64>().unwrap();
            let dest = cond_parts[2];
            conditions.push(Condition { name, op, value, dest: dest.to_string() });
        }
        rules_map.insert(parts[0].to_string(), Rule {
            conditions: conditions,
            final_dest: split_parts[split_len-1].to_string(),
        });
    }
    rules_map
}
    
fn part1(input: &str) -> u64 {
    let mut instructions = true;
    let mut rules = Vec::new();
    let mut cases = Vec::new();
    for line in input.lines() {
        if line.len() == 0 {
            instructions = false;
            continue;
        }
        if instructions {
            rules.push(line.to_string());
            continue;
        } 
        cases.push(line.to_string());
    }
    let rules_map = get_rules(&rules);
    let mut sum = 0;
    for case in cases.into_iter() {
        let parts = case.strip_prefix("{").unwrap().strip_suffix("}").unwrap().split(',');
        let mut state = HashMap::new();
        let mut total = 0;
        for part in parts {
            let name = part.chars().nth(0).unwrap();
            let value = part[2..].parse::<u64>().unwrap();
            total += value;
            state.insert(name, value);
        }
        let mut dest = "in".to_string();
        while dest != "A" && dest != "R" {
            let rule = rules_map.get(&dest).unwrap();
            let mut changed = false;
            for cond in rule.conditions.clone().into_iter() {
                let curr_value = state.get(&cond.name).unwrap();
                match cond.op {
                    '<' => {
                        if curr_value < &cond.value {
                            changed = true;
                            dest = cond.dest;
                            break;
                        }
                    },
                    '>' => {
                        if curr_value > &cond.value {
                            changed = true;
                            dest = cond.dest;
                            break;
                        }
                    },
                    _ => todo!(),
                }
            }
            if !changed {
                dest = rule.final_dest.clone();
            }
        }
        if dest == "A" {
            sum += total;
        }
    }
    sum 
}

fn index(c: char) -> usize {
    match c {
        'x' => 0,
        'm' => 2,
        'a' => 4,
        's' => 6,
        _ => todo!(),
    }
}
 
fn part2(input: &str) -> u64 {
    let mut rules = Vec::new();
    for line in input.lines() {
        if line.len() == 0 {
            break;
        }
        rules.push(line.to_string());
    }
    let rules_map = get_rules(&rules);
    let mut sum = 0;
    fn recurse(s: [u64; 8], rules_map: &HashMap<String, Rule>, sum: &mut u64, dest: String) {
        if dest == "R" {
            return;
        }
        if dest == "A" {
            let v = (s[1] - s[0] + 1) * (s[3] - s[2] + 1) * (s[5] - s[4] + 1) * (s[7] - s[6] + 1);
            if v > 0 {
                *sum += v;
            }
            return;
        }
        let rule = rules_map.get(&dest).unwrap();
        let mut curr_s = s.clone();
        for cond in rule.conditions.clone().into_iter() {
            let idx = index(cond.name);
            let idx2 = idx + 1;
            match cond.op {
                '<' => {
                    if curr_s[idx] > cond.value {
                        return;
                    }
                    let mut next_s = curr_s.clone();
                    next_s[idx2] = cmp::min(cond.value - 1, curr_s[idx2]);
                    recurse(next_s.clone(), rules_map, sum, cond.dest.clone());
                    curr_s[idx] = cmp::max(cond.value, curr_s[idx]);
                },
                '>' => {
                    if curr_s[idx2] < cond.value {
                        return;
                    }
                    let mut next_s = curr_s.clone();
                    next_s[idx] = cmp::max(cond.value + 1, curr_s[idx]);
                    recurse(next_s.clone(), rules_map, sum, cond.dest.clone());
                    curr_s[idx2] = cmp::min(cond.value, curr_s[idx2]);
                },
                _ => todo!(),
            };
        }
        recurse(curr_s.clone(), rules_map, sum, rule.final_dest.clone());
    }
    // array for state of >x <x, >m <m, ...
    let state = [1, 4000, 1, 4000, 1, 4000, 1, 4000];
    recurse(state, &rules_map, &mut sum, "in".to_string());
    sum 
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 19114);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 167409079868000);
    }
}
