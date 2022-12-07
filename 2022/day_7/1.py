def update_pwd(pwd, dir):
    match dir:
        case "/":
            pwd = [""]
        case "..":
            pwd.pop()
        case _:
            pwd.append(dir)


def to_path(pwd, name):
    return '/'.join(pwd)+'/'+name


with open("in.txt") as f:
    lines = f.read().split("\n")
    sizes = {}
    dirs = {}
    pwd = [""]

    for line in lines:
        tokens = line.split(" ")
        if tokens[0] == "$":
            # print(line)
            match tokens[1]:
                case "cd":
                    update_pwd(pwd, tokens[2])
                    print(pwd)
                case "ls":
                    dirs['/'.join(pwd)+"/"] = 0
        elif tokens[0] == "dir":
            pass
        else:
            size = int(tokens[0])
            path = '/'.join(pwd)+"/"+tokens[1]
            sizes[path] = size

    total = 0
    for dirpath in dirs:
        for filepath in sizes:
            if dirpath == filepath[:len(dirpath)]:
                print(dirpath, filepath[:len(dirpath)], "OK")
                dirs[dirpath] += sizes[filepath]

    # for dir in dirs:
    #     print(dir , dirs[dir])

    total = 0

    for dirpath in dirs:
        if dirs[dirpath] <= 100000:
            total += dirs[dirpath]

    print("TOTAL", total)
