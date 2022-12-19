import numpy as np

size = 30

droplet = np.zeros((size,size,size), dtype=int)

# 0 is air
# 1 is rock
# 2 is steam

for line in open("in.txt"):
    (x, y, z) = [int(val) for val in line.split(",")]
    droplet[z+1][y+1][x+1] = 1 # Rock

total = 0

adjs = [
    (1, 0, 0),
    (-1, 0, 0),
    (0, 1, 0),
    (0, -1, 0),
    (0, 0, 1),
    (0, 0, -1),
]

def print_droplet():
    for z in droplet:
        print()
        for y in z:
            print()
            for x in y:
                print(x, end="")

def adjacent_idx(idx):
    return [[sum(x) for x in zip(idx, adj)] for adj in adjs]

def at(idx):
    if idx[0] >= size or idx[1] >= size or idx[2] >= size or idx[0] < 0 or idx[1] < 0 or idx[2] < 0:
        return 2
    return droplet[idx[2]][idx[1]][idx[0]]

droplet[:, 0, :] = 2
droplet[:, -1, :] = 2
droplet[:, :, 0] = 2
droplet[:, :, -1] = 2
droplet[-1, :, :] = 2
droplet[0, :, :] = 2


changed = True
while changed:
    changed = False
    for idx, x in np.ndenumerate(droplet):
        if at(idx) == 2:
            for adj_idx in adjacent_idx(idx):
                if at(adj_idx) == 0:
                    droplet[adj_idx[2]][adj_idx[1]][adj_idx[0]] = 2
                    changed = True

# print_droplet()

for idx, x in np.ndenumerate(droplet):
    if at(idx) == 1:
        for adj_idx in adjacent_idx(idx):
            if at(adj_idx) == 2:
                total += 1

print()
print("total",total)
