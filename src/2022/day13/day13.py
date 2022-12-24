from functools import cmp_to_key

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def compare(a, b):
  if isinstance(a, int) and isinstance(b, int):
    if a == b:
      return 0
    if a < b:
      return -1
    return 1
  if isinstance(a, int):
    a = [a]
  if isinstance(b, int):
    b = [b]
  
  for i, a_val in enumerate(a):
    if i >= len(b):
      return 1
    b_val = b[i]
    comparison = compare(a_val, b_val)
    if comparison:
      return comparison
  
  if len(a) == len(b):
    return 0
  if len(a) < len(b):
    return -1
  return 1


def part1(name):
  lines = read_file(name)
  lines.append('')
  pair = []
  pair_index = 0
  final = 0
  for line in lines:
    if line:
      pair.append(eval(line))
      continue
    pair_index += 1
    if compare(pair[0], pair[1]) < 0:
      final += pair_index
    pair = []
  return final

def part2(name):
  lines = read_file(name)
  all_lines = []
  all_lines.append([[2]])
  all_lines.append([[6]])
  for line in lines:
    if not line:
      continue
    all_lines.append(eval(line))
  all_lines.sort(key=cmp_to_key(compare))

  first_index = -1
  second_index = -1
  for i, line in enumerate(all_lines):
    if line == [[2]]:
      first_index = i
    if line == [[6]]:
      second_index = i
  return (first_index + 1) * (second_index + 1)
       
print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
