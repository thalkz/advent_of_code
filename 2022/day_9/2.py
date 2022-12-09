def update_head(direction, head):
    if direction == "U":
        head["y"] += 1
    elif direction == "D":
        head["y"] -= 1
    elif direction == "R":
        head["x"] += 1
    elif direction == "L":
        head["x"] -= 1


def clamp(num, min_value, max_value):
   return max(min(num, max_value), min_value)

def update_knot(head, tail):
    ydist = head["y"] - tail["y"]
    xdist = head["x"] - tail["x"]
    if ydist > 1:
        tail["y"] += 1
        tail["x"] += clamp(xdist, -1, 1)
    elif -ydist > 1:
        tail["y"] -= 1
        tail["x"] += clamp(xdist, -1, 1)
    elif xdist > 1:
        tail["x"] += 1
        tail["y"] += clamp(ydist, -1, 1)
    elif -xdist > 1:
        tail["x"] -= 1
        tail["y"] += clamp(ydist, -1, 1)

def print_simulation(rope):
    size = 20
    for y in range(size, -size, -1):
        print()
        for x in range(-size, size):
            val = '.'
            current = {"x": x, "y": y}
            for i in range(len(rope)):
                if rope[i] == current:
                    val = str(i)
                    if i == 0:
                        val = "H"
                    break
            print(val, end="")
    print()


cmds = []
for line in open("in.txt").read().split("\n"):
    parts = line.split(" ")
    cmds.append((parts[0], int(parts[1])))

rope = []
for i in range(10):
    rope.append({"x": 0, "y": 0})

visited = []
for c in cmds:
    # print()
    # print(c)
    for _ in range(c[1]):
        update_head(c[0], rope[0])
        for i in range(1, len(rope)):
            update_knot(rope[i-1], rope[i])
        visited.append((rope[9]["x"], rope[9]["y"]))
    # print_simulation(rope)

print(len(set(visited)))
