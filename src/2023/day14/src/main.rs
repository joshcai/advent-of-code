
use std::collections::hash_map::DefaultHasher;
use std::hash::{Hash, Hasher};

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn part1(input: &str) -> u64 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let mut total: u64 = 0;
    let length = input_vec.len() as u64;
    for i in 0..input_vec[0].len() {
        let mut base: u64 = 0;
        let mut count: u64 = 0;
        for j in 0..input_vec.len() {
            let c = input_vec[j][i];
            if c == '#' {
                base = j as u64 + 1;
                count = 0;
                continue;
            }
            if c == 'O' {
                total += length - base - count;
                count += 1;
            }
        }
    }
    total
}

fn slide_north(input_vec: &mut Vec<Vec<char>>) {
    let length = input_vec.len();
    let row_size = input_vec[0].len();
    for i in 0..row_size {
        let mut base: usize = 0;
        let mut count: usize = 0;
        for j in 0..length {
            let c = input_vec[j][i];
            if c == '#' {
                base = j + 1;
                count = 0;
                continue;
            }
            if c == 'O' {
                input_vec[j][i] = '.';
                input_vec[base+count][i] = 'O';
                count += 1;
            }
        }
    }
}


fn slide_south(input_vec: &mut Vec<Vec<char>>) {
    let length = input_vec.len();
    let end = length - 1;
    let row_size = input_vec[0].len();
    for i in 0..row_size {
        let mut base: usize = 0;
        let mut count: usize = 0;
        for j in 0..length {
            let c = input_vec[end-j][i];
            if c == '#' {
                base = j + 1;
                count = 0;
                continue;
            }
            if c == 'O' {
                input_vec[end-j][i] = '.';
                input_vec[end - (base+count)][i] = 'O';
                count += 1;
            }
        }
    }
}

fn slide_east(input_vec: &mut Vec<Vec<char>>) {
    let length = input_vec.len();
    let row_size = input_vec[0].len();
    let end = row_size - 1;
    for i in 0..length {
        let mut base: usize = 0;
        let mut count: usize = 0;
        for j in 0..row_size {
            let c = input_vec[i][end-j];
            if c == '#' {
                base = j + 1;
                count = 0;
                continue;
            }
            if c == 'O' {
                input_vec[i][end-j] = '.';
                input_vec[i][end - (base+count)] = 'O';
                count += 1;
            }
        }
    }
}

fn slide_west(input_vec: &mut Vec<Vec<char>>) {
    let length = input_vec.len();
    let row_size = input_vec[0].len();
    for i in 0..length {
        let mut base: usize = 0;
        let mut count: usize = 0;
        for j in 0..row_size {
            let c = input_vec[i][j];
            if c == '#' {
                base = j + 1;
                count = 0;
                continue;
            }
            if c == 'O' {
                input_vec[i][j] = '.';
                input_vec[i][base+count] = 'O';
                count += 1;
            }
        }
    }
}

fn hash_count(input_vec: &Vec<Vec<char>>) -> u64 {
    let mut hasher = DefaultHasher::new();
    let s = input_vec.iter()
        .map(|x| x.iter().collect::<String>())
        .collect::<String>();
    s.hash(&mut hasher);
    hasher.finish()
}

fn load(input_vec: &Vec<Vec<char>>) -> u64 {
    let mut total: u64 = 0;
    let length = input_vec.len();
    for (i, line) in input_vec.into_iter().enumerate() {
        for ch in line.into_iter() {
            if *ch == 'O' {
                total += (length - i) as u64;
            }
        }
    }
    total
}

fn part2(input: &str) -> u64 {
    let mut input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    // Hashes of the state to check uniqueness.
    let mut hs: Vec<u64> = Vec::new();
    // Loads corresponding to the different states.
    let mut loads: Vec<u64> = Vec::new();
    let mut check = |input_vec| {
        let hc = hash_count(&input_vec);
        if hs.contains(&hc) {
            // Push one last time, we use this to determine the loop start.
            hs.push(hc);
            return true;
        }
        hs.push(hc);
        loads.push(load(&input_vec));
        return false;
    };
    loop {
        slide_north(&mut input_vec);
        slide_west(&mut input_vec);
        slide_south(&mut input_vec);
        slide_east(&mut input_vec);
        if check(input_vec.clone()) { break; }
    }
    let last = hs.pop().unwrap();
    let first_index = hs.iter().position(|&x| x == last).unwrap();
    
    let hs_len = hs.len();
    let sliced_hs = &hs[first_index..hs_len];
    let sliced_loads = &loads[first_index..hs_len];

    sliced_loads[(1000000000 - (first_index+1)) % sliced_hs.len()]
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 136);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 64);
    }
}
