import collections


def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  moves = False
  buffer = []
  stacks = collections.defaultdict(list)
  for line in lines:
    if not line:
      moves = True
      nums = buffer.pop()
      indices = {i: num for i, num in enumerate(nums) if num.strip()}
      for buf in reversed(buffer):
        for idx, num in indices.items():
          letter = buf[idx] if idx < len(buf) else ''
          if letter.strip():
            stacks[num].append(letter)
      continue

    if not moves:
      buffer.append(line)
    else:
      tokens = line.split(' ')
      num_moves = int(tokens[1])
      from_pos = tokens[3]
      to_pos = tokens[5]
      for i in range(num_moves):
        stacks[to_pos].append(stacks[from_pos].pop())

  final = ''
  for i in range(len(stacks)):
    final += stacks[str(i + 1)][-1]
  return final


def part2(name):
  lines = read_file(name)
  moves = False
  buffer = []
  stacks = collections.defaultdict(list)
  for line in lines:
    if not line:
      moves = True
      nums = buffer.pop()
      indices = {i: num for i, num in enumerate(nums) if num.strip()}
      for buf in reversed(buffer):
        for idx, num in indices.items():
          letter = buf[idx] if idx < len(buf) else ''
          if letter.strip():
            stacks[num].append(letter)
      continue

    if not moves:
      buffer.append(line)
    else:
      tokens = line.split(' ')
      num_moves = int(tokens[1])
      from_pos = tokens[3]
      to_pos = tokens[5]
      temp_buffer = []
      for i in range(num_moves):
        temp_buffer.append(stacks[from_pos].pop())
      for i in range(num_moves):
        stacks[to_pos].append(temp_buffer.pop())

  final = ''
  for i in range(len(stacks)):
    final += stacks[str(i + 1)][-1]
  return final


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
