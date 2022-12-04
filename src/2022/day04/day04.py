def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  count = 0
  for line in lines:
    part1, part2 = line.split(',')
    a, b = part1.split('-')
    c, d = part2.split('-')
    a, b, c, d = int(a), int(b), int(c), int(d)
    if (a >= c and b <= d) or (c >= a and d <= b):
      count += 1
  return count


def part2(name):
  lines = read_file(name)
  count = 0
  for line in lines:
    part1, part2 = line.split(',')
    a, b = part1.split('-')
    c, d = part2.split('-')
    a, b, c, d = int(a), int(b), int(c), int(d)
    if (b >= c and b <= d) or (d >= a and d <= b):
      count += 1
  return count


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
