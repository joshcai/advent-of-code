use std::collections::HashSet;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt"), 64));
    println!("Part 2: {}", part2(include_str!("./input.txt"), 26501365));
}

fn get_start(input_vec: &Vec<Vec<char>>) -> (isize, isize) {
     for (i, row) in input_vec.into_iter().enumerate() {
        for (j, c) in row.into_iter().enumerate() {
            if *c == 'S' {
                return (j as isize, i as isize);
            }
        }
    }   
    todo!();
}

fn get_next_hs(input_vec: &Vec<Vec<char>>, hs: &HashSet<(isize, isize)>) -> HashSet<(isize, isize)> {
    let mut next_hs = HashSet::new();
    let valid = |x, y| {
        if x < 0 || y < 0 || x >= input_vec[0].len() as isize || y >= input_vec.len() as isize {
            return false;
        }
        if input_vec[y as usize][x as usize] == '#' {
            return false;
        }
        true
    };
    for (x, y) in hs {
        for (dx, dy) in [(0, 1), (0, -1), (1, 0), (-1, 0)] {
            if valid(x + dx, y + dy) {
                next_hs.insert((x + dx, y + dy));
            }
        }
    }
    next_hs
}

fn get_next_hs2(input_vec: &Vec<Vec<char>>, hs: &HashSet<(isize, isize)>, size: isize) -> HashSet<(isize, isize)> {
    let mut next_hs = HashSet::new();
    let valid = |x, y| {
        let x_rem = ((x % size) + size) % size;
        let y_rem = ((y % size) + size) % size;
        !(input_vec[y_rem as usize][x_rem as usize] == '#')
    };
    for (x, y) in hs {
        for (dx, dy) in [(0, 1), (0, -1), (1, 0), (-1, 0)] {
            if valid(x + dx, y + dy) {
                next_hs.insert((x + dx, y + dy));
            }
        }
    }
    next_hs
}

fn part1(input: &str, steps: usize) -> usize {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let start = get_start(&input_vec);
    let mut curr_hs = HashSet::new();
    curr_hs.insert(start);

    for _step in 0..steps {
        curr_hs = get_next_hs(&input_vec, &curr_hs);      
    }
    curr_hs.len()
}

fn part2(input: &str, steps: usize) -> usize {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let start = get_start(&input_vec);
    let mut curr_hs = HashSet::new();
    let size = input_vec.len();
    let x = steps % size;
    curr_hs.insert(start);
    let mut steps_vec = Vec::new();
    let total_steps = x + 2 * size + 1;
    for _step in 0..total_steps {
        curr_hs = get_next_hs2(&input_vec, &curr_hs, size as isize);
        steps_vec.push(curr_hs.len());
    }
    // Based on https://www.reddit.com/r/adventofcode/comments/18nevo3/2023_day_21_solutions/keaiiq7/
    let a0 = steps_vec[x - 1];
    let a1 = steps_vec[x+size - 1];
    let a2 = steps_vec[x+2 * size - 1];
    let b0 = a0;
    let b1 = a1 - a0;
    let b2 = a2 - a1;
    let mult = steps / size;
    b0 + b1 * mult + (mult * (mult - 1) / 2) * (b2 - b1)
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt"), 6), 16);
    }
}
