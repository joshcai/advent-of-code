fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

const DIRECTIONS: [(i32, i32); 8] = [
    (-1, -1),
    (-1, 0), 
    (-1, 1),
    (0, -1),
    (0, 1),
    (1, -1),
    (1, 0), 
    (1, 1),
];

fn part1(input: &str) -> u32 {
    let lines: Vec<&str> = input.lines().collect();

    let check = |x: i32, y: i32| {
        for (x2, y2) in DIRECTIONS {
            let x3: i32 = x + x2;
            let y3: i32 = y + y2;
            if x3 < 0 || x3 >= lines[0].len().try_into().unwrap() {
                continue;
            }
            if y3 < 0 || y3 >= lines.len().try_into().unwrap() {
                continue;
            }
            let c = lines[y3 as usize].chars().nth(x3 as usize).unwrap();
            if !c.is_ascii_digit() && c != '.' {
                return true;
            }
        }
        return false;
    };

    let mut result: u32 = 0;
    for (i, line) in lines.iter().enumerate() {
        let mut near = false;
        let mut buffer: String = "".to_string();
        for (j, ch) in line.chars().enumerate() {
            if ch.is_ascii_digit() {
                if check(j.try_into().unwrap(), i.try_into().unwrap()) {
                    near = true;
                }
                buffer.push(ch);
                continue;
            }
            if buffer.len() > 0 {
                if near {
                    result += buffer.parse::<u32>().unwrap();
                }
                buffer = "".to_string();
                near = false;
            }
        }
        if buffer.len() > 0 && near {
            result += buffer.parse::<u32>().unwrap();
        }
    }
    result
}

fn part2(input: &str) -> u32 {
    let lines: Vec<&str> = input.lines().collect();

    let check = |x: i32, y: i32| {
        for (x2, y2) in DIRECTIONS {
            let x3: i32 = x + x2;
            let y3: i32 = y + y2;
            if x3 < 0 || x3 >= lines[0].len().try_into().unwrap() {
                continue;
            }
            if y3 < 0 || y3 >= lines.len().try_into().unwrap() {
                continue;
            }
            let c = lines[y3 as usize].chars().nth(x3 as usize).unwrap();
            if c == '*' {
                return Some((x3, y3));
            }
        }
        return None;
    };

    let mut hm: std::collections::HashMap<(i32, i32), Vec<u32>> = Default::default();
    for (i, line) in lines.iter().enumerate() {
        let mut near = (-1, -1);
        let mut buffer: String = "".to_string();
        for (j, ch) in line.chars().enumerate() {
            if ch.is_ascii_digit() {
                match check(j.try_into().unwrap(), i.try_into().unwrap()) {
                    Some(pair) => near = pair,
                    None => (),
                }
                buffer.push(ch);
                continue;
            }
            if buffer.len() > 0 {
                if near != (-1, -1) {
                    let val = buffer.parse::<u32>().unwrap();
                    hm.entry(near).or_insert(Vec::new()).push(val);
                }
                buffer = "".to_string();
                near = (-1, -1);
            }
        }
        if buffer.len() > 0 && near != (-1, -1) {
            let val = buffer.parse::<u32>().unwrap();
            hm.entry(near).or_insert(Vec::new()).push(val);
        }
    }
    let mut result: u32 = 0;
    for val in hm.values() {
        if val.len() == 2 {
            result += val[0] * val[1];
        }
    }
    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 4361);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 467835);
    } 
}