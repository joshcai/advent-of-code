import heapq

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def part1(name):
  lines = read_file(name)
  grid = []
  start = (-1, -1)
  end = (-1, -1)
  for line in lines:
    grid.append([c for c in line])

  for i, row in enumerate(grid):
    for j, cell in enumerate(row):
      if cell == 'S':
        start = (i, j)
      if cell == 'E':
        end = (i, j)

  distances = {}
  distances[start] = 0
  # Added to the heap
  seen = {start}
  queue = [(0, start)]
  heapq.heapify(queue)

  def getElevation(pos):
    c = grid[pos[0]][pos[1]]
    if c == 'S':
      return 'a'
    if c == 'E':
      return 'z'
    return c

  def canVisit(currPos, xDel, yDel):
    newPos = (currPos[0] + yDel, currPos[1] + xDel)
    if newPos[0] < 0 or newPos[0] >= len(grid):
      return (None, False)
    if newPos[1] < 0 or newPos[1] >= len(grid[0]):
      return (None, False)
    if newPos in seen:
      return (None, False)
    currChar = getElevation(currPos)
    newChar = getElevation(newPos)
    if ord(newChar) <= ord(currChar) + 1:
      seen.add(newPos)
      return (newPos, True)
    return (None, False)

  while True:
    currDist, currPos = heapq.heappop(queue)

    if currPos == end:
      return currDist

    nextDist = currDist + 1

    for pair in ((-1, 0), (1, 0), (0, 1), (0, -1)):
      x, y = pair
      newPos, ok = canVisit(currPos, x, y)
      if ok:
        heapq.heappush(queue, (nextDist, newPos))

def part2(name):
  lines = read_file(name)
  grid = []
  start = (-1, -1)
  for line in lines:
    grid.append([c for c in line])

  for i, row in enumerate(grid):
    for j, cell in enumerate(row):
      if cell == 'E':
        start = (i, j)

  distances = {}
  distances[start] = 0
  # Added to the heap
  seen = {start}
  queue = [(0, start)]
  heapq.heapify(queue)

  def getElevation(pos):
    c = grid[pos[0]][pos[1]]
    if c == 'S':
      return 'a'
    if c == 'E':
      return 'z'
    return c

  def canVisit(currPos, xDel, yDel):
    newPos = (currPos[0] + yDel, currPos[1] + xDel)
    if newPos[0] < 0 or newPos[0] >= len(grid):
      return (None, False)
    if newPos[1] < 0 or newPos[1] >= len(grid[0]):
      return (None, False)
    if newPos in seen:
      return (None, False)
    currChar = getElevation(currPos)
    newChar = getElevation(newPos)
    if ord(currChar) <= ord(newChar) + 1:
      seen.add(newPos)
      return (newPos, True)
    return (None, False)

  while True:
    currDist, currPos = heapq.heappop(queue)

    if getElevation(currPos) == 'a':
      return currDist

    nextDist = currDist + 1

    for pair in ((-1, 0), (1, 0), (0, 1), (0, -1)):
      x, y = pair
      newPos, ok = canVisit(currPos, x, y)
      if ok:
        heapq.heappush(queue, (nextDist, newPos))


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
