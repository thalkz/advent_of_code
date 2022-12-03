def to_priority(n):
    value = ord(n)
    if value >= ord("a"):
        return value - ord("a") + 1
    else:
        return value - ord("A") + 27


with open("in.txt", "r+") as f:
    data = f.read().split("\n")
    total = 0

    for i in range(0, len(data), 3):
        both = set(data[i]) & set(data[i+1]) & set(data[i+2])
        item = both.pop()
        total += to_priority(item)
    
    print(total)
