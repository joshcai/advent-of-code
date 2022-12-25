def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

HORIZONTAL_ROCK = [
  (0, 0),
  (1, 0),
  (2, 0),
  (3, 0),
]

T_ROCK = [
  (0, 1),
  (1, 1),
  (2, 1),
  (1, 0),
  (1, 2),
]

L_ROCK = [
  (0, 0),
  (1, 0),
  (2, 0),
  (2, 1),
  (2, 2),
]

VERTICAL_ROCK = [
  (0, 0),
  (0, 1),
  (0, 2),
  (0, 3),
]

SQUARE_ROCK = [
  (0, 0),
  (0, 1),
  (1, 0),
  (1, 1),
]

def add(tuples):
  x = 0
  y = 0
  for tup in tuples:
    x += tup[0]
    y += tup[1]
  return x, y

class Rock:

  def __init__(self, pos, form):
    self.pos = pos
    self.form = form

  def height(self):
    maxY = 0
    for f in self.form:
      if f[1] > maxY:
        maxY = f[1]
    return self.pos[1] + maxY + 1

  def free(self, blocks, direction):
    for f in self.form:
      next_pos = add([self.pos, f, direction])
      next_x, next_y = next_pos
      if next_x < 0 or next_x > 6:
        return False
      if next_y < 0:
        return False
      if next_pos in blocks:
        return False

    return True
  
  def move(self, direction):
    self.pos = add([self.pos, direction])
    # print('new pos',self.pos)

  def curr_positions(self):
    positions = []
    for f in self.form:
      positions.append(add([f, self.pos]))
    return positions


class Jet:

  def __init__(self, jets):
    self.jets = jets
    self.length = len(jets)
    self.counter = 0
  
  def get_next(self):
    direction = self.jets[self.counter % self.length]
    self.counter += 1
    if direction == '<':
      return (-1, 0)
    return (1, 0)
  
def visualize(blocks, height):
  for i in range(height, -1, -1):
    next = ''
    for j in range(7):
      if (j, i) in blocks:
        next += '#'
      else:
        next += '.'
    print(next)
  

def part1(name):
  lines = read_file(name)
  jet = Jet(lines[0])
  rock_forms = [
    HORIZONTAL_ROCK,
    T_ROCK,
    L_ROCK,
    VERTICAL_ROCK,
    SQUARE_ROCK,
  ]
  blocks = set()
  curr_height = 0
  for i in range(2022):
    # if i == 2000:
    #   visualize(blocks, curr_height)
    #   print('hello')
    curr_rock_form = rock_forms[i % 5]
    rock = Rock((2, curr_height + 3), curr_rock_form)
    while True:
      next_dir = jet.get_next()
      if rock.free(blocks, next_dir):
        rock.move(next_dir)
      down_dir = (0, -1)
      if rock.free(blocks, down_dir):
        rock.move(down_dir)
      else:
        break
    for position in rock.curr_positions():
      blocks.add(position)
    curr_height = max(curr_height, rock.height())
  return curr_height

def repeating(cache, heights):
  if len(heights) < 50:
    return False
  key = ''.join([str(x) for x in heights[-50:]])
  if key in cache:
    return cache[key]
  cache[key] = len(heights)
  return False

  # l = len(heights)
  # if l % 2 != 0:
  #   return False
  # return heights[:l//2] == heights[l//2:]

def part2(name):
  lines = read_file(name)
  jet = Jet(lines[0])
  rock_forms = [
    HORIZONTAL_ROCK,
    T_ROCK,
    L_ROCK,
    VERTICAL_ROCK,
    SQUARE_ROCK,
  ]
  blocks = set()
  curr_height = 0
  delta_heights = []
  i = 0
  cache = {}
  step_to_height = {}
  while True:
    if last_seen := repeating(cache, delta_heights):
      cycle_steps = i - last_seen

      total_per_cycle = curr_height - step_to_height[last_seen]
      
      remaining_cycles = 1000000000000 - i
      final = curr_height
      final += remaining_cycles // cycle_steps * total_per_cycle
      for i in range(remaining_cycles % cycle_steps):
        final += delta_heights[last_seen + i]
      return final
    curr_rock_form = rock_forms[i % 5]
    rock = Rock((2, curr_height + 3), curr_rock_form)
    while True:
      next_dir = jet.get_next()
      if rock.free(blocks, next_dir):
        rock.move(next_dir)
      down_dir = (0, -1)
      if rock.free(blocks, down_dir):
        rock.move(down_dir)
      else:
        break
    for position in rock.curr_positions():
      blocks.add(position)
    prev_curr_height = curr_height
    curr_height = max(curr_height, rock.height())
    step_to_height[i] = curr_height
    delta_heights.append(curr_height - prev_curr_height)
    i += 1






print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
