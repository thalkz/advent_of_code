def to_priority(n):
    value = ord(n)
    if value >= ord("a"):
        return value - ord("a") + 1
    else:
        return value - ord("A") + 27


with open("in.txt", "r+") as f:
    data = f.read().split("\n")
    total = 0

    for line in data:
        size = len(line)//2
        first = set(line[:size])
        last = set(line[size:])
        both = first & last
        item = both.pop()
        total += to_priority(item)
    
    print(total)
