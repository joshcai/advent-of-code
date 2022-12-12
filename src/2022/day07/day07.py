import collections


def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

class Node:

  def __init__(self, parent=None):
    self.children = {}
    self.size = 0
    # Reference to parent node.
    self.parent = parent
  

def part1(name):
  root = Node()
  lines = read_file(name)

  curr = root
  curr_size = 0
  for line in lines:
    tokens = line.split(' ')
    if tokens[0] == '$':
      if tokens[1] == 'ls':
        continue

      curr.size += curr_size
      curr_size = 0
      if tokens[2] == '/':
        curr = root
      elif tokens[2] == '..':
        curr = curr.parent
      else:
        next_dir = tokens[2]
        if next_dir not in curr.children:
          curr.children[next_dir] = Node(parent=curr)
        curr = curr.children[next_dir]
    elif tokens[0] == 'dir':
      next_dir = tokens[1]
      if next_dir not in curr.children:
        curr.children[next_dir] = Node(parent=curr)
    else:
      curr_size += int(tokens[0])
  curr.size += curr_size
      
  final = 0
  def dfs(node):
    nonlocal final
    if len(node.children) == 0:
      if node.size < 100000:
        final += node.size
      return node.size
    temp = 0
    for child_node in node.children.values():
      temp += dfs(child_node)
    node_final_size = temp + node.size
    if node_final_size < 100000:
      final += node_final_size
    return node_final_size
    
  dfs(root)
  
  return final
    
          
      
def part2(name):
  root = Node()
  lines = read_file(name)

  curr = root
  curr_size = 0
  for line in lines:
    tokens = line.split(' ')
    if tokens[0] == '$':
      if tokens[1] == 'ls':
        continue

      curr.size += curr_size
      curr_size = 0
      if tokens[2] == '/':
        curr = root
      elif tokens[2] == '..':
        curr = curr.parent
      else:
        next_dir = tokens[2]
        if next_dir not in curr.children:
          curr.children[next_dir] = Node(parent=curr)
        curr = curr.children[next_dir]
    elif tokens[0] == 'dir':
      next_dir = tokens[1]
      if next_dir not in curr.children:
        curr.children[next_dir] = Node(parent=curr)
    else:
      curr_size += int(tokens[0])
  curr.size += curr_size
      
  final = []
  def dfs(node):
    nonlocal final
    if len(node.children) == 0:
      final.append(node.size)
      return node.size
    temp = 0
    for child_node in node.children.values():
      temp += dfs(child_node)
    node_final_size = temp + node.size
    final.append(node_final_size)
    return node_final_size
    
  all_space = dfs(root)

  final = sorted(final)
  for num in final:
    if all_space - num <= 40000000:
      return num

  return 0
    


print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
