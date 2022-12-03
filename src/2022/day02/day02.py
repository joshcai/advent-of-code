def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines


SCORE_MAP = {
  'X': 1,  # Rock
  'Y': 2,  # Paper
  'Z': 3,  # Scissors
}

OTHER_MAP = {
  'A': 1,  # Rock
  'B': 2,  # Paper
  'C': 3,  # Scissors
}

WIN_MAP = {
  'A': 2,
  'B': 3,
  'C': 1,
}

LOSE_MAP = {
  'A': 3,
  'B': 1,
  'C': 2,
}

PART2_SCORE_MAP = {
  'X': 0,  # Lose
  'Y': 3,  # Draw
  'Z': 6,  # Win
}


def part1(name):
  lines = read_file(name)
  score = 0
  for line in lines:
    first, second = line.split(' ')
    theirs = OTHER_MAP[first]
    mine = SCORE_MAP[second]
    score += mine
    if mine == theirs:
      score += 3
    if (mine > theirs and mine - theirs == 1) or (mine == 1 and theirs == 3):
      score += 6
  return score


def part2(name):
  lines = read_file(name)
  score = 0
  for line in lines:
    first, second = line.split(' ')
    score += PART2_SCORE_MAP[second]
    if second == 'Y':
      score += OTHER_MAP[first]
    elif second == 'X':
      score += LOSE_MAP[first]
    else:
      score += WIN_MAP[first]

  return score


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
