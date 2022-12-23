import re

f = open("in.txt").read().split("\n\n")
op = f[1]
board = {}
y = 0
me = None

for line in f[0].split("\n"):
    y += 1
    x = 0
    chars = list(line)
    for char in chars:
        x += 1
        if char != " ":
            board[(x, y)] = char
            if me == None:
                me = [x, y]

distances = [int(x) for x in re.findall("[0-9]+", op)]
turns = re.findall("[RL]", op)
turns.append("L")
# print(op)
# print(distances)
# print(turns)

# print(op)
# print(board)
# print("start", start)
ri = 0

r = [(1, 0), (0, 1), (-1, 0), (0, -1)]

for i in range(len(distances)):
    print("move forward", distances[i])
    for _ in range(distances[i]):
        pos = [me[0] + r[ri][0], me[1] + r[ri][1]]

        if tuple(pos) not in board:
            # Wrap around
            pos[0] -= r[ri][0]
            pos[1] -= r[ri][1]
            while tuple(pos) in board:
                pos[0] -= r[ri][0]
                pos[1] -= r[ri][1]
                print("move back to", pos)
            pos[0] += r[ri][0]
            pos[1] += r[ri][1]
            print("wrap around to", pos)

        if board[tuple(pos)] == ".":
            me = pos
            print("me", me)
        else:
            # Rock
            break

    if turns[i] == "R":
        ri = (ri + 1) % 4
        print("rotate right to", r[ri])
    else:
        ri = (ri - 1) % 4
        print("rotate left to", r[ri])

row = me[1]
col = me[0]
facing = (ri + 1) % 4
print("row", row)
print("col", col)
print("facing", facing)
print("final value", row * 1000 + col * 4 + facing)