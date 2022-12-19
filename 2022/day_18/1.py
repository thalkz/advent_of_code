import numpy as np

size = 30

droplet = np.zeros((size,size,size), dtype=bool)

for line in open("in.txt"):
    (x, y, z) = [int(val) for val in line.split(",")]
    droplet[z][y][x] = True

# print(droplet[2])

total = 0

adjs = [
    (1, 0, 0),
    (-1, 0, 0),
    (0, 1, 0),
    (0, -1, 0),
    (0, 0, 1),
    (0, 0, -1),
]

def adjacent_idx(idx):
    return [[sum(x) for x in zip(idx, adj)] for adj in adjs]

print(adjacent_idx((1,1,1)))

for idx, x in np.ndenumerate(droplet):
    if droplet[idx[2]][idx[1]][idx[0]]:
        for adj_idx in adjacent_idx(idx):
            # print("idx", idx)
            # print("adj_idx", adj_idx)
            if not droplet[adj_idx[2]][adj_idx[1]][adj_idx[0]]:
                total += 1

print(total)