import numpy as np
from collections import deque

# Bitwise values
me = 1
up = 2
down = 4
left = 8
right = 16
dirs = [up, down, left, right]
symbols = ["^", "v", "<", ">"]
coords = [(0, -1), (0, 1), (-1, 0), (1, 0)]

filename = input()
lines = open(filename).read().split("\n")
x_size = len(lines[0])-2
y_size = len(lines)-2
board = np.zeros((y_size, x_size), dtype=int)

all_boards = []

for y in range(y_size):
    for x in range(x_size):
        char = lines[y+1][x+1]
        if char != ".":
            board[y][x] = dirs[symbols.index(char)]


def print_board(board):
    for line in board:
        print()
        for val in line:
            if val == 0:
                print(".", end="")
            elif val == 1:
                print("X", env="")
            else:
                to_print = "."
                count = 0
                blizz = [val & x for x in dirs]
                for i in range(4):
                    if blizz[i] > 0:
                        to_print = symbols[i]
                        count += 1
                if count > 1:
                    to_print = count
                print(to_print, end="")
    print()


def step(board):
    new_board = np.zeros((y_size, x_size), dtype=int)
    for y in range(y_size):
        for x in range(x_size):
            val = board[y][x]
            for i in range(4):
                if val & dirs[i]:
                    new_x = (x + coords[i][0]) % x_size
                    new_y = (y + coords[i][1]) % y_size
                    # Should also works with: new_board[new_y][new_x] += val
                    new_board[new_y][new_x] += dirs[i]
    return new_board


# print("Initial")
# print_board(board)

all_boards.append(board)
board = step(board)
while not (board == all_boards[0]).all():
    all_boards.append(board)
    board = step(board)

loop = len(all_boards)

# print(len(all_boards))
# print_board(all_boards[400])


def traverse(start, entrypoint, out, start_dist) -> int:
    entry_dists = []
    for i in range(loop):
        dist = i + start_dist
        if all_boards[dist % loop][entrypoint[1]][entrypoint[0]] == 0:
            entry_dists.append(dist)
    visited = set()
    for i in entry_dists:
        q = deque()
        q.append((start, i))
        while True:
            if len(q) == 0:
                print(f"failed for entry dist={i}")
                break
            pos, dist = q.popleft()
            if pos == out:
                print(f"arrived at {pos} in {dist=}")
                print(f"answer is {dist+1}")
                exit()
            # print(f"currently at {pos} with {dist=}")
            for coord in coords:
                next_pos = (pos[0]+coord[0], pos[1]+coord[1])
                if next_pos[0] >= x_size or next_pos[0] < 0 or next_pos[1] >= y_size or next_pos[1] < 0:
                    continue
                if all_boards[(dist+1) % loop][next_pos[1]][next_pos[0]] == 0:
                    step = (next_pos, dist+1)
                    if step not in visited:
                        visited.add(step)
                        q.append(step)
            if pos != start and all_boards[(dist+1) % loop][pos[1]][pos[0]] == 0:
                q.append((pos, dist+1))


print(traverse(start=(0, -1), entrypoint=(0, 0),
      out=(x_size-1, y_size-1), start_dist=0))
