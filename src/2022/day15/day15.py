import re

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

# Taken from: https://www.interviewbit.com/blog/merge-intervals/
def merge(intervals):
  intervals.sort(key=lambda x: x[0])
  merged = []
  for interval in intervals:
    if not merged or merged[-1][1] < interval[0]:
      merged.append(interval)
    else:
      merged[-1][1] = max(merged[-1][1], interval[1])

  return merged

def part1(name, target):
  lines = read_file(name)
  ranges = []
  total = 0
  beacons = set()
  for line in lines:
    nums = re.findall(r'(\d+)', line)
    nums = [int(x) for x in nums]
    sx, sy, bx, by = nums
    if by == target:
      beacons.add(bx)
    dist = abs(bx - sx) + abs(by - sy)
    excess = dist - abs(sy - target)
    if excess <= 0:
      continue
    ranges.append([sx - excess, sx + excess])
  
  ranges = merge(ranges)
  for r in ranges:
    total += r[1] - r[0] + 1

  for beacon in beacons:
    for r in ranges:
      if beacon >= r[0] and beacon <= r[1]:
        total -= 1
        break

  return total

def part2(name, upper):
  lines = read_file(name)
  beacons = set()
  all_inputs = []
  for line in lines:
    nums = re.findall(r'(\d+)', line)
    nums = [int(x) for x in nums]
    all_inputs.append(nums)
  
  def get_range(target):
    ranges = []
    for nums in all_inputs:
      sx, sy, bx, by = nums
      if by == target:
        beacons.add(bx)
      dist = abs(bx - sx) + abs(by - sy)
      excess = dist - abs(sy - target)
      if excess <= 0:
        continue
      ranges.append([sx - excess, sx + excess])

    return merge(ranges)

  for i in range(upper+1):
    merged = get_range(i)
    if len(merged) == 2:
      print(i, merged)
      return (merged[0][1] + 1) * 4000000 + i
  
  return 0


print('Part 1')
print(part1('test', 10))
print(part1('input', 2000000))

print('Part 2')
print(part2('test', 20))
print(part2('input', 4000000))
