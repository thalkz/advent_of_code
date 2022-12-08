def compute_view(trees, originX, originY):
    height = trees[originY][originX]
    # print("origin height", height)

    a = 0
    for x in range(originX+1, len(trees[0])):
        a += 1
        if trees[originY][x] >= height:
            break

    b = 0
    for x in range(originX-1, -1, -1):
        b += 1
        if trees[originY][x] >= height:
            break

    c = 0
    for y in range(originY+1, len(trees)):
        c += 1
        if trees[y][originX] >= height:
            break

    d = 0
    for y in range(originY-1, -1, -1):
        d += 1
        if trees[y][originX] >= height:
            break

    return a, b, c, d


lines = open("in.txt").read().split("\n")
trees = [[int(x) for x in [*line]] for line in lines]
visible = []

scores = []
for y in range(len(trees)):
    for x in range(len(trees[0])):
        a, b, c, d = compute_view(trees, x, y)
        # print((x, y), "->", a, b, c, d)
        scores.append(a * b * c * d)

print(max(scores))
