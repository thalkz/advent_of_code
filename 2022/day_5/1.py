with open("in.txt") as f:
    parts = f.read().split("\n\n")

    crates = parts[0].split("\n")

    stacks = []
    maxStack = (len(crates[0]) + 1) // 4
    for stackIndex in range(0, maxStack):
        stacks.append([])
        for i in range(len(crates) - 2, -1, -1):
            line = crates[i]
            # print("line:", line)
            char = line[1 + 4 * stackIndex]
            # print("char:", char)
            if char != " ":
                stacks[stackIndex].append(char)

    print(stacks)

    instructions = parts[1].split("\n")
    for instruction in instructions:
        tokens = instruction.split(" ")
        count = int(tokens[1])
        fromStack = int(tokens[3]) - 1
        toStack = int(tokens[5]) - 1

        for i in range(0, count):
            stacks[toStack].append(stacks[fromStack].pop())

    for i in range(0, len(stacks)):
        print(stacks[i].pop(), end="")