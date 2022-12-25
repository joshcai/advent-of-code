import functools
import re

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

class Monkey:

  def __init__(self, monkeys, num=None, op=None, first=None, second=None, humn=False):
    self.monkeys = monkeys
    self.num = num
    self.op = op
    self.first = first
    self.second = second
    self.humn = humn
  
  def value(self):
    if self.num is not None:
      return self.num
    val1 = self.monkeys.compute(self.first) 
    if self.humn and self.first == 'humn':
      val1 = '(humn)'
    val2 = self.monkeys.compute(self.second)
    if self.humn and self.second == 'humn':
      val2 = '(humn)'
    expr = f'({val1} {self.op} {val2})'
    if 'humn' in expr:
      return expr
    return int(eval(expr))


class Monkeys:

  def __init__(self):
    self.compute.cache_clear()
    self.monkeys = {}

  def set_monkeys(self, monkeys):
    self.monkeys = monkeys

  def get_monkey(self, name):
    return self.monkeys[name]

  @functools.cache
  def compute(self, name):
    return self.monkeys[name].value()


def part1(name):
  lines = read_file(name)

  monkeys = Monkeys()
  monkeys_dict = {}
  for line in lines:
    monkey_name, token2 = line.split(':')
    token2 = token2.strip()
    num = None
    op = None
    first = None
    second = None
    token2 = token2.split(' ')
    if len(token2) == 1:
      num = int(token2[0])
    else:
      first, op, second = token2
    monkeys_dict[monkey_name] = Monkey(monkeys, num, op, first, second)
  monkeys.set_monkeys(monkeys_dict)

  return monkeys.compute('root')

def part2(name):
  lines = read_file(name)

  monkeys = Monkeys()
  monkeys_dict = {}
  for line in lines:
    monkey_name, token2 = line.split(':')
    token2 = token2.strip()
    num = None
    op = None
    first = None
    second = None
    token2 = token2.split(' ')
    if len(token2) == 1:
      num = int(token2[0])
    else:
      first, op, second = token2
    monkeys_dict[monkey_name] = Monkey(monkeys, num, op, first, second, humn=True)
  monkeys.set_monkeys(monkeys_dict)

  root = monkeys.get_monkey('root')
  expression = monkeys.compute(root.first)
  final = monkeys.compute(root.second)

  while True:
    if expression == '(humn)':
      return final
    if not expression.startswith('(') or not expression.endswith(')'):
      raise ValueError('not wrapped with parentheses')
    expression = expression[1:-1]
    match = re.match(r'(?P<first>(\(.*\))|\d+) (?P<op>\*|/|\+|-) (?P<second>(\(.*\))|\d+)', expression)
    first = match.group('first')
    op = match.group('op')
    second = match.group('second')
    if expression.startswith('('):
      num = int(second)
      expression = first
      if op == '+':
        final -= num
      elif op == '-':
        final += num
      elif op == '*':
        final //= num
      elif op == '/':
        final *= num
      else:
        raise ValueError('invalid op')
    else:
      num = int(first)
      expression = second
      if op == '+':
        final -= num
      elif op == '-':
        final = num - final
      elif op == '*':
        final //= num
      elif op == '/':
        final = num // final
      else:
        raise ValueError('invalid op')


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))

