cmds = []
for line in open("in.txt").read().split("\n"):
    parts = line.split(" ")
    cmds.append((parts[0], int(parts[1])))

head = {"x": 0, "y": 0}
tail = {"x": 0, "y": 0}
visited = []
for c in cmds:
    print(c)
    for _ in range(c[1]):
        if c[0] == "U":
            head["y"] += 1
            if head["y"] - tail["y"] > 1:
                tail["y"] += 1
                tail["x"] = head["x"]

        elif c[0] == "D":
            head["y"] -= 1
            if tail["y"] - head["y"] > 1:
                tail["y"] -= 1
                tail["x"] = head["x"]

        elif c[0] == "R":
            head["x"] += 1
            if head["x"] - tail["x"] > 1:
                tail["x"] += 1
                tail["y"] = head["y"]

        elif c[0] == "L":
            head["x"] -= 1
            if tail["x"] - head["x"] > 1:
                tail["x"] -= 1
                tail["y"] = head["y"]

        visited.append((tail["x"], tail["y"]))
        for y in range(6, -1, -1):
            print()
            for x in range(6):
                current = {"x": x, "y": y}
                if head == current:
                    print("H", end="")
                elif tail == current:
                    print("T", end="")
                else:
                    print(".", end="")
        print()
        print()

print(len(set(visited)))
