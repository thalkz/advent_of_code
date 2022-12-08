lines = open("in.txt").read().split("\n")
trees = [[int(x) for x in [*line]] for line in lines]
visible = []

for y in range(len(trees)):
    left = -1
    for x in range(len(trees[0])):
        if trees[y][x] > left:
            visible.append((x, y))
            left = trees[y][x]

    right = -1
    for x in range(len(trees[0])-1, -1, -1):
        if trees[y][x] > right:
            visible.append((x, y))
            right = trees[y][x]

for x in range(len(trees[0])):
    top = -1
    for y in range(len(trees)):
        if trees[y][x] > top:
            visible.append((x, y))
            top = trees[y][x]

    bottom = -1
    for y in range(len(trees)-1, -1, -1):
        if trees[y][x] > bottom:
            visible.append((x, y))
            bottom = trees[y][x]

visibleSet = set(visible)
print(len(visibleSet))

# for y in range(len(trees)):
#     print()
#     for x in range(len(trees[0])):
#         if (x, y) in visibleSet:
#             print(trees[y][x], end="")
#         else:
#             print("*", end="")
