def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


def group_lines(name):
  lines = read_file(name)
  lines.append('')
  groups = []
  curr = []
  for line in lines:
    if not line:
      groups.append(curr)
      curr = []
    else:
      curr.append(line)
  return groups


class Monkey:

  def __init__(self, starting_items, op, divisible, true_monkey, false_monkey):
    self.inspected = 0
    self.items = starting_items
    # op is just a string, we'll replace it with the number and then eval
    self.op = op
    self.divisible = divisible
    self.true_monkey = true_monkey
    self.false_monkey = false_monkey
    self.mod = None
    self.divide = None

  def inspect(self):
    if not self.items:
      return None
    item = self.items.pop(0)
    self.inspected += 1
    op = self.op.replace('old', str(item))
    item = eval(op)
    if self.divide is not None:
      item //= self.divide
    if self.mod is not None:
      item = item % self.mod
    if item % self.divisible == 0:
      return (self.true_monkey, item)
    return (self.false_monkey, item)

  def add_item(self, item):
    self.items.append(item)

  def set_mod(self, mod):
    self.mod = mod

  def set_divide(self, divide):
    self.divide = divide


def removeprefix(line, prefix):
  if line.startswith(prefix):
    line = line[len(prefix):]
  return line


def part1(name):
  groups = group_lines(name)

  monkeys = []
  for group in groups:
    lines = group
    starting = removeprefix(lines[1], '  Starting items: ').split(', ')
    starting = [int(x) for x in starting]
    op = removeprefix(lines[2], '  Operation: new = ')
    divisible = int(lines[3].split(' ')[-1])
    true_monkey = int(lines[4].split(' ')[-1])
    false_monkey = int(lines[5].split(' ')[-1])
    monkeys.append(Monkey(starting, op, divisible, true_monkey, false_monkey))
    monkeys[-1].set_divide(3)

  for _ in range(20):
    for monkey in monkeys:
      result = monkey.inspect()

      while result is not None:
        throw_to, item = result
        monkeys[throw_to].add_item(item)
        result = monkey.inspect()

  inspected = [monkey.inspected for monkey in monkeys]
  inspected.sort(reverse=True)

  return inspected[0] * inspected[1]


def part2(name):
  groups = group_lines(name)

  monkeys = []
  for group in groups:
    lines = group
    starting = removeprefix(lines[1], '  Starting items: ').split(', ')
    starting = [int(x) for x in starting]
    op = removeprefix(lines[2], '  Operation: new = ')
    divisible = int(lines[3].split(' ')[-1])
    true_monkey = int(lines[4].split(' ')[-1])
    false_monkey = int(lines[5].split(' ')[-1])
    monkeys.append(Monkey(starting, op, divisible, true_monkey, false_monkey))
  prod = 1

  for monkey in monkeys:
    prod *= monkey.divisible
  for monkey in monkeys:
    monkey.set_mod(prod)

  for i in range(10000):
    for monkey in monkeys:
      result = monkey.inspect()

      while result is not None:
        throw_to, item = result
        monkeys[throw_to].add_item(item)
        result = monkey.inspect()

  inspected = [monkey.inspected for monkey in monkeys]
  inspected.sort(reverse=True)

  return inspected[0] * inspected[1]


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
