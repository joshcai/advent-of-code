def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  elves = []
  curr = 0
  for line in lines:
    if not line:
      elves.append(curr)
      curr = 0
    else:
      curr += int(line)
  return max(elves)


def part2(name):
  lines = read_file(name)
  elves = []
  curr = 0
  for line in lines:
    if not line:
      elves.append(curr)
      curr = 0
    else:
      curr += int(line)
  elves = sorted(elves, reverse=True)
  return elves[0] + elves[1] + elves[2]


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
