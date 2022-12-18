import numpy as np


def generate_jets(jets):
    i = 0

    def next():
        nonlocal i
        if i >= len(jets):
            i = 0
        jet = jets[i]
        i += 1
        if jet == ">":
            return (1, 0)
        else:
            return (-1, 0)
    return next


def generate_rocks(rocks):
    i = 0

    def next(init_position: tuple):
        nonlocal i
        if i >= len(rocks):
            i = 0
        rock = rocks[i]
        i += 1
        new_rock = []
        for cell in rock:
            new_rock.append((cell[0] + init_position[0],
                            cell[1] + init_position[1]))
        return new_rock
    return next


jets = open("in.txt").read()
next_jet = generate_jets(jets)

rocks = [
    [(0, 0), (1, 0), (2, 0), (3, 0)],
    [(1, 0), (0, 1), (1, 1), (2, 1), (1, 2)],
    [(0, 0), (1, 0), (2, 0), (2, 1), (2, 2)],
    [(0, 0), (0, 1), (0, 2), (0, 3)],
    [(0, 0), (0, 1), (1, 0), (1, 1)],
]
next_rock = generate_rocks(rocks)


def print_board(board, flying_rock, minY, maxY):
    print()
    for y in range(maxY, minY, -1):
        print("|", end="")
        for x in range(7):
            if board[y][x]:
                print("#", end="")
            elif (x, y) in flying_rock:
                print("@", end="")
            else:
                print(".", end="")
        print("|")
    print("+-------+")


def can_move(rock, board, move_direction):
    for cell in rock:
        x = cell[0] + move_direction[0]
        y = cell[1] + move_direction[1]
        if y < 0 or x >= 7 or x < 0 or board[y][x]:
            return False
    return True


def move(rock, direction):
    for i in range(len(rock)):
        rock[i] = (rock[i][0] + direction[0], rock[i][1] + direction[1])


def main():
    h = -1
    down = (0, -1)

    board = np.zeros((1_000_000, 7))
    rocks_count = 0
    jets_count = 0
    last = 0
    rounds = 0
    delta_h = 0
    total_rounds = 1000000000000
    skipped_h = 0
    last_round = 0

    while rounds < total_rounds:
        flying_rock = next_rock((2, h + 4))
        rocks_count += 1

        while True:
            direction = next_jet()
            jets_count += 1
            if can_move(flying_rock, board, direction):
                move(flying_rock, direction)

            if can_move(flying_rock, board, down):
                move(flying_rock, down)
            else:
                break

        for cell in flying_rock:
            board[cell[1]][cell[0]] = True
            if cell[1] > h:
                h = cell[1]

        if jets_count % len(jets) == 0:
            print_board(board, flying_rock, h - 5, h+5)
            print("height", h+1)
            print("since last", h - last)
            if delta_h == h - last:
                print(f"repetition (round={rounds})")
                delta_round = rounds - last_round
                skips = int((total_rounds - rounds) / delta_round)
                skipped_rounds = skips * delta_round
                skipped_h = skips * delta_h
                total_rounds = total_rounds - skipped_rounds
                print("skipped_rounds", skipped_rounds)
                print("total", total_rounds)
                delta_h = 0
            else:
                delta_h = h - last
                last = h
                last_round = rounds
                rounds += 1
            # print(f"jet counts is 0 (at h = {h}, {rocks_count})")
            # if rocks_count % len(rocks) == 0:
            #     print_board(board, flying_rock, -1, 10)
            #     print("height", h+1)
            #     break
        else:
            rounds += 1
    print("computed rounds =", rounds)
    print("skipped rounds =", skipped_rounds)
    print("total rounds =", skipped_rounds + rounds)
    print("h =", h)
    print("skipped_h =", skipped_h)
    print("total_h =", h + skipped_h)


main()


# 2690 + 379650720 * 2634 + 830 = 1_000_000_000_000

# First = 2690
# Loop = 379650720 * 2634
# End = 830
