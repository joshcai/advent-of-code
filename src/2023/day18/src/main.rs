use std::collections::HashSet;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}
    
fn part1(input: &str) -> usize {
    let pairs = input
      .lines()
      .map(|s| s.split_whitespace().collect::<Vec<_>>())
      .map(|t| (t[0], t[1].parse::<u64>().unwrap()));
    let mut hs = HashSet::new();
    hs.insert((0, 0));
    let mut x = 0;
    let mut y = 0;
    for pair in pairs {
        let (dir, dist) = pair;
        for _ in 0..dist {
            match dir {
                "R" => {
                    x += 1;
                },
                "U" => {
                    y -= 1;
                },
                "D" => {
                    y += 1;
                },
                "L" => {
                    x -= 1;
                },
                _ => todo!(),
            };
            hs.insert((x, y));
        }
    }
    fn fill(x: isize, y: isize, hs: &mut HashSet<(isize, isize)>) {
        if hs.contains(&(x, y)) {
            return;
        }
        hs.insert((x, y));
        fill(x+1, y, hs);
        fill(x-1, y, hs);
        fill(x, y+1, hs);
        fill(x, y-1, hs);
    }
    fill(1, 1, &mut hs);
    hs.len()
}

// Using shoelace theorem + Pick's theorem.
fn part2(input: &str) -> i64 {
    let pairs = input
      .lines()
      .map(|s| s.split_whitespace().last().unwrap().strip_prefix("(#").unwrap().strip_suffix(")").unwrap())
      .map(|t| (t.chars().last().unwrap(), i64::from_str_radix(&t[0..5], 16).unwrap()));
    let mut hs = HashSet::new();
    hs.insert((0, 0));
    let mut x = 0;
    let mut y = 0;
    let mut perimeter = 0;
    let mut coords = Vec::new();
    coords.push((0, 0));
    for pair in pairs {
        let (dir, dist) = pair;
        perimeter += dist;
        match dir {
            '0' => {
                x += dist;
            },
            '1' => {
                y += dist;
            },
            '2' => {
                x -= dist;
            },
            '3' => {
                y -= dist;
            },
            _ => todo!(),
        };
        coords.push((x, y));
    }
    let mut area = 0;
    let coords_len = coords.len();
    for (i, coord) in coords.clone().into_iter().enumerate() {
        let (_x, y) = coord;
        let (x1, _y1) = coords[(i + coords_len - 1) % coords_len];
        let (x2, _y2) = coords[(i + 1) % coords_len];
        area += y * (x1 - x2);
    }
    area /= 2;
    // Pick's theorem: area = interior + perimeter points / 2 - 1
    let interior = area + 1 - perimeter / 2;
    interior + perimeter
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 62);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 952408144115);
    }
}
