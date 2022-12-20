import re
import numpy as np


class Blueprint:
    id: int
    robot_costs: list

    def __init__(self, id, ore_robot_cost, clay_robot_cost, obsidian_robot_cost, geode_robot_cost) -> None:
        self.id = id
        self.robot_costs = [ore_robot_cost,
                            clay_robot_cost,
                            obsidian_robot_cost,
                            geode_robot_cost]

    def __str__(self) -> str:
        return f"Blueprint #{self.id}: ore robot cost={self.robot_costs[0]}, clay robot cost={self.robot_costs[1]}, obsidian robot cost={self.robot_costs[2]}, geode robot cost={self.robot_costs[3]}"


class Factory:
    blueprint: Blueprint
    robots: list[int]
    resources: list[int]
    time: int
    best_robot: int

    def __init__(self, blueprint, robots, resources, time) -> None:
        self.blueprint = blueprint
        self.robots = robots
        self.resources = resources
        self.time = time
        self.best_robot = 0

    def mine(self) -> None:
        self.resources = self.resources + self.robots
        self.time += 1

    def can_build(self, robot_id) -> bool:
        return (self.resources >= self.blueprint.robot_costs[robot_id]).all()

    def should_build(self, robot_id) -> bool:
        if robot_id == 0:
            return self.robots[0] < max(self.blueprint.robot_costs[1][0], self.blueprint.robot_costs[2][0])
        elif robot_id == 1:
            return self.robots[1] < self.blueprint.robot_costs[2][1] and self.resources[1] < self.blueprint.robot_costs[2][1]+3
        elif robot_id == 2:
            return self.robots[2] < self.blueprint.robot_costs[3][2] and self.resources[2] < self.blueprint.robot_costs[3][2]+2
        elif robot_id == 3:
            return True

    def build(self, robot_id) -> bool:
        self.resources = self.resources - self.blueprint.robot_costs[robot_id]
        self.robots[robot_id] += 1
        if robot_id >= self.best_robot:
            # Each robot allow to build the next
            self.best_robot = min(robot_id+1, 3)

    def time_is_up(self) -> bool:
        return self.time >= 32

    def copy(self):
        return Factory(self.blueprint, self.robots.copy(), self.resources.copy(), self.time)

    def robots_score(self) -> int:
        return self.robots[0] + 1000 * self.robots[1] + 1000000 * self.robots[2] + 100000000 * self.robots[3]

    def resources_score(self) -> int:
        return self.resources[0] + 1000 * self.resources[1] + 1000000 * self.resources[2] + 100000000 * self.resources[3]

    def __str__(self) -> str:
        return f"Factory #{self.blueprint.id}: robots={self.robots} resources={self.resources} time={self.time}"


call_count = 0


def step(factory: Factory) -> int:
    global visited
    global call_count
    call_count += 1

    if factory.time_is_up():
        return factory.resources[3]

    result = [0]

    if factory.can_build(3):
        factory.mine()
        factory.build(3)
        result.append(step(factory))
    else:
        should_mine_more = False
        for id in range(3):
            if id <= factory.best_robot and factory.should_build(id):
                if factory.can_build(id):
                    a = factory.copy()
                    a.mine()
                    a.build(id)
                    result.append(step(a))
                else:
                    should_mine_more = True
        if should_mine_more:
            factory.mine()
            result.append(step(factory))
    return max(result)


qualities = []
for line in open("in2.txt"):
    numbers = [int(k) for k in re.findall("[0-9]+", line)]
    blueprint = Blueprint(numbers[0],
                          np.array([numbers[1], 0, 0, 0]),
                          np.array([numbers[2], 0, 0, 0]),
                          np.array([numbers[3], numbers[4], 0, 0]),
                          np.array([numbers[5], 0, numbers[6], 0]))

    print(blueprint)
    robots = np.array([1, 0, 0, 0])
    resources = np.array([0, 0, 0, 0])
    time = 0
    factory = Factory(blueprint, robots, resources, time)

    geodes = step(factory)
    print("geodes", geodes)
    print("call_count", call_count)
    qualities.append(geodes)

print("total qualities", qualities[0] * qualities[1] * qualities[2])
