use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn hash(s: &str) -> u64 {
    s.chars()
    .fold(0, |acc, num| ((acc + num as u64) * 17) % 256)
}

fn part1(input: &str) -> u64 {
    input.split(',').map(hash).sum::<u64>()
}

fn part2(input: &str) -> u64 {
    let mut hm: HashMap<u64, Vec<(String, u64)>> = HashMap::new();
    for line in input.split(',') {
        if line.contains('=') {
            let split = line.split('=').collect::<Vec<_>>();
            let key = split[0];
            let val = split[1].parse::<u64>().unwrap();
            let hsh = hash(key);
            let v = hm.entry(hsh).or_insert(Vec::new());
            let mut idx: i32 = -1;
            let key_str = key.to_string();
            for (i, tup) in v.into_iter().enumerate() {
                if tup.0 == key_str {
                    idx = i as i32;
                }
            }
            if idx >= 0 {
                v[idx as usize] = (key.to_string(), val);
            } else {
                v.push((key.to_string(), val));
            }
        } else {
            let key = &line[0..line.len()-1];
            let hsh = hash(key);
            let v = hm.entry(hsh).or_insert(Vec::new());
            let mut idx: i32 = -1;
            let key_str = key.to_string();
            for (i, tup) in v.into_iter().enumerate() {
                if tup.0 == key_str {
                    idx = i as i32;
                }
            }
            if idx >= 0 {
                v.remove(idx as usize);
            }
        }
    }
    let mut total: u64 = 0;
    for (key, tups) in hm.into_iter() {
        for (j, tup) in tups.into_iter().enumerate() {
            total += (key + 1) * (j as u64 + 1) * tup.1;
        }
    }
    total
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(hash("rn=1"), 30);
        assert_eq!(hash("cm-"), 253);
        assert_eq!(part1(include_str!("./example.txt")), 1320);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 145);
    }
}
