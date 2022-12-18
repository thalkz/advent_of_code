rocks = [
    [(0, 0), (1, 0), (2, 0), (3, 0)],
    [(1, 0), (0, 1), (1, 1), (2, 1), (1, 2)],
    [(0, 0), (1, 0), (2, 0), (2, 1), (2, 2)],
    [(0, 0), (0, 1), (0, 2), (0, 3)],
    [(0, 0), (0, 1), (1, 0), (1, 1)],
]

jets = open("in.txt").read()


def generate_jets(jets):
    i = 0

    def next():
        nonlocal i
        if i >= len(jets):
            i = 0
        jet = jets[i]
        i += 1
        if jet == ">":
            print("push right")
            return (1, 0)
        else:
            print("push left")
            return (-1, 0)
    return next


def generate_rocks(rocks):
    i = 0
    def next():
        nonlocal i
        if i >= len(rocks):
            i = 0
        rock = rocks[i]
        i += 1
        print("new rock !")
        return rock
    return next


next_jet = generate_jets(jets)
next_rock = generate_rocks(rocks)

board = {(x, -1) for x in range(7)}

def print_board(board, flying_rock, maxY):
    flying_rock_cells = used_cells(flying_rock)
    print()
    for y in range(maxY, -1, -1):
        print("|", end="")
        for x in range(7):
            if (x, y) in board:
                print("#", end="")
            elif (x, y) in flying_rock_cells:
                print("@", end="")
            else:
                print(".", end="")
        print("|")
    print("+-------+")


def used_cells(flying_rock):
    return [(x+flying_rock["pos"][0], y+flying_rock["pos"][1])
            for (x, y) in flying_rock["shape"]]


def move(flying_rock, direction):
    return {"shape": flying_rock["shape"], "pos": (flying_rock["pos"][0]+direction[0], flying_rock["pos"][1]+direction[1])}

def highest_cell(board):
    max = -1
    for cell in board:
        if cell[1] > max:
            max = cell[1]
    return max

def can_move(rock, board):
    for cell in used_cells(rock):
        if cell[0] >= 7 or cell[0] < 0 or cell in board:
            return False
    return True


for i in range(3):
    flying_rock = {"shape": next_rock(), "pos": (2, highest_cell(board) + 4)}
    print_board(board, flying_rock, 10)
    print("highest_cell", highest_cell(board))
    while True:
        after_jet_rock = move(flying_rock, next_jet())
        if can_move(after_jet_rock, board):
            flying_rock = after_jet_rock

        moved_flying_rock = move(flying_rock, (0, -1))
        if can_move(moved_flying_rock, board):
            flying_rock = moved_flying_rock
        else:
            break
    print("stop moving")
    for cell in used_cells(flying_rock):
        board.add(cell)

print(highest_cell(board)+1)