def tick(cycle, x):
    # print("During cycle", cycle, " - CTR draws at pixel", cycle-1)
    if abs(((cycle - 1) % 40) - x) <= 1:
        print("#", end="")
        # print("Draw #")
    else:
        print(".", end="")
        # print("Draw .")
    if cycle % 40 == 0:
        print()
    cycle += 1
    return cycle


x = 1
cycle = 1

for line in open("in.txt").read().split("\n"):
    parts = line.split(" ")

    # print("Start cycle", cycle, "- begin executing", line)
    if parts[0] == "noop":
        cycle = tick(cycle, x)
    elif parts[0] == "addx":
        cycle = tick(cycle, x)
        cycle = tick(cycle, x)
        x += int(parts[1])
        # print("End of cycle", cycle - 1, "- finish executing ",
            #   line, "(Register X is now", x, ")")
        # print()
