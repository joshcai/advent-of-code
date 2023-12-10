
use std::collections::HashSet;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt"), '|'));
    println!("Part 2: {}", part2(include_str!("./input.txt"), '|'));
}

fn part1(input: &str, start_pipe: char) -> u32 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let mut start: (i32, i32) = (0, 0);
    for (y, line) in input_vec.clone().into_iter().enumerate() {
        for (x, c) in line.into_iter().enumerate() {
            if c == 'S' {
                start = (x as i32, y as i32);
            }
        }
    }
    let mut dir = match start_pipe {
        'F' => 'U',
        'J' => 'D',
        'L' => 'D',
        '7' => 'R',
        '-' => 'R',
        '|' => 'U',
        _ => todo!(),
    };
    let mut steps: u32 = 0;
    let mut curr = start;
    loop {
        if steps > 0 && curr == start {
            return steps / 2;
        } 
        let curr_pipe = if steps == 0 { start_pipe } else { input_vec[curr.1 as usize][curr.0 as usize] };
        steps += 1;
        let next_dir = match curr_pipe {
            'F' => match dir {
                'U' => 'R',
                'L' => 'D',
                _ => todo!(),
            },
            'J' => match dir {
                'D' => 'L',
                'R' => 'U',
                _ => todo!(),
            },
            'L' => match dir {
                'D' => 'R',
                'L' => 'U',
                _ => todo!(),
            },
            '7' => match dir {
                'R' => 'D',
                'U' => 'L',
                _ => todo!(),
            },
            '-' => dir,
            '|' => dir,
            _ => todo!(),
        };
        dir = next_dir;
        let coords = match dir {
            'R' => (1, 0),
            'L' => (-1, 0),
            'U' => (0, -1),
            'D' => (0, 1),
            _ => todo!(),
        };
        curr = (curr.0 + coords.0, curr.1 + coords.1);
    }
}

fn part2(input: &str, start_pipe: char) -> u32 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let mut start: (i32, i32) = (0, 0);
    for (y, line) in input_vec.clone().into_iter().enumerate() {
        for (x, c) in line.into_iter().enumerate() {
            if c == 'S' {
                start = (x as i32, y as i32);
            }
        }
    }
    let mut dir = match start_pipe {
        'F' => 'U',
        'J' => 'D',
        'L' => 'D',
        '7' => 'R',
        '-' => 'R',
        '|' => 'U',
        _ => todo!(),
    };
    let mut steps: u32 = 0;
    let mut curr = start;
    let mut seen: HashSet<(i32, i32)> = HashSet::new();
    loop {
        if steps > 0 && curr == start {
            break;
        } 
        seen.insert(curr);
        let curr_pipe = if steps == 0 { start_pipe } else { input_vec[curr.1 as usize][curr.0 as usize] };
        steps += 1;
        let next_dir = match curr_pipe {
            'F' => match dir {
                'U' => 'R',
                'L' => 'D',
                _ => todo!(),
            },
            'J' => match dir {
                'D' => 'L',
                'R' => 'U',
                _ => todo!(),
            },
            'L' => match dir {
                'D' => 'R',
                'L' => 'U',
                _ => todo!(),
            },
            '7' => match dir {
                'R' => 'D',
                'U' => 'L',
                _ => todo!(),
            },
            '-' => dir,
            '|' => dir,
            _ => todo!(),
        };
        dir = next_dir;
        let coords = match dir {
            'R' => (1, 0),
            'L' => (-1, 0),
            'U' => (0, -1),
            'D' => (0, 1),
            _ => todo!(),
        };
        curr = (curr.0 + coords.0, curr.1 + coords.1);
    }
    let mut inside: u32 = 0;
    let height = input_vec.len() as i32;
    let mut final_vec = input_vec.clone(); 
    final_vec[start.1 as usize][start.0 as usize] = start_pipe;
    for (y, line) in input_vec.into_iter().enumerate() {
        for (x, _c) in line.into_iter().enumerate() {
            let place = (x as i32, y as i32);
            if seen.contains(&place) {
                continue;
            }
            let mut count: u32 = 0;
            let mut y_pos: i32 = y as i32;
            while y_pos < height {
                let temp = final_vec[y_pos as usize][x];
                if seen.contains(&(x as i32, y_pos)) && temp != '|' && temp != '7' && temp != 'J' {
                    count += 1;
                }
                y_pos += 1;
            }
            if count % 2 == 1 {
                inside += 1;
            }
        }
    }
    inside
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt"), 'F'), 4);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example2.txt"), '7'), 10);
    }
}
