import functools


def parse(input) -> list:
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

def compare_wrapper(left, right):
    if compare(left, right):
        return -1
    else:
        return 1


# Parse
f = open("in.txt").read().split("\n")
packets = []
for line in f:
    if line != "":
        packets.append(parse(line[1:len(line)-1]))

packets = sorted(packets, key=functools.cmp_to_key(compare_wrapper))

mul = 1
for i in range(len(packets)):
    print(packets[i])
    if packets[i] == [[6]] or packets[i] == [[2]]:
        mul *= (i + 1)

print(mul)

# for i in range(len(packets)-1):
#     if compare(packets[i+1],  packets[i]) == compare(packets[i],  packets[i+1]):
#         print("compare fails for")
#         print(packets[i+1])
#         print(packets[i])
