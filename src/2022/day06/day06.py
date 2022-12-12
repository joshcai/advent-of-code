import collections


def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  assert len(lines) == 1
  return lines[0]


def part(msg, num):
  mapping = collections.defaultdict(int)
  for i in range(num):
    mapping[msg[i]] += 1
  for i in range(num, len(msg)):
    if len(mapping) == num:
      return i
    mapping[msg[i]] += 1
    mapping[msg[i - num]] -= 1
    copied = mapping.copy()
    for k, v in copied.items():
      if v == 0:
        del mapping[k]


def part1(msg):
  return part(msg, 4)


def part2(msg):
  return part(msg, 14)


print('Part 1')
print(part1('mjqjpqmgbljsphdztnvjfqwrcgsmlb'))
print(part1('bvwbjplbgvbhsrlpgdmjqwftvncz'))
print(part1('nppdvjthqldpwncqszvftbrmjlhg'))
print(part1('nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg'))
print(part1('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw'))
print(part1(read_file('input')))

print('Part 1')
print(part2('mjqjpqmgbljsphdztnvjfqwrcgsmlb'))
print(part2('bvwbjplbgvbhsrlpgdmjqwftvncz'))
print(part2('nppdvjthqldpwncqszvftbrmjlhg'))
print(part2('nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg'))
print(part2('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw'))
print(part2(read_file('input')))
