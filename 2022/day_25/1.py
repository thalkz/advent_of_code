def to_decimal(input) -> int:
    l = reversed(list(input))
    base = 1
    result = 0
    for symbol in l:
        if symbol == "=":
            result += (-2) * base
        elif symbol == "-":
            result += (-base)
        elif symbol == "0":
            result += 0
        elif symbol == "1":
            result += base
        elif symbol == "2":
            result += 2 * base
        base *= 5
    return result


def to_snafu(d) -> str:
    result = []
    plusone = False
    while d > 0 or plusone:
        if plusone:
            d = d + 1
        val = d % 5
        if val == 3:
            result.append("=")
            plusone = True
        elif val == 4:
            result.append("-")
            plusone = True
        else:
            result.append(val)
            plusone = False

        d = int((d - val) / 5)
    return "".join((str(x) for x in list(reversed(result))))


total = 0
for line in open(input()):
    line = line.strip()
    d = to_decimal(line)
    print(f"{line} -> {d}")
    # if to_snafu(d) != line:
        # print(f"failed to reconvert {to_snafu(d)} != {line}")
    total += d

print("total", total)
print("total in snafu", to_snafu(total))
