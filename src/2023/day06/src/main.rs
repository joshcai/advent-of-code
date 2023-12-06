fn main() {
    let part1 = num_ways(56, 546) * num_ways(97, 1927) * num_ways(78, 1131) * num_ways(75, 1139);
    println!("Part 1: {}", part1);
    let part2 = num_ways(56977875, 546192711311139);
    println!("Part 2: {}", part2);
}

fn num_ways(t: u64, d: u64) -> u64 {
    (0..=t)
        .map(|x| (t-x) * x)
        .filter(|x| x > &d)
        .map(|_x| 1)
        .sum::<u64>()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn num_ways_examples() {
        assert_eq!(num_ways(7, 9), 4);
        assert_eq!(num_ways(15, 40), 8);
        assert_eq!(num_ways(30, 200), 9);
    }
}
