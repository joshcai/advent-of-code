fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt"), 200000000000000, 400000000000000));
}

fn get_slope(x1: i64, y1: i64, dx: i64, dy: i64) -> (f32, f32) {
  let x2 = x1 + dx;
  let y2 = y1 + dy;
  let slope = (y2 - y1) as f32 / (x2 - x1) as f32;
  let intercept = y1 as f32 - slope * x1 as f32;
  (slope, intercept)
}

fn part1(input: &str, low: i64, high: i64) -> u64 {
    let points = input.lines()
        .map(|s| s.replace(" @", ",")
                .split(",")
                .map(|n| n.trim().parse::<i64>().unwrap())
                .collect::<Vec<_>>())
        .collect::<Vec<_>>();
    let mut collisions = 0;
    for i in 0..points.len() {
        for j in i+1..points.len() {
            let a = &points[i];
            let b = &points[j];
            let (m1, b1) = get_slope(a[0], a[1], a[3], a[4]);
            let (m2, b2) = get_slope(b[0], b[1], b[3], b[4]);
            if m1 == m2 {
                continue;
            }
            let x = (b2 - b1) / (m1 - m2);
            let y = m1 * x + b1;
            if x < low as f32 || x > high as f32 {
                continue;
            }
            if y < low as f32 || y > high as f32 {
                continue;
            }
            if (x - a[0] as f32) / (a[3] as f32) < 0.0 {
                continue;
            }
            if (x - b[0] as f32) / (b[3] as f32) < 0.0 {
                continue;
            }
            collisions += 1;
        }
    }
    collisions
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt"), 7, 27), 2);
    }
}
