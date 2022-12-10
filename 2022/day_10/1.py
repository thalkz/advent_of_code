def tick(cycle, signal, x):
    cycle += 1
    if (cycle + 20) % 40 == 0:
        signal += cycle * x
        print(cycle, x, " -> ", cycle * x)
    return cycle, signal

x = 1
cycle = 0
signal = 0

for line in open("in.txt").read().split("\n"):
    parts = line.split(" ")

    if parts[0] == "noop":
        cycle, signal = tick(cycle, signal, x)
    elif parts[0] == "addx":
        cycle, signal = tick(cycle, signal, x)
        cycle, signal = tick(cycle, signal, x)
        x += int(parts[1])

print(signal)
