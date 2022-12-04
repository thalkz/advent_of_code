f = open("in.txt")
lines = f.read().split("\n")

total = 0
for line in lines:
    [a, b] = line.split(",")
    [a1, a2] = a.split("-")
    [b1, b2] = b.split("-")
    if (int(a1) >= int(b1) and int(a2) <= int(b2)) or (int(a1) <= int(b1) and int(a2) >= int(b2)):
        total += 1

print(total)