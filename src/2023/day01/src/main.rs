fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn part1(input: &str) -> u32 {
    input
        .lines()
        .map(String::from)
        .map(|s| 
            s.chars()
            .filter(|c| c.is_ascii_digit()).collect())
        .map(|v: Vec<char>| 
            v.first().unwrap().to_string() + &v.last().unwrap().to_string())
        .map(|s| s.parse::<u32>().unwrap())
        .sum::<u32>()
}

fn part2(input: &str) -> u32 {
    input
        .lines()
        .map(String::from)
        .map(|s| s.replace("one", "o1ne"))
        .map(|s| s.replace("two", "t2wo"))
        .map(|s| s.replace("three", "t3hree"))
        .map(|s| s.replace("four", "f4our"))
        .map(|s| s.replace("five", "f5ive"))
        .map(|s| s.replace("six", "s6ix"))
        .map(|s| s.replace("seven", "s7even"))
        .map(|s| s.replace("eight", "e8ight"))
        .map(|s| s.replace("nine", "n9ine"))
        .map(|s| 
            s.chars()
            .filter(|c| c.is_ascii_digit()).collect())
        .map(|v: Vec<char>| 
            v.first().unwrap().to_string() + &v.last().unwrap().to_string())
        .map(|s| s.parse::<u32>().unwrap())
        .sum::<u32>()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 142);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example2.txt")), 281);
    }
}