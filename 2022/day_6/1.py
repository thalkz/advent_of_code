def check_buffer(buffer):
    return len(set(buffer)) == 4

with open("in.txt") as f:
    signal = list(f.read())
    # print(signal)

    buffer = []

    for i, char in enumerate(signal):
        buffer.append(char)
        if check_buffer(buffer):
            print(buffer)
            print(i+1)
            break
        if len(buffer) >= 4:
            buffer.pop(0)
