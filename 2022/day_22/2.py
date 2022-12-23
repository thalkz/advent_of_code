import re
import numpy as np

cube_size = 50
filename = "in.txt"

# Faces
# ..1.
# 234.
# ..56

# faces = [(2, 0), (0, 1), (1, 1), (2, 1), (2, 2), (3, 2)]
faces = [(1, 0), (2, 0), (1, 1), (0, 2), (1, 2), (0, 3)]
tl = [(x*cube_size, y*cube_size) for (x, y) in faces]
print(tl)


def get_face(pos: tuple):
    x = int(pos[0] / cube_size)
    y = int(pos[1] / cube_size)
    for i in range(len(faces)):
        if (x, y) == faces[i]:
            return i


# On which face we arrive when
# moving past the R/D/L/U edge
# adj = np.array([
#     # R  D  L  U
#     [6, 4, 3, 2],  # 1
#     [3, 5, 6, 1],  # 2
#     [4, 5, 2, 1],  # 3
#     [6, 5, 3, 1],  # 4
#     [6, 2, 3, 4],  # 5
#     [1, 2, 5, 4]  # 6
# ])
adj = np.array([
    # R  D  L  U
    [2, 3, 4, 6],  # 1
    [5, 3, 1, 6],  # 2
    [2, 5, 4, 1],  # 3
    [5, 6, 1, 3],  # 4
    [2, 6, 4, 3],  # 5
    [5, 2, 1, 4]  # 6
])

# How many clockwise rotation is
# required when crossing the edge
# rot = np.array([
#     # R  D  L  U
#     [2, 0, 1, 2],  # 1
#     [0, 2, 3, 2],  # 2
#     [0, 1, 0, 3],  # 3
#     [3, 0, 0, 0],  # 4
#     [0, 2, 3, 0],  # 5
#     [2, 1, 0, 1]  # 6
# ])
rot = np.array([
    # R  D  L  U
    [0, 0, 2, 3],  # 1
    [2, 3, 0, 0],  # 2
    [1, 0, 1, 0],  # 3
    [0, 0, 2, 3],  # 4
    [2, 3, 0, 0],  # 5
    [1, 0, 1, 0]  # 6
])


f = open(filename).read().split("\n\n")
op = f[1]
board = {i: np.empty((cube_size, cube_size), dtype=bool) for i in range(6)}

# print(board)

global_board = set()

y = 0
for line in f[0].split("\n"):
    x = 0
    print()
    chars = list(line)
    for char in chars:
        if char == " ":
            print(" ", end="")
        else:
            is_rock = (char == "#")
            face = get_face((x, y))
            face_x = x - tl[face][0]
            face_y = y - tl[face][1]
            print(face, end="")
            board[face][face_y][face_x] = is_rock
            if is_rock:
                global_board.add((x, y))
        x += 1
    y += 1


def print_face(face, rot, local_pos):
    print(f"--- Face {face+1} (rot={rot}) ---")
    print(local_pos)
    arr = np.rot90(board[face], -rot)
    for y in range(cube_size):
        for x in range(cube_size):
            if arr[y][x]:
                print("#", end="")
            elif x == local_pos[0] and y == local_pos[1]:
                print("X", end="")
            else:
                print(".", end="")
        print()

distances = [int(x) for x in re.findall("[0-9]+", op)]
turns = re.findall("[RL]", op)

# print(distances)
# print(turns)

ri = 0
r = [(1, 0), (0, 1), (-1, 0), (0, -1)]


def cross_edge(current_face, current_rotation, dir_index):
    current_rotation += rot[current_face][dir_index]
    current_rotation = current_rotation % 4
    current_face = adj[current_face][dir_index] - 1
    print("moved to face", current_face+1)
    print("current world rotation", current_rotation)
    return current_face, current_rotation


def to_global(current_face, current_rotation, local_pos):
    x = tl[current_face][0]
    y = tl[current_face][1]
    z = np.zeros((cube_size, cube_size), dtype=bool)
    # print("local_pos", local_pos)
    z[local_pos[1]][local_pos[0]] = True
    # print("before", z)
    rotated = np.rot90(z, current_rotation)
    # print("rotated", rotated)
    indice = np.argwhere(rotated == True)
    # print("indice", indice)
    return [tl[current_face][0]+indice[0][1], tl[current_face][1]+indice[0][0]]


local_me = [0, 0]
current_face = 0
current_rotation = 0
global_path = []

for i in range(len(distances)):
    print("move forward", distances[i])
    for _ in range(distances[i]):
        local_pos = [local_me[0] + r[ri][0], local_me[1] + r[ri][1]]

        new_face = current_face
        new_rotation = current_rotation
        dir_index = -1

        if local_pos[0] >= cube_size:
            # exit right
            print("exit right")
            local_pos = [0, local_pos[1]]
            dir_index = (0-current_rotation) % 4
        elif local_pos[1] >= cube_size:
            # exit bottom
            print("exit bottom")
            local_pos = [local_pos[0], 0]
            dir_index = (1-current_rotation) % 4
        elif local_pos[0] < 0:
            # exit left
            print("exit left")
            local_pos = [cube_size-1, local_pos[1]]
            dir_index = (2-current_rotation) % 4
        elif local_pos[1] < 0:
            # exit top
            print("exit top")
            local_pos = [local_pos[0], cube_size-1]
            dir_index = (3-current_rotation) % 4

        if dir_index != -1:
            new_face, new_rotation = cross_edge(
                current_face, current_rotation, dir_index)

        if np.rot90(board[new_face], -new_rotation)[local_pos[1]][local_pos[0]]:
            print("found a rock")
            break
        else:
            local_me = local_pos
            current_face = new_face
            current_rotation = new_rotation
            print("move to", local_pos, "on face", current_face+1)
            # print_face(current_face, current_rotation, local_me)
            global_pos = to_global(current_face, current_rotation, local_me)
            # print("global_pos", global_pos)
            global_path.append(tuple(global_pos))

    if i < len(turns):
        if turns[i] == "R":
            ri = (ri + 1) % 4
            print("rotate right to", r[ri])
        else:
            ri = (ri - 1) % 4
            print("rotate left to", r[ri])

# for y in range(cube_size*4):
#     print()
#     for x in range(cube_size*3):
#         if get_face((x, y)) == None:
#             print(" ", end="")
#         elif (x, y) in global_board:
#             print("#", end="")
#         elif (x, y) in global_path:
#             print("X", end="")
#         else:
#             print(".", end="")
# print()

print("current_rotation", current_rotation)
print("current_face", current_face)

global_pos = to_global(current_face, current_rotation, local_me)
row = global_pos[1]+1
col = global_pos[0]+1
facing = (ri-current_rotation) % 4
print("row", row)
print("col", col)
print("facing", facing)
print("final value", row * 1000 + col * 4 + facing)
