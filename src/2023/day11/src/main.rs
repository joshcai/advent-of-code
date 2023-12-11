
fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt"), 2));
    println!("Part 2: {}", part1(include_str!("./input.txt"), 1000000));
}

fn part1(input: &str, expansion: u64) -> u64 {
    let input_vec = input.lines().map(|s| s.chars().collect::<Vec<_>>()).collect::<Vec<_>>();
    let mut row_vec: Vec<u64> = Vec::new();
    let mut temp_galaxies: Vec<(u64, u64)> = Vec::new();
    for (y, line) in input_vec.clone().into_iter().enumerate() {
        let mut seen = false;
        for (x, c) in line.into_iter().enumerate() {
            if c == '#' {
                temp_galaxies.push((x as u64, y as u64));
                seen = true;
            }
        }
        if !seen {
            row_vec.push(y as u64);
        }
    }
    let mut col_vec: Vec<u64> = Vec::new();
    for x in 0..input_vec[0].len() {
        let mut seen = false;
        for y in 0..input_vec.len() {
            if input_vec[y][x] == '#' {
                seen = true;
            }
        }
        if !seen {
            col_vec.push(x as u64);
        }
    }
    let mut galaxies: Vec<(u64, u64)> = Vec::new();
    for coord in temp_galaxies.into_iter() {
        let x = coord.0;
        let y = coord.1;
        let y_count = row_vec.clone().into_iter().filter(|s| s < &y).count() as u64; 
        let x_count = col_vec.clone().into_iter().filter(|s| s < &x).count() as u64; 
        galaxies.push((x + x_count * (expansion - 1), y + y_count * (expansion - 1)));
    }
    let mut result: u64 = 0;
    for coord1 in galaxies.clone().into_iter() {
        for coord2 in galaxies.clone().into_iter() {
            if coord1 == coord2 {
                continue;
            }
            result += coord1.0.abs_diff(coord2.0) + coord1.1.abs_diff(coord2.1);
        }
    }
    result / 2

}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt"), 2), 374);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part1(include_str!("./example.txt"), 10), 1030);
        assert_eq!(part1(include_str!("./example.txt"), 100), 8410);
    }
}
