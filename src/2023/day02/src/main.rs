fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn part1(input: &str) -> u32 {
    let mut result: u32 = 0;
    for line in input.lines() {
        let parts: Vec<&str> = line.split(":").collect();
        let game = parts[0]
            .split_whitespace()
            .last().unwrap()
            .parse::<u32>().unwrap();
        let mut impossible = false;
        for game in parts[1].split(";") {
            let m = game
                .split(",")
                .map(|s| s.split_whitespace().collect())
                .map(|s: Vec<&str>| (s.last().unwrap().to_string(), s.first().unwrap().parse::<u32>().unwrap()))
                .collect::<std::collections::HashMap<_,_>>();
            if m.get("red").is_some() && m["red"] > 12 {
                impossible = true;
            }
            if m.get("green").is_some() && m["green"] > 13 {
                impossible = true;
            }
            if m.get("blue").is_some() && m["blue"] > 14 {
                impossible = true;
            }
        }
        if !impossible {
            result += game;
        }
    }
    result
}

fn part2(input: &str) -> u32 {
    let mut result: u32 = 0;
    for line in input.lines() {
        let parts: Vec<&str> = line.split(":").collect();
        let mut hm: std::collections::HashMap<String, u32> = Default::default();
        for game in parts[1].split(";") {
            let m = game
                .split(",")
                .map(|s| s.split_whitespace().collect())
                .map(|s: Vec<&str>| (s.last().unwrap().to_string(), s.first().unwrap().parse::<u32>().unwrap()))
                .collect::<std::collections::HashMap<_,_>>();
            for (key, value) in m.into_iter() {
                if !hm.contains_key(&key) {
                    hm.insert(key, value);
                    continue;
                }
                hm.insert(key.clone(), std::cmp::max(hm[&key], value));
            }
        }
        result += hm.values().fold(1, |acc, &x| acc * x);
    }
    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 8);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 2286);
    } 
}