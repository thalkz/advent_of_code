import re

targetY = 2000000
covered = set()
coveredBeacons = set()
for line in open("in.txt"):
    # print(line)
    (sX, sY, bX, bY) = re.findall('-?[0-9]+', line)
    (sX, sY, bX, bY) = (int(sX), int(sY), int(bX), int(bY))

    if bY == targetY:
        coveredBeacons.add(bX)

    totalDistance = abs(bX - sX) + abs(bY - sY)
    # print(sX, sY, bX, bY)
    # print(f"{totalDistance=}")
    xDist = totalDistance - abs(targetY - sY)
    # print(f"{xDist=}")
    for x in  range(-xDist, xDist+1):
        # print(f"add {x}")
        covered.add(x+sX)

print(len(covered - coveredBeacons))