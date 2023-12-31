use std::collections::BinaryHeap;
use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

// Heavily borrowed from https://www.reddit.com/r/adventofcode/comments/18k9ne5/2023_day_17_solutions/kdpyysi/
fn get_heat_loss(min_steps: isize, max_steps: isize, input_vec: &Vec<Vec<i32>>) -> i32 {
  let mut dists = HashMap::new();
  // (Cost, (x, y (dir_x, dir_y)))
  // Cost needs to be negative as it is a max heap.
  let mut q = BinaryHeap::from_iter([(0, (0,0,(0,0)))]);
  let end = (input_vec[0].len() - 1, input_vec.len() - 1);
  while let Some((cost, (x, y, d))) = q.pop() {
    if (x, y) == end {
      return -cost;
    }
    if -cost > *dists.get(&(x, y, d)).unwrap_or(&i32::MAX) {
      continue;
    }
    for (dx, dy) in [(-1,0), (1,0), (0,-1), (0,1)] {
      // We already traveled in this direction, so we can't travel
      // again in this direction. Also, we can't flip directions,
      // we can only turn.
      if d == (dx, dy) || d == (-dx, -dy) {
        continue;
      }
      let mut next_cost = cost;
      for i in 1..=max_steps {
        let new_x = x as isize + i * dx;
        let new_y = y as isize + i * dy;
        if new_x < 0 || new_x > end.0 as isize {
          continue;
        }
        if new_y < 0 || new_y > end.1 as isize {
          continue;
        }
        next_cost -= input_vec[new_y as usize][new_x as usize];
        // Can't be merged into for loop, as we need to accumulate
        // the cost of each of the previous steps.
        if i < min_steps {
          continue;
        }
        let key = (new_x as usize, new_y as usize, (dx, dy));
        if -next_cost < *dists.get(&key).unwrap_or(&i32::MAX) {
          dists.insert(key, -next_cost);
          q.push((next_cost, key));
        }
      }
    }
  }
  -1
}
    
fn part1(input: &str) -> i32 {
    let input_vec = input.lines().map(|s| s.chars().map(|c| c.to_digit(10).unwrap() as i32).collect::<Vec<_>>()).collect::<Vec<_>>();
    get_heat_loss(1, 3, &input_vec)
}

fn part2(input: &str) -> i32 {
    let input_vec = input.lines().map(|s| s.chars().map(|c| c.to_digit(10).unwrap() as i32).collect::<Vec<_>>()).collect::<Vec<_>>();
    get_heat_loss(4, 10, &input_vec)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 102);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 94);
    }
}
