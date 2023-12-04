use std::collections::HashSet;
use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn part1(input: &str) -> u32 {
    input
        .lines()
        .map(|s| s.split(':').last().unwrap())
        .map(|s| 
            s.split('|')
            .map(|s| s.split_whitespace().map(|s| s.parse::<u32>().unwrap()))
            .map::<HashSet<u32>, _>(HashSet::from_iter))
        .map(|hs| hs.fold(None, |acc: Option<HashSet<u32>>, x: HashSet<u32>| {
            Some(match acc {
                    None => x,
                    Some(a) => a.intersection(&x).cloned().collect(),
                })
            })
            .unwrap()
            .len())
        .filter(|x| x > &0)
        .map(|l| 2_u32.pow((l - 1).try_into().unwrap()))
        .sum::<u32>()
}

fn part2(input: &str) -> u32 {
    let nums: Vec<_> = input
        .lines()
        .map(|s| s.split(':').last().unwrap())
        .map(|s| 
            s.split('|')
            .map(|s| s.split_whitespace().map(|s| s.parse::<u32>().unwrap()))
            .map::<HashSet<u32>, _>(HashSet::from_iter))
        .map(|hs| hs.fold(None, |acc: Option<HashSet<u32>>, x: HashSet<u32>| {
            Some(match acc {
                    None => x,
                    Some(a) => a.intersection(&x).cloned().collect(),
                })
            })
            .unwrap()
            .len() as u32)
        .collect();
    let mut sum: u32 = 0;
    let mut hm: HashMap<u32, u32> = Default::default();
    for (i, val) in nums.iter().enumerate() {
        let idx: u32 = (i + 1).try_into().unwrap();
        let curr = match hm.get(&idx) {
            Some(count) => count,
            None => &0
        };
        let mult = curr + 1;
        sum += mult;
        for j in idx+1..idx+1+val {
            let curr_value = *hm.entry(j).or_insert(0);
            hm.insert(j, curr_value + mult);
        }
    }
    sum
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 13);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 30);
    } 
}