rocks = set()

def print_board():
    for y in range(0, 200):
        print()
        for x in range(400, 600):
            if (x, y) == (500, 0):
                print("+", end="")
            elif (x, y) in rocks:
                print("#", end="")
            else:
                print(".", end="")
    print()

# parse input
for line in open("in.txt"):
    cornersStr = line.split(" -> ")
    corners = [[int(k) for k in c.split(",")] for c in cornersStr]

    for i in range(0, len(corners) - 1):
        (x1, y1) = corners[i]
        (x2, y2) = corners[i+1]
        if x1 == x2:
            yMin = min(y1, y2)
            yMax = max(y1, y2)
            for y in range(yMin, yMax+1):
                rocks.add((int(x1), y))
        else:
            xMin = min(x1, x2)
            xMax = max(x1, x2)
            for x in range(xMin, xMax+1):
                rocks.add((x, int(y1)))

maxDepth = max([y for (_, y) in rocks]) + 1

print_board()
def gen():
    (x, y) = (500,0)
    while y < maxDepth:
        if (x, y+1) not in rocks:
            y += 1
        elif (x - 1, y+1) not in rocks:
            x-=1
            y+=1
        elif (x + 1, y+1) not in rocks:
            x += 1
            y+=1
        else:
            return (x, y)


total = 0

while True:
    sand = gen()
    if sand == None:
        break
    else:
        rocks.add(sand)
    total += 1

print_board()

print(total)