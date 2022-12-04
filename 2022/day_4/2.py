f = open("in.txt")
lines = f.read().split("\n")

def overlap(x1, x2, y1, y2):
    return (x1 <= y1 and x2 >= y1) or (y1 <= x1 and y2 >= x1)

total = 0
for line in lines:
    [a, b] = line.split(",")
    [a1, a2] = a.split("-")
    [b1, b2] = b.split("-")
    if overlap(int(a1), int(a2), int(b1), int(b2)):
        total += 1

print(total)