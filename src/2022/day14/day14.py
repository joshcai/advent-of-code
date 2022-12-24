def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def pair(nums):
  a, b = nums.split(',')
  return int(a), int(b)

def part1(name):
  lines = read_file(name)

  max_height = 0
  # Tuple of all blocked positions, including rock and sand.
  blocked = set()
  for line in lines:
    tokens = line.split(' -> ')
    curr = pair(tokens.pop(0))
    for token in tokens:
      next = pair(token)
      a, b = curr
      c, d = next
      for i in range(min(a, c), max(a, c) + 1):
        for j in range(min(b, d), max(b, d) + 1):
          if j > max_height:
            max_height = j
          blocked.add((i, j))
      curr = next

  def below(curr):
    return (curr[0], curr[1] + 1)
  def left(curr):
    return (curr[0] - 1, curr[1] + 1)
  def right(curr):
    return (curr[0] + 1, curr[1] + 1)

  def available(pos):
    return pos not in blocked
  def spawn_sand():
    curr = (500, 0)
    while True:
      if curr[1] > max_height:
        return None
      b = below(curr)
      if available(b):
        curr = b
        continue
      l = left(curr)
      if available(l):
        curr = l
        continue
      r = right(curr)
      if available(r):
        curr = r
        continue
      return curr

  # Spawn sands for as long as possible.
  num_sands = 0
  while True:
    result = spawn_sand()
    if result is None:
      break
    num_sands += 1
    blocked.add(result)
  
  return num_sands

def part2(name):
  lines = read_file(name)

  max_height = 0
  # Tuple of all blocked positions, including rock and sand.
  blocked = set()
  for line in lines:
    tokens = line.split(' -> ')
    curr = pair(tokens.pop(0))
    for token in tokens:
      next = pair(token)
      a, b = curr
      c, d = next
      for i in range(min(a, c), max(a, c) + 1):
        for j in range(min(b, d), max(b, d) + 1):
          if j > max_height:
            max_height = j
          blocked.add((i, j))
      curr = next

  def below(curr):
    return (curr[0], curr[1] + 1)
  def left(curr):
    return (curr[0] - 1, curr[1] + 1)
  def right(curr):
    return (curr[0] + 1, curr[1] + 1)

  def available(pos):
    if pos[1] == max_height + 2:
      return False
    return pos not in blocked

  def spawn_sand():
    curr = (500, 0)
    if curr in blocked:
      return None
    while True:
      b = below(curr)
      if available(b):
        curr = b
        continue
      l = left(curr)
      if available(l):
        curr = l
        continue
      r = right(curr)
      if available(r):
        curr = r
        continue
      return curr

  # Spawn sands for as long as possible.
  num_sands = 0
  while True:
    result = spawn_sand()
    if result is None:
      break
    num_sands += 1
    blocked.add(result)
  
  return num_sands

print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
