import collections
import re

def read_file(name):
  with open(name) as file:
    lines = [line.rstrip() for line in file]
  return lines

class Node:

  def __init__(self, name, flow, connections):
    self.name = name
    self.flow = flow
    self.connections = connections

def get_state(curr, next):
  state = curr.split(',')
  state = [x for x in state if x]
  state.append(next)
  return ','.join(sorted(state))

def part1(name):
  lines = read_file(name)
  
  # V1 -> V2 -> distance
  dist = collections.defaultdict(dict)
  nodes = {}
  for line in lines:
    m = re.match(r'Valve\s(?P<node>\w\w).*rate\=(?P<flow>\d+).*valve(s)?\s(?P<connections>.*)', line)
    node = m.group('node')
    flow = int(m.group('flow'))
    connections = m.group('connections').split(', ')
    nodes[node] = Node(node, flow, connections)
    dist[node][node] = 0
    for connection in connections:
      dist[node][connection] = 1
      dist[connection][node] = 1

  def at(i, j):
    return dist[i].get(j, 1e8)

  # Use Floyd-Warshall to compute the shortest distances.
  for k in nodes:
    for i in nodes:
      for j in nodes:
        if at(i, j) > at(i, k) + at(k, j):
          dist[i][j] = at(i, k) + at(k, j)


  nodes = {
    k: v for k, v in nodes.items() if v.flow > 0
  }

  # From state -> maximum flow.
  # state is an alphabetized list of node names.
  max_flow = 0
  # Based on https://www.reddit.com/r/adventofcode/comments/zn6k1l/2022_day_16_solutions/j0hrdpt/
  def visit(node, budget, state, flow):
    nonlocal max_flow
    if flow > max_flow:
      max_flow = flow

    for next in nodes:
      if node == next:
        continue
      if next in state:
        continue
      next_budget = budget - dist[node][next] - 1
      if next_budget < 0:
        continue
      next_node = nodes[next]
      next_flow = flow + next_node.flow * next_budget
      visit(next, next_budget, get_state(state, next), next_flow)
  visit('AA', 30, '', 0)
  return max_flow

def part2(name):
  lines = read_file(name)
  
  # V1 -> V2 -> distance
  dist = collections.defaultdict(dict)
  nodes = {}
  for line in lines:
    m = re.match(r'Valve\s(?P<node>\w\w).*rate\=(?P<flow>\d+).*valve(s)?\s(?P<connections>.*)', line)
    node = m.group('node')
    flow = int(m.group('flow'))
    connections = m.group('connections').split(', ')
    nodes[node] = Node(node, flow, connections)
    dist[node][node] = 0
    for connection in connections:
      dist[node][connection] = 1
      dist[connection][node] = 1

  def at(i, j):
    return dist[i].get(j, 1e8)

  # Use Floyd-Warshall to compute the shortest distances.
  for k in nodes:
    for i in nodes:
      for j in nodes:
        if at(i, j) > at(i, k) + at(k, j):
          dist[i][j] = at(i, k) + at(k, j)


  nodes = {
    k: v for k, v in nodes.items() if v.flow > 0
  }

  # From state -> maximum flow.
  # state is an alphabetized list of node names.
  # Dictionary from state -> maximum flow. Used to compute
  # the maximum between elephant and me.
  max_flows = {}
  # Based on https://www.reddit.com/r/adventofcode/comments/zn6k1l/2022_day_16_solutions/j0hrdpt/
  def visit(node, budget, state, flow):
    max_flows[state] = max(max_flows.get(state, 0), flow)

    for next in nodes:
      if node == next:
        continue
      if next in state:
        continue
      next_budget = budget - dist[node][next] - 1
      if next_budget < 0:
        continue
      next_node = nodes[next]
      next_flow = flow + next_node.flow * next_budget
      visit(next, next_budget, get_state(state, next), next_flow)
  visit('AA', 26, '', 0)

  def no_intersection(state1, state2):
    s1 = state1.split(',')
    s2 = state2.split(',')
    for s in s1:
      if s in s2:
        return False
    return True

  max_combined = 0
  for state1, flow1 in max_flows.items():
    for state2, flow2 in max_flows.items():
      if no_intersection(state1, state2):
        if flow1 + flow2 > max_combined:
          max_combined = flow1 + flow2

  return max_combined

print('Part 1')
print(part1('test'))
print(part1('input'))

print('Part 2')
print(part2('test'))
print(part2('input'))
