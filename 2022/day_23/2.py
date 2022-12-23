def rect(positions):
    xs = [x for (x, _) in positions]
    ys = [y for (_, y) in positions]
    return (min(xs), min(ys)), (max(xs), max(ys))


def available_space(positions):
    topleft, bottomright = rect(positions)
    total = 0
    for y in range(topleft[1], bottomright[1]+1):
        for x in range(topleft[0], bottomright[0]+1):
            if (x, y) not in positions:
                total += 1
    return total


def print_map(positions):
    topleft, bottomright = rect(positions)
    # print(topleft, bottomright)
    for y in range(topleft[1]-1, bottomright[1]+2):
        print()
        for x in range(topleft[0]-1, bottomright[0]+2):
            if (x, y) == (0, 0):
                print("0", end="")
            elif (x, y) in positions:
                print("#", end="")
            else:
                print(".", end="")
    print()


def is_free(elves, elve, dir):
    pos = [(elve[0] + x[0], elve[1] + x[1]) for x in dir]
    return pos[0] not in elves and pos[1] not in elves and pos[2] not in elves


ajdacent_cells = [
    (1, 1),
    (1, -1),
    (-1, -1),
    (-1, 1),
    (1, 0),
    (-1, 0),
    (0, 1),
    (0, -1),
]


def has_neighbourg(elves, elve):
    pos = [(elve[0] + x[0], elve[1] + x[1]) for x in ajdacent_cells]
    for p in pos:
        if p in elves:
            return True
    return False


elves = []
lines = open("in.txt").read().split("\n")
for y in range(len(lines)):
    for x in range(len(lines[y])):
        if lines[y][x] == "#":
            elves.append((x, y))

# print()
# print(f"== Initial State ==")
# print_map(elves)
# print("available_spaces", available_space(elves))

directions = [
    [(-1, -1), (0, -1), (1, -1)],
    [(-1, 1), (0, 1), (1, 1)],
    [(-1, -1), (-1, 0), (-1, 1)],
    [(1, -1), (1, 0), (1, 1)],
]

direction_names = ["N", "S", "W", "E"]
has_change = True
round = 0

while has_change:
    has_change = False
    # print()
    # print("Start round", round+1)
    # print("order is")
    # for i in range(4):
    #     print(direction_names[(i + di) % 4])

    intentions = {}
    for elve in elves:
        if not has_neighbourg(elves, elve):
            continue

        for i in range(4):
            di = (i + round) % 4
            dir = directions[di]
            if is_free(elves, elve, dir):
                # print(f"{elve} proposes to move {direction_names[di]}")
                intention = (elve[0] + dir[1][0], elve[1] + dir[1][1])
                if intention not in intentions:
                    intentions[intention] = []
                intentions[intention].append(elve)
                break
            # else:
                # print(f"{elve} cannot move {direction_names[di]}")

    for target, origin in intentions.items():
        if len(origin) == 1:
            # print(f"{origin[0]} moved to {target}")
            elves.remove(origin[0])
            elves.append(target)
            has_change = True
    round += 1

    # print()
    # print(f"== End of Round {round+1} ==")
    # print_map(elves)

print(round)
