import collections

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

class Rule:

  def __init__(self, check):
    self.check = check
    self.dir = check[1]

def visualize(curr):
  minX = 1e7
  maxX = -1e7
  minY = 1e7
  maxY = -1e7

  for pos in curr:
    x, y = pos
    if x < minX:
      minX = x
    if x > maxX:
      maxX = x
    if y < minY:
      minY = y
    if y > maxY:
      maxY = y

  for i in range(minY, maxY + 1):
    next = ''
    for j in range(minX, maxX + 1):
      if (j, i) in curr:
        next += '#'
      else:
        next += '.'
    print(next)


def part1(name):
  lines = read_file(name)
  curr = set()
  for i, line in enumerate(lines):
    for j, cell in enumerate(line):
      if cell != '#':
        continue
      curr.add((j, i))
  rules = [
    Rule([(-1, -1), (0, -1), (1, -1)]),
    Rule([(-1, 1), (0, 1), (1, 1)]),
    Rule([(-1, -1), (-1, 0), (-1, 1)]),
    Rule([(1, -1), (1, 0), (1, 1)]),
  ]

  # Returns next position if it's free, otherwise None.
  def free(pos, rule):
    nonlocal curr
    a, b = pos
    for c in rule.check:
      x, y = c
      if (a+x, b+y) in curr:
        return None
    x, y = rule.dir
    return (a+x, b+y)

  for i in range(10):
    # mapping from curr -> next position
    next = {}
    for pos in curr:
      available = []
      for j in range(4):
        available.append(free(pos, rules[(i+j)%4]))
        available = [x for x in available if x is not None]
      if len(available) == 4 or not available:
        next[pos] = pos
      else:
        next[pos] = available[0]
    count = collections.Counter(next.values())
    final_next = set()
    for c, n in next.items():
      if count[n] == 1:
        final_next.add(n)
      else:
        final_next.add(c)
    curr = final_next
  minX = 1e7
  maxX = -1e7
  minY = 1e7
  maxY = -1e7

  for pos in curr:
    x, y = pos
    if x < minX:
      minX = x
    if x > maxX:
      maxX = x
    if y < minY:
      minY = y
    if y > maxY:
      maxY = y

  return (maxY - minY + 1) * (maxX - minX + 1) - len(curr)

def part2(name):
  lines = read_file(name)
  curr = set()
  for i, line in enumerate(lines):
    for j, cell in enumerate(line):
      if cell != '#':
        continue
      curr.add((j, i))
  rules = [
    Rule([(-1, -1), (0, -1), (1, -1)]),
    Rule([(-1, 1), (0, 1), (1, 1)]),
    Rule([(-1, -1), (-1, 0), (-1, 1)]),
    Rule([(1, -1), (1, 0), (1, 1)]),
  ]

  # Returns next position if it's free, otherwise None.
  def free(pos, rule):
    nonlocal curr
    a, b = pos
    for c in rule.check:
      x, y = c
      if (a+x, b+y) in curr:
        return None
    x, y = rule.dir
    return (a+x, b+y)

  step = 0
  while True:
    # mapping from curr -> next position
    next = {}
    for pos in curr:
      available = []
      for j in range(4):
        available.append(free(pos, rules[(step+j)%4]))
        available = [x for x in available if x is not None]
      if len(available) == 4 or not available:
        next[pos] = pos
      else:
        next[pos] = available[0]
    count = collections.Counter(next.values())
    final_next = set()
    for c, n in next.items():
      if count[n] == 1:
        final_next.add(n)
      else:
        final_next.add(c)
    if len(final_next - curr) == 0:
      return step + 1
    curr = final_next
    step += 1

print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))

