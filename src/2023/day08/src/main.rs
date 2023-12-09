use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn part1(input: &str) -> u64 {
    let mut hm: HashMap<String, (String, String)> = Default::default();
    let instructions = input.lines().next().unwrap();
    let matches = input.lines().skip(2)
        .map(|s| s
            .replace(" ", "")
            .replace("(", "")
            .replace(")", "")
            .replace(",", "="))
        .collect::<Vec<_>>();
    for m_str in matches.into_iter() {
        let m = m_str.split("=").collect::<Vec<_>>();
        hm.insert(m[0].to_string(), (m[1].to_string(), m[2].to_string()));
    }
    let mut curr = "AAA".to_string();
    let mut steps: u64 = 0;
    loop {
        for c in instructions.chars() {
            if curr == "ZZZ" {
                return steps;
            }
            steps += 1;
            let next = hm.get(&curr).unwrap();
            if c == 'L' {
                curr = (*next.0).to_string();
            } else {
                curr = (*next.1).to_string();
            }
        }
    }

}

fn gcd(a: u64, b: u64) -> u64 {
    if b == 0 {
        return a;
    }
    return gcd(b, a % b);
}

fn lcm(a: u64, b: u64) -> u64 {
    return a * b / gcd(a, b);
}

fn part2(input: &str) -> u64 {
    let mut hm: HashMap<String, (String, String)> = Default::default();
    let instructions = input.lines().next().unwrap();
    let matches = input.lines().skip(2)
        .map(|s| s
            .replace(" ", "")
            .replace("(", "")
            .replace(")", "")
            .replace(",", "="))
        .collect::<Vec<_>>();
    let mut curr: Vec<String> = Vec::new();
    for m_str in matches.into_iter() {
        let m = m_str.split("=").collect::<Vec<_>>();
        if m[0].ends_with("A") {
            curr.push(m[0].to_string());
        }
        hm.insert(m[0].to_string(), (m[1].to_string(), m[2].to_string()));
    }
    let f = |curr_step: String| {
        let mut curr = curr_step;
        let mut steps: u64 = 0;
        loop {
            for ins in instructions.chars() {
                if curr.ends_with('Z') {
                    return steps;
                }
                steps += 1;
                let next = hm.get(&curr).unwrap();
                if ins == 'L' {
                    curr = (*next.0).to_string();
                } else {
                    curr = (*next.1).to_string();
                }
            }
        }
    };
    curr.iter()
        .map(|x| f(x.clone()))
        .fold(1, |acc, num| lcm(acc, num))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example1.txt")), 2);
        assert_eq!(part1(include_str!("./example2.txt")), 6);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example3.txt")), 6);
        assert_eq!(part2(include_str!("./input.txt")), 21165830176709);
    } 
}
