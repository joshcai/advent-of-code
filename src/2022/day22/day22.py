import re

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def part1(name):
  lines = read_file(name)
  grid = []
  reading_grid = True
  instructions = []
  width = 0
  for line in lines:
    if not line:
      reading_grid = False
      continue
    if reading_grid:
      grid.append(line)
      if len(line) > width:
        width = len(line)
    else:
      instructions = re.findall(r'(R|L|(\d+))', line)
      instructions = [x[0] for x in instructions]
  height = len(grid)
  start = (0, 0)
  for i, c in enumerate(grid[0]):
    if c == '.':
      start = (i, 0)
      break
  dirs = [
    (1, 0),
    (0, 1),
    (-1, 0),
    (0, -1),
  ]
  currDir = 0
  curr = start

  def at(pos):
    try:
      return grid[pos[1]][pos[0]]
    except IndexError:
      return ' '

  def move(curr, currDir):
    temp = curr
    d = dirs[currDir % 4]
    while True:
      next = (temp[0]+d[0], temp[1]+d[1])
      if next[0] < 0:
        next = (width, next[1])
      elif next[0] >= width:
        next = (0, next[1])
      if next[1] < 0:
        next = (next[0], height)
      elif next[1] >= height:
        next = (next[0], 0)

      val = at(next)
      # Blocked by a wall
      if val == '#':
        return curr
      elif val == '.':
        return next
      elif val == ' ':
        temp = next
        continue
      raise ValueError('unexpected grid item')
    
  for ins in instructions:
    if ins == 'R':
      currDir += 1
      continue
    if ins == 'L':
      currDir -= 1
      continue
    num = int(ins)
    for i in range(num):
      curr = move(curr, currDir)

  return (curr[1] + 1) * 1000 + (curr[0] + 1) * 4 + (currDir % 4)

# Hardcoding the cube shape, e.g.:

#     1  2
#     3
#  5  4
#  6
FACES = {
  0: {1: 1, 2: 2},
  1: {1: 3},
  2: {0: 5, 1: 4},
  3: {0: 6}
}

# 0 -> Right
# 1 -> Down
# 2 -> Left
# 3 -> Up

TELEPORT = {
  1: {
    2: (0, lambda x, y: (0, 149 - y)),
    3: (0, lambda x, y: (0, x + 100)),
  },
  2: {
    0: (2, lambda x, y: (99, 149 - y)),
    1: (2, lambda x, y: (99, x - 50)),
    3: (3, lambda x, y: (x - 100, 199)),
  },
  3: {
    0: (3, lambda x, y: (y + 50, 49)),
    2: (1, lambda x, y: (y - 50, 100))
  },
  4: {
    0: (2, lambda x, y: (149, 149 - y)),
    1: (2, lambda x, y: (49, x + 100))
  },
  5: {
    2: (0, lambda x, y: (50, 149 - y)),
    3: (0, lambda x, y: (50, x + 50)),
  },
  6: {
    0: (3, lambda x, y: (y - 100 , 149)),
    1: (1, lambda x, y: (x + 100, 0)),
    2: (1, lambda x, y: (y - 100, 0)),
  }
}

# With some pointers from https://www.reddit.com/r/adventofcode/comments/zsct8w/2022_day_22_solutions/
def part2(name):
  lines = read_file(name)
  grid = []
  reading_grid = True
  instructions = []
  width = 0
  for line in lines:
    if not line:
      reading_grid = False
      continue
    if reading_grid:
      grid.append(line)
      if len(line) > width:
        width = len(line)
    else:
      instructions = re.findall(r'(R|L|(\d+))', line)
      instructions = [x[0] for x in instructions]
  height = len(grid)
  start = (0, 0)
  for i, c in enumerate(grid[0]):
    if c == '.':
      start = (i, 0)
      break
  dirs = [
    (1, 0),
    (0, 1),
    (-1, 0),
    (0, -1),
  ]
  currDir = 0
  curr = start

  def at(pos):
    try:
      return grid[pos[1]][pos[0]]
    except IndexError:
      return ' '

  # Returns next position and next direction.
  def teleportToFace(curr, currDir):
    currDir = currDir % 4
    x, y = curr
    currFace = FACES[y//50][x//50]
    nextDir, nextFn = TELEPORT[currFace][currDir]
    return nextDir, nextFn(x, y)


  def move(curr):
    nonlocal currDir
    d = dirs[currDir % 4]
    while True:
      next = (curr[0]+d[0], curr[1]+d[1])
      outOfBounds = False
      if next[0] < 0 or next[0] >= width:
        outOfBounds = True
      if next[1] < 0 or next[1] >= height:
        outOfBounds = True

      teleport = outOfBounds
      if not outOfBounds:
        val = at(next)
        if val == ' ':
          teleport = True
      
      # Next should move to another face.
      nextDir = None
      if teleport:
        nextDir, next = teleportToFace(curr, currDir)
      val = at(next)
      # Blocked by a wall
      if val == '#':
        return curr
      elif val == '.':
        if nextDir is not None:
          currDir = nextDir
        return next
      else:
        raise ValueError('unexpected grid item')
    
  for ins in instructions:
    if ins == 'R':
      currDir += 1
      continue
    if ins == 'L':
      currDir -= 1
      continue
    num = int(ins)
    for i in range(num):
      curr = move(curr)

  return (curr[1] + 1) * 1000 + (curr[0] + 1) * 4 + (currDir % 4)


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('input'))
