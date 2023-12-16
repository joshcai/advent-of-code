
fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn val(input: Vec<Vec<char>>, val: u64) -> u64 {
    // Check horizontal.
    for i in 1..input.len() {
        let mut x: i32 = (i as i32) - 1;
        let mut y = i;
        let mut works = true;
        while x >= 0 && y < input.len() {
            if input[x as usize] != input[y] {
                works = false;
                break;
            }
            x -= 1;
            y += 1;
        }
        if works && (i as u64) * 100 != val {
            return (i as u64) * 100;
        }
    }
    // Check vertical.
    let length = input[0].len();
    let col = |i| {
        input.iter().map(|s| s[i])
            .collect::<Vec<_>>()
    };
    for i in 1..length {
        let mut x: i32 = (i as i32) - 1;
        let mut y = i;
        let mut works = true;
        while x >= 0 && y < length {
            if col(x as usize) != col(y) {
                works = false;
                break;
            }
            x -= 1;
            y += 1;
        }
        if works && i as u64 != val {
            return i as u64;
        }
    }
    0
}

fn part1(input: &str) -> u64 {
    let mut total: u64 = 0;
    let mut input_vec: Vec<Vec<char>> = Vec::new();
    for line in input.lines() {
        if line.len() == 0 {
            total += val(input_vec.clone(), 0);
            input_vec.clear();
            continue;
        }
        input_vec.push(line.chars().collect::<Vec<_>>());
    }
    total + val(input_vec.clone(), 0)
}

fn swap(c: char) -> char {
    match c {
        '#' => '.',
        '.' => '#',
        _ => todo!(),
    }
}

fn val2(input: &mut Vec<Vec<char>>) -> u64 {
    let old_val = val(input.to_vec(), 0);
    for i in 0..input.len() {
        for j in 0..input[i].len() {
            let prev_char = input[i][j];
            input[i][j] = swap(prev_char);
            let v = val(input.clone(), old_val);
            if v > 0 && v != old_val {
                println!("{}", v);
                return v
            }
            input[i][j] = prev_char;
        }
    }
    todo!();
}

fn part2(input: &str) -> u64 {
    let mut total: u64 = 0;
    let mut input_vec: Vec<Vec<char>> = Vec::new();
    for line in input.lines() {
        if line.len() == 0 {
            total += val2(&mut input_vec);
            input_vec.clear();
            continue;
        }
        input_vec.push(line.chars().collect::<Vec<_>>());
    }
    total + val2(&mut input_vec)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 405);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 400);
    }
}
