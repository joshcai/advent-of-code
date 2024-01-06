use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

fn num_arrangements(parts: &Vec<char>, nums: &Vec<u64>) -> u64 {
  let mut cache = HashMap::new();

  fn recurse(parts: &Vec<char>, nums: &Vec<u64>, cache: &mut HashMap<(u64, u64, u64), u64>, i: u64, num_idx: u64, curr_len: u64) -> u64 {
    let key = (i, num_idx, curr_len);
    if cache.contains_key(&key) {
      return cache[&key];
    }
    if i == parts.len() as u64 {
      if num_idx == nums.len() as u64 && curr_len == 0 {
        return 1;
      }
      if num_idx == nums.len() as u64 - 1 && curr_len == nums[nums.len()-1] {
        return 1;
      }
      return 0;
    }
    let mut count = 0;
    let curr_part = parts[i as usize];
    if curr_part == '.' || curr_part == '?' {
      if curr_len == 0 {
        // Just increase index, since no block being formed currently.
        count += recurse(parts, nums, cache, i+1, num_idx, curr_len);
      }
      if curr_len > 0 && num_idx < nums.len() as u64 && nums[num_idx as usize] == curr_len {
        // Matched a full block, can move to next block.
        count += recurse(parts, nums, cache, i+1, num_idx+1, 0);
      }
    }
    if curr_part == '#' || curr_part == '?' {
      // Add to current block length.
      count += recurse(parts, nums, cache, i+1, num_idx, curr_len+1);
    }
    cache.insert(key, count);
    return count;
  }
  return recurse(parts, nums, &mut cache, 0, 0, 0);
}

// Based off of: https://www.reddit.com/r/adventofcode/comments/18ge41g/2023_day_12_solutions/kd03uf3/
fn part1(input: &str) -> u64 {
    let mut sum = 0;
    for line in input.lines() {
      let parts = line.split_whitespace().collect::<Vec<_>>();
      let springs = parts[0].chars().collect::<Vec<_>>();
      let nums = parts[1].split(',').map(|x| x.parse::<u64>().unwrap()).collect::<Vec<_>>();
      let n = num_arrangements(&springs, &nums);
      sum += n;
    }
  sum
}

fn part2(input: &str) -> u64 {
    let mut sum = 0;
    for line in input.lines() {
      let parts = line.split_whitespace().collect::<Vec<_>>();
      let mut springs = parts[0].chars().collect::<Vec<_>>();
      let cloned_springs = springs.clone();
      for _i in 0..4 {
        springs.push('?');
        for c in cloned_springs.clone().into_iter() {
          springs.push(c);
        }
      }
      let mut nums = parts[1].split(',').map(|x| x.parse::<u64>().unwrap()).collect::<Vec<_>>();
      let cloned_nums = nums.clone();
      for _i in 0..4 {
        for n in cloned_nums.clone().into_iter() {
          nums.push(n);
        }
      }
      let n = num_arrangements(&springs, &nums);
      sum += n;
    }
  sum
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 21);
    }

    #[test]
    fn part2_example() {
        assert_eq!(part2(include_str!("./example.txt")), 525152);
    }
}
