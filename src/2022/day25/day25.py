def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

def to_dec(num: str):
  power = 1
  final = 0
  for i, c in enumerate(reversed(num)):
    if c == '-':
      mult = -1
    elif c == '=':
      mult = -2
    else:
      mult = int(c)
    final += power * mult
    power *= 5

  return final

def to_snafu(num: str):
  num = int(num)
  power = 1
  while power <= num:
    power *= 5
  remain = num
  parts = []
  while power != 1:
    power //= 5
    curr = remain // power
    parts.append(curr)
    remain -= curr * power
  rev = list(reversed(parts))
  final_rev = []
  for i in range(len(rev) + 1):
    if i == len(rev):
      break
    part = rev[i]
    if part <= 2:
      final_rev.append(str(part))
      continue
    if i + 1 == len(rev):
      rev.append(1)
    else:
      rev[i+1] += 1
    if part == 3:
      final_rev.append('=')
    elif part == 4:
      final_rev.append('-')
    elif part == 5:
      final_rev.append('0')
    else:
      raise ValueError('invalid num')

  final = list(reversed(final_rev))
  return ''.join(final)


def part1(name):
  lines = read_file(name)
  total = 0
  for line in lines:
    total += to_dec(line)

  return to_snafu(total)

print('Part 1')
print(part1('test'))
print(part1('input'))


