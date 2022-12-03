def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  final = 0
  for line in lines:
    l = len(line) // 2
    letter = list(set(line[:l]) & set(line[l:]))[0]
    num = ord(letter)
    if num < 97:
      final += num - 38
    else:
      final += num - 96
  return final


def part2(name):
  lines = read_file(name)
  current = None
  final = 0
  # counter += 0
  for line in lines:
    if not current:
      current = set(line)
      continue
    current = current & set(line)
    if len(current) != 1:
      continue
    letter = list(current)[0]
    current = None
    num = ord(letter)
    if num < 97:
      final += num - 38
    else:
      final += num - 96
  return final


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
