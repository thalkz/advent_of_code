import re

tunnels = {}
released_per_min = {}

# Parse input
for line in open("in.txt"):
    valves = re.findall("[A-Z]{2}", line)
    tunnels[valves[0]] = valves[1:]
    flowMatches = re.findall("[0-9]+", line)
    released_per_min[valves[0]] = int(flowMatches[0])

# Compute all distances
distances = {}
for position in tunnels:
    local_distances = {}
    visited = set()
    queue = [(position, 0)]

    while len(queue) > 0:
        pos, distance = queue.pop(0)
        for next in tunnels[pos]:
            if next not in visited:
                visited.add(next)
                queue.append((next, distance+1))
                if released_per_min[next] > 0:
                    local_distances[next] = distance+1

    distances[position] = local_distances

print(distances, "distances")
print(released_per_min)

# Search all paths
scores = set()
def walk(position, visited, time, score):
    if time >= 30:
        scores.add(score)
        return
    
    score += released_per_min[position] * (30 - time)

    for next in distances[position]:
        if next not in visited:
            new_visited = visited.copy()
            new_visited.add(next)
            new_time = time + distances[position][next] + 1
            walk(next, new_visited, new_time, score)

walk("AA", set(), 0,  0)

print(max(scores))
