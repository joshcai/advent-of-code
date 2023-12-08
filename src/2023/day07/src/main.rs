use std::cmp::Ordering;
use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn char_rank(a: char) -> u8 {
    match a {
        'A' => 13,
        'K' => 12,
        'Q' => 11,
        'J' => 10,
        'T' => 9,
        '9' => 8,
        '8' => 7,
        '7' => 6,
        '6' => 5,
        '5' => 4,
        '4' => 3,
        '3' => 2,
        '2' => 1,
        _ => 0,
    }
}

fn rank(a: &str) -> u8 {
    let mut counter: HashMap<char, usize> = HashMap::new();
    for ch in a.chars() {
        *counter.entry(ch).or_insert(0) += 1;
    }
    
    let mut counter2: HashMap<usize, usize> = HashMap::new();
    for count in counter.values() {
        *counter2.entry(*count).or_insert(0) += 1
    }
    if counter2.get(&5).unwrap_or(&0) > &0 {
        return 7;
    }
    if counter2.get(&4).unwrap_or(&0) > &0 {
        return 6;
    }
    let three_count = counter2.get(&3).unwrap_or(&0);
    let two_count = counter2.get(&2).unwrap_or(&0);
    if  three_count > &0 && two_count > &0 {
        return 5;
    }
    if three_count > &0 {
        return 4;
    }
    if two_count > &1 {
        return 3;
    }
    if two_count > &0 {
        return 2;
    }
    1
}

fn order(a: &str, b: &str) -> Ordering {
    if a == b {
        return Ordering::Equal;
    }
    let rank_a = rank(a);
    let rank_b = rank(b);
    if rank_a > rank_b {
        return Ordering::Greater;
    }
    if rank_b > rank_a {
        return Ordering::Less;
    }
    for (c, d) in a.chars().zip(b.chars()) {
        if c == d {
            continue;
        }
        let c_rank = char_rank(c);
        let d_rank = char_rank(d);
        return c_rank.cmp(&d_rank);
    }
    Ordering::Equal
}

fn part1(input: &str) -> u64 {
    let mut results = input
        .lines()
        .map(|s| s.split_whitespace())
        .map(|mut s| (s.next().unwrap(), s.last().unwrap().parse::<u64>().unwrap()))
        .collect::<Vec<_>>();
    results.sort_by(|a, b| order(a.0, b.0));
    let mut sum: u64 = 0;
    for (i, (_, val))in results.into_iter().enumerate() {
        sum += (i+1) as u64 * val;
    }
    sum
}

fn char_rank2(a: char) -> u8 {
    match a {
        'A' => 13,
        'K' => 12,
        'Q' => 11,
        'T' => 9,
        '9' => 8,
        '8' => 7,
        '7' => 6,
        '6' => 5,
        '5' => 4,
        '4' => 3,
        '3' => 2,
        '2' => 1,
        'J' => 0,
        _ => 0,
    }
}

fn rank2(a: &str) -> u8 {
    let mut counter: HashMap<char, usize> = HashMap::new();
    for ch in a.chars() {
        *counter.entry(ch).or_insert(0) += 1;
    }
    
    let j = counter.get(&'J').unwrap_or(&0).clone();
    counter.remove(&'J');
    let mut counter2: HashMap<usize, usize> = HashMap::new();
    for count in counter.values() {
        *counter2.entry(*count).or_insert(0) += 1
    }
    let five_count = counter2.get(&5).unwrap_or(&0);
    let four_count = counter2.get(&4).unwrap_or(&0);
    let three_count = counter2.get(&3).unwrap_or(&0);
    let two_count = counter2.get(&2).unwrap_or(&0);
    match j {
        5 => return 7,
        4 => return 7,
        3 => {
            if two_count > &0 {
                return 7;
            }
            return 6;
        },
        2 => {
            if three_count > &0 {
                return 7;
            }
            if two_count > &0 {
                return 6;
            }
            return 4;
        },
        1 => {
            if four_count > &0 {
                return 7;
            }
            if three_count > &0 {
                return 6;
            }
            if two_count > &1 {
                return 5;
            }
            if two_count > &0 {
                return 4;
            }
            return 2;
        },
        0 => {
            if five_count > &0 {
                return 7;
            }
            if four_count > &0 {
                return 6;
            }
            if  three_count > &0 && two_count > &0 {
                return 5;
            }
            if three_count > &0 {
                return 4;
            }
            if two_count > &1 {
                return 3;
            }
            if two_count > &0 {
                return 2;
            }
            return 1;
        },
        _ => 0,
    }

}

fn order2(a: &str, b: &str) -> Ordering {
    if a == b {
        return Ordering::Equal;
    }
    let rank_a = rank2(a);
    let rank_b = rank2(b);
    if rank_a > rank_b {
        return Ordering::Greater;
    }
    if rank_b > rank_a {
        return Ordering::Less;
    }
    for (c, d) in a.chars().zip(b.chars()) {
        if c == d {
            continue;
        }
        let c_rank = char_rank2(c);
        let d_rank = char_rank2(d);
        return c_rank.cmp(&d_rank);
    }
    Ordering::Equal
}

fn part2(input: &str) -> u64 {
    let mut results = input
        .lines()
        .map(|s| s.split_whitespace())
        .map(|mut s| (s.next().unwrap(), s.last().unwrap().parse::<u64>().unwrap()))
        .collect::<Vec<_>>();
    results.sort_by(|a, b| order2(a.0, b.0));
    let mut sum: u64 = 0;
    for (i, (_, val))in results.into_iter().enumerate() {
        sum += (i+1) as u64 * val;
    }
    sum
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 6440);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 5905);
    } 
}
