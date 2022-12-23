def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def part1(name):
  lines = read_file(name)
  total = 0

  val = 1
  cycle = 1

  def add_signal_strength():
    nonlocal total, cycle, val
    if (cycle - 20) % 40 == 0:
      total += cycle * val

  for line in lines:
    tokens = line.split(' ')
    inst = tokens[0]
    if inst == 'noop':
      cycle += 1
    elif inst == 'addx':
      cycle += 1
      add_signal_strength()
      cycle += 1
      val += int(tokens[1])
    else:
      raise ValueError('invalid instruction')

    add_signal_strength()

  return total


def part2(name):
  lines = read_file(name)

  values = ['.'] * 240

  val = 1
  cycle = 1

  def draw_pixel():
    nonlocal cycle, val, values
    if abs(cycle % 40 - val - 1) <= 1:
      values[cycle - 1] = '#'

  for line in lines:
    tokens = line.split(' ')
    inst = tokens[0]
    draw_pixel()
    if inst == 'noop':
      cycle += 1
    elif inst == 'addx':
      cycle += 1
      draw_pixel()
      cycle += 1
      val += int(tokens[1])
    else:
      raise ValueError('invalid instruction')

  draw_pixel()

  for i in range(6):
    line = ''.join(values[i * 40:(i + 1) * 40])
    print(line)


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
part2('test')
part2('input')
