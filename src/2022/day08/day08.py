def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)

  grid = []
  visible = []
  for line in lines:
    grid.append([int(x) for x in line])
    visible.append([False for x in line])

  for i, row in enumerate(grid):
    currMax = -1
    for j, num in enumerate(row):
      if num > currMax:
        visible[i][j] = True
        currMax = num

  for i, row in enumerate(grid):
    currMax = -1
    for j in range(len(row) - 1, -1, -1):
      num = row[j]
      if num > currMax:
        visible[i][j] = True
        currMax = num

  for j in range(len(grid[0])):
    currMax = -1
    for i in range(len(grid)):
      num = grid[i][j]
      if num > currMax:
        visible[i][j] = True
        currMax = num

  for j in range(len(grid[0])):
    currMax = -1
    for i in range(len(grid) - 1, -1, -1):
      num = grid[i][j]
      if num > currMax:
        visible[i][j] = True
        currMax = num

  count = 0
  for row in visible:
    for val in row:
      if val:
        count += 1

  return count


def part2(name):
  lines = read_file(name)

  grid = []
  for line in lines:
    grid.append([int(x) for x in line])

  def view(currHeight, x, y, dx, dy):
    temp = 0
    while True:
      newX, newY = x + dx, y + dy
      if newX < 0 or newX >= len(grid):
        return temp
      if newY < 0 or newY >= len(grid[0]):
        return temp
      temp += 1
      if grid[newX][newY] >= currHeight:
        return temp
      x, y = newX, newY

  maxScore = 0
  for i, row in enumerate(grid):
    for j, num in enumerate(row):
      curr = 1
      curr *= view(num, i, j, 0, 1)
      curr *= view(num, i, j, 0, -1)
      curr *= view(num, i, j, 1, 0)
      curr *= view(num, i, j, -1, 0)
      if curr > maxScore:
        maxScore = curr

  return maxScore


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
