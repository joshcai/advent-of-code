
fn main() {
    let p1 = include_str!("./input.txt").lines()
        .map(part1)
        .sum::<i32>();
    println!("Part 1: {}", p1);
    let p2 = include_str!("./input.txt").lines()
        .map(part2)
        .sum::<i32>();
    println!("Part 2: {}", p2);
}

fn part1(input: &str) -> i32 {
    let mut curr = input.split_whitespace()
        .map(|s| s.parse::<i32>().unwrap())
        .collect::<Vec<_>>();
    let mut last: Vec<i32> = Vec::new();
    let mut next_curr: Vec<i32 >= Vec::new();
    loop {
        next_curr.clear();
        if curr.iter().all(|x| *x == 0) {
            break;
        }
        last.push(curr.last().copied().unwrap());
        for w in curr.windows(2) {
            next_curr.push(w[1] - w[0]);
        }
        curr = next_curr.clone();
    }
    last.iter().sum::<i32>()
}

fn part2(input: &str) -> i32 {
    let mut curr = input.split_whitespace()
        .map(|s| s.parse::<i32>().unwrap())
        .collect::<Vec<_>>();
    let mut first: Vec<i32> = Vec::new();
    let mut next_curr: Vec<i32 >= Vec::new();
    loop {
        next_curr.clear();
        if curr.iter().all(|x| *x == 0) {
            break;
        }
        first.push(curr.iter().next().copied().unwrap());
        for w in curr.windows(2) {
            next_curr.push(w[1] - w[0]);
        }
        curr = next_curr.clone();
    }
    first.iter().rev().fold(0, |acc, num| num - acc)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1("0   3   6   9  12  15"), 18);
        assert_eq!(part1("1   3   6  10  15  21"), 28);
        assert_eq!(part1("10  13  16  21  30  45"), 68);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2("0   3   6   9  12  15"), -3);
        assert_eq!(part2("1   3   6  10  15  21"), 0);
        assert_eq!(part2("10  13  16  21  30  45"), 5);
    }
}
