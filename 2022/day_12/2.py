def edges(pos):
    return [(pos[0]+1, pos[1]), (pos[0]-1, pos[1]), (pos[0], pos[1]-1), (pos[0], pos[1]+1)]

def is_reachable(board, start, end) -> bool:
    if end[1] < 0 or end[0] >= len(board[0]) or end[1] < 0 or end[1] >= len(board):
        return False
    else:
        return ord(board[start[1]][start[0]]) + 1 - ord(board[end[1]][end[0]]) >= 0


def search(board, start, end):
    visited = []
    queue = []
    dist = 0
    pos = start

    while True:
        if pos == end:
            print(f"Found the end in {dist} steps")
            return dist

        for edge in edges(pos):
            if is_reachable(board, pos, edge) and edge not in visited:
                visited.append(edge)
                queue.append((edge, dist+1))

        if len(queue) == 0:
            return None
        pos, dist = queue.pop(0)


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

    # print("board size is", len(board[0]) * len(board))
    # print_board(board)

    all = []

    for y in range(len(board)):
        for x in range(len(board[0])):
            if board[y][x] == "a":
                # print(f"start at {(x, y)}")
                dist = search(board, (x, y), end)
                if dist != None:
                    all.append(dist)
    print(sorted(all)[0])


main()
