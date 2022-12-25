def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def part1(name):
  lines = read_file(name)
  zero_id = -1
  original = []
  for i, line in enumerate(lines):
    num = int(line)
    if num == 0:
      zero_id = i
    original.append((i, num))
  dec = original.copy()

  length = len(original)
  for pair in original: 
    _, num = pair
    index = dec.index(pair)
    dec.pop(index)
    new_index = (index + num) % (length - 1)
    dec.insert(new_index, pair)

  total = 0
  zero_index = dec.index((zero_id, 0))
  for num in (1000, 2000, 3000):
    temp = dec[(num + zero_index) % length]
    total += temp[1]
  return total

def part2(name):
  lines = read_file(name)
  zero_id = -1
  original = []
  for i, line in enumerate(lines):
    num = int(line) * 811589153
    if num == 0:
      zero_id = i
    original.append((i, num))
  dec = original.copy()

  length = len(original)
  for _ in range(10):
    for pair in original: 
      _, num = pair
      index = dec.index(pair)
      dec.pop(index)
      new_index = (index + num) % (length - 1)
      dec.insert(new_index, pair)

  total = 0
  zero_index = dec.index((zero_id, 0))
  for num in (1000, 2000, 3000):
    temp = dec[(num + zero_index) % length]
    total += temp[1]
  return total

print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
