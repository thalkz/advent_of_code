import re

size = 4000000
sensors = set()
beacons = set()
for line in open("in.txt"):
    (sX, sY, bX, bY) = re.findall('-?[0-9]+', line)
    (sX, sY, bX, bY) = (int(sX), int(sY), int(bX), int(bY))
    totalDistance = abs(bX - sX) + abs(bY - sY)
    sensors.add((sX, sY, totalDistance))
    beacons.add((bX, bY))

# print(sensors)

for targetY in range(size+1):
    cover = set()
    for sensor in sensors:
        xDist = sensor[2] - abs(targetY - sensor[1])
        if xDist < 0:
            continue
        minX = max(0, -xDist+sensor[0])
        maxX = min(size, xDist+sensor[0])
        cover.add((minX, maxX))

    cover = sorted(cover, key=lambda item: item[0])

    # maxCovered = 0
    # for i in range(len(cover)-1):
    #     if cover[i][1] < maxCovered:
    #         continue
    #     if cover[i][1] + 1 < cover[i+1][0]:
    #         print(f"cover (y={targetY}):", cover)
    #         print(cover[i][1] + 1, '<' ,cover[i+1][0])
    #         break
    #     if cover[i][1] == size:
    #         break
    #     maxCovered = max(cover[i+1][1], cover[i][1])
    
    walker = 0
    while walker < size:
        found = False
        for c in cover:
            if walker >= c[0] and walker <= c[1]:
                found = True
                walker = c[1] + 1
        if not found:
            print(walker, targetY, "not covered !")
            print(walker*size + targetY)
