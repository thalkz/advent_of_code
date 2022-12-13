def parse(input) -> list:
    # print("parsing", input)
    if input == "":
        return []

    result = []
    i = 0
    while i < len(input):
        if input[i] == "[":
            end = i
            depth = 1
            while input[end] != "]" or depth != 0:
                end += 1
                if input[end] == "[":
                    depth += 1
                if input[end] == "]":
                    depth -= 1
            result.append(parse(input[i+1:end]))
            i = end
        elif input[i] != ",":
            # print(input[i])
            end = i
            while end < len(input) and input[end] != ",":
                end += 1
            result.append(int(input[i:end]))
            i = end
        i += 1

    return result


def compare(left, right):
    if left == right:
        return None
    if type(left) is int and type(right) is int:
        print(f"Compare {left} < {right} !")
        return left < right
    elif type(left) is int and type(right) is list:
        return compare([left], right)
    elif type(left) is list and type(right) is int:
        return compare(left, [right])
    elif type(left) is list and type(right) is list:
        i = 0
        while True:
            if i == len(left) and i == len(right):
                return None
            elif i == len(left):
                print("Left ran out of items !")
                return True
            elif i == len(right):
                print("Right ran out of items !")
                return False
            result = compare(left[i], right[i])
            if result != None:
                return result
            i += 1


f = open("small.txt").read().split("\n")
total = 0
for i in range(0, len(f), 3):
    leftStr = f[i]
    rightStr = f[i+1]

    left = parse(leftStr[1:len(leftStr)-1])
    right = parse(rightStr[1:len(rightStr)-1])

    print("comparing")
    print(left)
    print(right)
    ok = compare(left, right)
    if ok:
        print("Correct !")
        index = int(i/3 + 1)
        total += index
    else:
        print("Incorrect !")
    print()

print("total", total)
