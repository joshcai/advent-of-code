def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

WALL = ['#']

def visualize(grid):
  next = ''
  for row in grid:
    for cell in row:
      if len(cell) == 1:
        next += cell[0]
      elif len(cell) == 0:
        next += '.'
      else:
        next += str(len(cell))
      
    print(next)
    next = ''

def compute_grid(grid):
  next = []
  for _ in range(len(grid)):
    next_line = []
    for _ in range(len(grid[0])):
      next_line.append([])
    next.append(next_line)
  for i, row in enumerate(grid):
    for j, cell in enumerate(row):
      if cell == WALL:
        next[i][j] = cell
        continue
      if not cell:
        continue
      if '>' in cell:
        next_j = j + 1
        if grid[i][next_j] == WALL:
          next_j = 1
        next[i][next_j].append('>')
      if '<' in cell:
        next_j = j - 1
        if grid[i][next_j] == WALL:
          next_j = len(grid[0]) - 2
        next[i][next_j].append('<')
      if 'v' in cell:
        next_i = i + 1
        if grid[next_i][j] == WALL:
          next_i = 1
        next[next_i][j].append('v')
      if '^' in cell:
        next_i = i - 1
        if grid[next_i][j] == WALL:
          next_i = len(grid) - 2
        next[next_i][j].append('^')
  return next

def can_move(grid, pos):
  x, y = pos
  if x < 0 or x >= len(grid[0]):
    return False
  if y < 0 or y >= len(grid):
    return False
  return not grid[y][x]

def add(first, second):
  x, y = first
  a, b = second
  return (x+a, y+b)

# Returns step and the next grid.
def compute_time(grid, start, end):
  moves = [
    (0, 0),
    (0, 1),
    (0, -1),
    (1, 0),
    (-1, 0)
  ]
  step = 0
  queue = [start]
  while True:
    next_grid = compute_grid(grid)
    next_queue = []
    next_queue_set = set()
    for curr in queue:
      for move in moves:
        next_move = add(curr, move)
        if next_move in next_queue_set:
          continue
        if can_move(next_grid, next_move):
          if next_move == end:
            return step + 1, next_grid
          next_queue.append(next_move)
          next_queue_set.add(next_move)
    queue = next_queue
    grid = next_grid
    step += 1

def part1(name):
  lines = read_file(name)
  grid = []
  for line in lines:
    grid.append([[x] if x != '.' else [] for x in line])

  start = (1, 0)
  end = (len(grid[0]) - 2, len(grid) - 1)
  steps, _ = compute_time(grid, start, end)
  return steps

def part2(name):
  lines = read_file(name)
  grid = []
  for line in lines:
    grid.append([[x] if x != '.' else [] for x in line])

  start = (1, 0)
  end = (len(grid[0]) - 2, len(grid) - 1)

  total = 0
  steps, grid = compute_time(grid, start, end)
  total += steps
  steps, grid = compute_time(grid, end, start)
  total += steps
  steps, grid = compute_time(grid, start, end)
  total += steps
  return total





print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))

