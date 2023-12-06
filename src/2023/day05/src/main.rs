fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn next(maps: &Vec<Vec<u64>>, num: u64) -> u64 {
    for map in maps {
        let dest = map[0];
        let src = map[1];
        let length = map[2];
        if num >= src && num < src + length {
            return dest + num - src;
        }
    }
    num
}

fn part1(input: &str) -> u64 {
    let mut nums: Vec<u64> = Vec::new();
    let mut maps: Vec<Vec<u64>> = Vec::new();
    for (i, line) in input.lines().chain(std::iter::once("")).enumerate() {
        if i == 0 {
            nums = line
                .split(":").last().unwrap()
                .split_whitespace()
                .map(|x| x.parse::<u64>().unwrap())
                .collect();
            continue;
        }
        if i == 1 {
            continue;
        }
        if line.contains("map") {
            continue;
        }
        if line.len() == 0 {
            nums = nums.iter().map(|x| next(&maps, *x)).collect();
            // Reset state for the next set of maps.
            maps = Vec::new();
            continue;
        }
        let curr = line.split_whitespace().map(|x| x.parse::<u64>().unwrap()).collect();
        maps.push(curr);
    }
    *nums.iter().min().unwrap()
}

fn part2(input: &str) -> u64 {
    let mut nums: Vec<Vec<u64>> = Vec::new();
    let mut maps: Vec<Vec<u64>> = Vec::new();
    for (i, line) in input.lines().chain(std::iter::once("")).enumerate() {
        if i == 0 {
            let temp_nums: Vec<Vec<u64>> = line
                .split(":").last().unwrap()
                .split_whitespace()
                .map(|x| x.parse::<u64>().unwrap())
                .collect::<Vec<_>>()
                .chunks(2)
                .map(|x| x.to_vec())
                .collect();
            for temp in temp_nums {
                nums.push(vec![temp[0], temp[0]+temp[1]]);
            }
            continue;
        }
        if i == 1 {
            continue;
        }
        if line.contains("map") {
            continue;
        }
        if line.len() == 0 {
            let mut next_nums: Vec<Vec<u64>> = Vec::new();
            let mut queue: Vec<Vec<u64>> = Vec::new();
            for temp in &nums {
                queue.push(temp.clone());
            }
            while queue.len() > 0 {
                let mut next_queue: Vec<Vec<u64>> = Vec::new();
                for temp in queue {
                    let a = temp[0];
                    let b = temp[1];
                    let mut seen = false;
                    for map in &maps {
                        let dest = map[0];
                        let src = map[1];
                        let length = map[2];
                        let end = src + length;
                        // Doesn't overlap
                        if b <= src || a >= end {
                            continue;
                        }
                        seen = true;
                        // Overlap on left side
                        if a < src {
                            next_queue.push(vec![a, src]);
                        }
                        // Intersection
                        next_nums.push(vec![dest + a.max(src) - src, dest + b.min(end) - src]);
                        // Overlap on right side
                        if b > end {
                            next_queue.push(vec![end, b]);
                        }
                        break;
                    }
                    if !seen {
                        next_nums.push(temp);
                    }
                }
                queue = next_queue;
            }
            nums = next_nums;
            // Reset state for the next set of maps.
            maps = Vec::new();
            continue;
        }
        let curr = line.split_whitespace().map(|x| x.parse::<u64>().unwrap()).collect();
        maps.push(curr);
    }
    nums.iter().map(|x| x[0]).min().unwrap()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 35);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 46);
    } 
}
