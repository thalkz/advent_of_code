import re

tunnels = {}
released_per_min = {}
useful_valves = 0

# Parse input
for line in open("in.txt"):
    valves = re.findall("[A-Z]{2}", line)
    tunnels[valves[0]] = valves[1:]
    flowMatches = re.findall("[0-9]+", line)
    released_per_min[valves[0]] = int(flowMatches[0])
    if int(flowMatches[0]) > 0:
        useful_valves += 1

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


# Search all paths
scores = set()
maxScore = 0

def walk(you, elefant, visited, you_time, elefant_time, score):
    global maxScore
    # print(you_time,  elefant_time)
    if (you_time >= 26 and elefant_time >= 26) or len(visited) >= useful_valves:
        print(visited,  score)
        if score > maxScore:
            print(new_visited,  score)
            maxScore = score
        return

    if you_time <= elefant_time and you_time < 26:
        for next in distances[you]:
            if next not in visited:
                duration = distances[you][next] + 1
                new_time = you_time + duration
                new_visited = visited.copy()
                new_visited.append(next)
                if new_time >= 26:
                    # scores.add(score)
                    if score > maxScore:
                        print(new_visited,  score)
                        maxScore = score
                    return
                released = released_per_min[next] * (26 - new_time)
                walk(next, elefant, new_visited, new_time,
                     elefant_time, score + released)
    elif elefant_time < 26:
        for next in distances[elefant]:
            if next not in visited:
                duration = distances[elefant][next] + 1
                new_time = elefant_time + duration
                new_visited = visited.copy()
                new_visited.append(next)
                if new_time >= 26:
                    # print(new_visited,  score)
                    if score > maxScore:
                        print(new_visited,  score)
                        maxScore = score
                    return
                released = released_per_min[next] * (26 - new_time)
                walk(you, next, new_visited, you_time,
                     new_time, score + released)


walk("AA", "AA", [], 0, 0, 0)

print(maxScore)
