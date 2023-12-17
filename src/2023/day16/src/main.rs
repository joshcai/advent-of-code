use std::collections::HashSet;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn get_dir_tup(dir: char) -> (i32, i32) {
    match dir {
        'R' => (1, 0),
        'L' => (-1, 0),
        'U' => (0, -1),
        'D' => (0, 1),
        _ => todo!(),
    }
}

fn get_energized(x: i32, y: i32, dir: char, input_vec: &Vec<Vec<char>>) -> u64 {
    let mut seen: HashSet<((u64, u64), char)> = HashSet::new();
    // dirs = 'R', 'L', 'U', 'D'
    fn visit(x: i32, y: i32, dir: char, input_vec: &Vec<Vec<char>>, seen: &mut HashSet<((u64, u64), char)>,) {
        let tup = ((x as u64, y as u64), dir);
        if x < 0 || x as usize >= input_vec[0].len() {
            return;
        }
        if y < 0 || y as usize >= input_vec.len() {
            return;
        }
        if seen.contains(&tup) {
            return;
        }
        seen.insert(tup);
        let dir_tup = get_dir_tup(dir);
        match input_vec[y as usize][x as usize] {
            '.' => { visit(x + dir_tup.0, y + dir_tup.1, dir, input_vec, seen); },
            '|' => {
                match dir {
                    'U' | 'D' => { visit(x + dir_tup.0, y + dir_tup.1, dir, input_vec, seen); },
                    'L' | 'R' =>  {
                        visit(x, y - 1, 'U', input_vec, seen);
                        visit(x, y + 1, 'D', input_vec, seen);
                    },
                    _ => todo!(),
                }
            },
            '-' => {
                match dir {
                    'L' | 'R' => { visit(x + dir_tup.0, y + dir_tup.1, dir, input_vec, seen); },
                    'U' | 'D' =>  {
                        visit(x - 1, y, 'L', input_vec, seen);
                        visit(x + 1, y, 'R', input_vec, seen);
                    },
                    _ => todo!(),
                }
            },
            '/' => {
                let next_dir = match dir {
                    'R' => 'U',
                    'D' => 'L',
                    'L' => 'D',
                    'U' => 'R',
                    _ => todo!(),
                };
                let dt = get_dir_tup(next_dir);
                visit(x + dt.0, y + dt.1, next_dir, input_vec, seen);
            },
            '\\' => {
                let next_dir = match dir {
                    'R' => 'D',
                    'D' => 'R',
                    'L' => 'U',
                    'U' => 'L',
                    _ => todo!(),
                };
                let dt = get_dir_tup(next_dir);
                visit(x + dt.0, y + dt.1, next_dir, input_vec, seen);
            },
            _ => todo!(),
        }
    }
    visit(x, y, dir, &input_vec, &mut seen);
    let mut seen_coords: HashSet<(u64, u64)> = HashSet::new();
    seen.into_iter().for_each(|tup| { seen_coords.insert(tup.0); });
    seen_coords.len() as u64
}


fn part1(input: &str) -> u64 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    get_energized(0, 0, 'R', &input_vec)
}

fn part2(input: &str) -> u64 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let mut curr_max: u64 = 0;
    for i in 0..input_vec.len() {
        let mut temp = get_energized(0, i as i32, 'R', &input_vec);
        curr_max = temp.max(curr_max);
        temp = get_energized(input_vec[0].len() as i32 - 1, i as i32, 'L', &input_vec);
        curr_max = temp.max(curr_max);
    }
    for i in 0..input_vec[0].len() {
        let mut temp = get_energized(i as i32, 0, 'D', &input_vec);
        curr_max = temp.max(curr_max);
        temp = get_energized(i as i32, input_vec.len() as i32 - 1, 'L', &input_vec);
        curr_max = temp.max(curr_max);
    }
    curr_max
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 46);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 51);
    }
}
