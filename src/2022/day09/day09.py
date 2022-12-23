def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  # Set of all visited by the tail.
  all_visited = set()

  tail = (0, 0)
  head = (0, 0)
  all_visited.add(tail)
  for line in lines:
    direction, num = line.split(' ')
    num = int(num)

    for _ in range(num):
      if direction == 'R':
        head = (head[0] + 1, head[1])
      elif direction == 'L':
        head = (head[0] - 1, head[1])
      elif direction == 'D':
        head = (head[0], head[1] - 1)
      elif direction == 'U':
        head = (head[0], head[1] + 1)
      else:
        raise ValueError('not supported direction')

      xDiff = head[0] - tail[0]
      yDiff = head[1] - tail[1]
      if abs(xDiff) == 2 or abs(yDiff) == 2:
        xDenom = abs(xDiff) if xDiff else 1
        signedXDiff = xDiff // xDenom
        tail = (tail[0] + signedXDiff, tail[1])

        yDenom = abs(yDiff) if yDiff else 1
        signedYDiff = yDiff // yDenom
        tail = (tail[0], tail[1] + signedYDiff)
      all_visited.add(tail)

  return len(all_visited)


def part2(name):
  lines = read_file(name)
  # Set of all visited by the tail.
  all_visited = set()

  rope = [(0, 0)] * 10
  for line in lines:
    direction, num = line.split(' ')
    num = int(num)

    for _ in range(num):
      head = rope[0]
      if direction == 'R':
        rope[0] = (head[0] + 1, head[1])
      elif direction == 'L':
        rope[0] = (head[0] - 1, head[1])
      elif direction == 'D':
        rope[0] = (head[0], head[1] - 1)
      elif direction == 'U':
        rope[0] = (head[0], head[1] + 1)
      else:
        raise ValueError('not supported direction')

      for i in range(9):
        head = rope[i]
        tail = rope[i + 1]
        xDiff = head[0] - tail[0]
        yDiff = head[1] - tail[1]
        if abs(xDiff) == 2 or abs(yDiff) == 2:
          xDenom = abs(xDiff) if xDiff else 1
          signedXDiff = xDiff // xDenom
          rope[i + 1] = (tail[0] + signedXDiff, tail[1])
          tail = rope[i + 1]

          yDenom = abs(yDiff) if yDiff else 1
          signedYDiff = yDiff // yDenom
          rope[i + 1] = (tail[0], tail[1] + signedYDiff)

      all_visited.add(rope[-1])

  return len(all_visited)


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('test2'))
print(part2('input'))
