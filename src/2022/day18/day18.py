import collections

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def part1(name):
  lines = read_file(name)

  seen = set()
  total = 0

  def check(pos, x, y, z):
    a, b, c = pos
    nonlocal total, seen
    if (a + x, b + y, c + z) in seen:
      total -= 1
    else:
      total += 1

  for line in lines:
    x, y, z = [int(x) for x in line.split(',')]
    pos = (x, y, z)
    check(pos, 1, 0, 0)
    check(pos, -1, 0, 0)
    check(pos, 0, 1, 0)
    check(pos, 0, -1, 0)
    check(pos, 0, 0, 1)
    check(pos, 0, 0, -1)
    seen.add(pos)

  return total

def part2(name):
  lines = read_file(name)

  seen = set()
  others = collections.defaultdict(int)
  total = 0

  def check(pos, x, y, z):
    a, b, c = pos
    nonlocal total, seen
    next = (a + x, b + y, c + z)
    if next in seen:
      total -= 1
    else:
      others[next] += 1
      total += 1

  minX = 1e8
  maxX = -1e8
  minY = 1e8
  maxY = -1e8
  minZ = 1e8
  maxZ = -1e8

  for line in lines:
    x, y, z = [int(x) for x in line.split(',')]
    if x < minX:
      minX = x
    if x > maxX:
      maxX = x
    if y < minY:
      minY = y
    if y > maxY:
      maxY = y
    if z < minZ:
      minZ = z
    if z > maxZ:
      maxZ = z
    pos = (x, y, z)
    check(pos, 1, 0, 0)
    check(pos, -1, 0, 0)
    check(pos, 0, 1, 0)
    check(pos, 0, -1, 0)
    check(pos, 0, 0, 1)
    check(pos, 0, 0, -1)
    seen.add(pos)
  

  visited = set()
  def contained(pos, x, y, z):
    a, b, c = pos
    pos = (a + x, b + y, c + z)
    nonlocal visited
    if pos in visited:
      return True
    if pos in seen:
      return True
    curX, curY, curZ = pos
    if curX < minX or curX > maxX:
      return False
    if curY < minY or curY > maxY:
      return False
    if curZ < minZ or curZ > maxZ:
      return False
    visited.add(pos)
    directions = [
      (1, 0, 0),
      (-1, 0, 0),
      (0, 1, 0),
      (0, -1, 0),
      (0, 0, 1),
      (0, 0, -1),
    ]
    for dir in directions:
      x, y, z = dir
      if not contained(pos, x, y, z):
        return False
    return True

  for pos, count in others.items():
    if pos in seen:
      continue
    if count == 6:
      total -= count
      continue
    visited = set()
    if contained(pos, 0, 0, 0):
      total -= count

  return total


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
