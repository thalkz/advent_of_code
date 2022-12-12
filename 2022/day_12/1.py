def right(pos):
    return (pos[0]+1, pos[1])


def left(pos):
    return (pos[0]-1, pos[1])


def top(pos):
    return (pos[0], pos[1]-1)


def bot(pos):
    return (pos[0], pos[1]+1)


def is_reachable(board, start, end) -> bool:
    if end[1] < 0 or end[0] >= len(board[0]) or end[1] < 0 or end[1] >= len(board):
        return False
    else:
        return ord(board[start[1]][start[0]]) + 1 - ord(board[end[1]][end[0]]) >= 0


def add(pQueue: dict, pos, dist):
    print(f"adding {pos}:{dist}")
    if pos in pQueue and pQueue[pos] < dist:
        # We already have the shortest distance
        return
    pQueue[pos] = dist


def get_next(pQueue: dict):
    # print(f"before {pQueue}")
    s = sorted(pQueue.items(), key=lambda item: item[1])
    # print(f"next is {s[0]}")
    del pQueue[s[0][0]]
    return s[0]

def search(board, start, end):
    visited = []
    pQueue = {}
    dist = 0
    pos = start

    while True:
        print(f"visiting {pos} (dist = {dist})")
        if pos == end:
            print(f"Found the end in {dist} steps")
            return

        # print(visited)
        visited.append(pos)

        if is_reachable(board, pos, right(pos)) and right(pos) not in visited:
            # print("try right")
            add(pQueue, right(pos), dist+1)

        if is_reachable(board, pos, left(pos)) and left(pos) not in visited:
            # print("try left")
            add(pQueue, left(pos), dist+1)

        if is_reachable(board, pos, top(pos)) and top(pos) not in visited:
            # print("try top")
            add(pQueue, top(pos), dist+1)

        if is_reachable(board, pos, bot(pos)) and bot(pos) not in visited:
            # print("try bot")
            add(pQueue, bot(pos), dist+1)

        # print(f"priorityQueue is {pQueue}")
        pos, dist = get_next(pQueue)


def print_board(board):
    for row in board:
        print()
        for cell in row:
            print(cell, end="")
    print()


def main():
    visited = []
    board = []
    for line in open("in.txt"):
        board.append(list(filter(lambda c: c != "\n", list(line))))

    start = (0, 0)
    end = (0, 0)
    for y in range(len(board)):
        for x in range(len(board[0])):
            if board[y][x] == "S":
                start = (x, y)
                board[y][x] = "a"
            elif board[y][x] == "E":
                end = (x, y)
                board[y][x] = "z"

    print("board size is", len(board[0]) * len(board))
    print(f"start is at {start}, and end {end}")
    # print_board(board)

    search(board, start, end)


main()
