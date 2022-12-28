import functools
import collections
import re

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

# Robot indices
ORE = 0
CLAY = 1
OBS = 2
GEO = 3

def update(curr, ore=0, clay=0, obs=0, geo=0):
  curr_ore, curr_clay, curr_obs, curr_geo = curr
  return (curr_ore + ore, curr_clay + clay, curr_obs + obs, curr_geo + geo)

def add(curr, next):
  a, b, c, d = curr
  w, x, y, z = next
  return (a+w, b+x, c+y, d+z)

def sub(curr):
  a, b, c, d = curr
  return (-1 * a, -1 * b, -1 * c, -1 * d)

def canMake(curr, cost):
  for a, b in zip(curr, cost):
    if a - b < 0:
      return False
  return True

def key(item):
  resources, robots = item
  a, b, c, d = add(resources, robots)
  w, x, y, z = robots
  return (d, c, b, a, z, y, x, w)

def maximum(minutes, ore, clay, obs1, obs2, geo1, geo2):
  
  allCosts = [
    ((ore, 0, 0, 0), (1, 0, 0, 0)),
    ((clay, 0, 0, 0), (0, 1, 0, 0)),
    ((obs1, obs2, 0, 0), (0, 0, 1, 0)),
    ((geo1, 0, geo2, 0), (0, 0, 0, 1)),
    ((0, 0, 0, 0), (0, 0, 0, 0))
  ]

  # Based on: https://www.reddit.com/r/adventofcode/comments/zpihwi/2022_day_19_solutions/j0tvzgz/
  resources = (0, 0, 0, 0)
  robots = (1, 0, 0, 0)
  queue = [(resources, robots)]
  for _ in range(minutes):
    next_queue = []
    for curr_resources, curr_robots in queue:
      for cost, make in allCosts:
        if canMake(curr_resources, cost):
          next_queue.append((
            add(add(curr_resources, curr_robots), sub(cost)),
            add(curr_robots, make)))
    next_queue.sort(key=key)
    queue = next_queue[-5000:]
  
  queue.sort(key=key, reverse=True)
  return queue[0][0][3]
  
def part1(name):
  lines = read_file(name)
  
  # V1 -> V2 -> distance
  dist = collections.defaultdict(dict)
  nodes = {}
  total = 0
  for line in lines:
    m = re.findall(r'(\d+)', line)
    blueprint, ore, clay, obs1, obs2, geo1, geo2 = [int(x) for x in m]
    geo_max = maximum(24, ore, clay, obs1, obs2, geo1, geo2)
    total += blueprint * geo_max
  return total

def part2(name):
  lines = read_file(name)
  
  # V1 -> V2 -> distance
  dist = collections.defaultdict(dict)
  nodes = {}
  total = 1
  for i, line in enumerate(lines):
    if i > 2:
      break
    m = re.findall(r'(\d+)', line)
    blueprint, ore, clay, obs1, obs2, geo1, geo2 = [int(x) for x in m]
    geo_max = maximum(32, ore, clay, obs1, obs2, geo1, geo2)
    total *= geo_max
  return total

print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
