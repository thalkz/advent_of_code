import re
import numpy as np


class Blueprint:
    def __init__(self, id, ore_robot_cost, clay_robot_cost, obsidian_robot_cost, geode_robot_cost) -> None:
        self.id = id
        self.robot_costs = [ore_robot_cost,
                            clay_robot_cost,
                            obsidian_robot_cost,
                            geode_robot_cost]

    def __str__(self) -> str:
        return f"Blueprint #{self.id}: ore robot cost={self.robot_costs[0]}, clay robot cost={self.robot_costs[1]}, obsidian robot cost={self.robot_costs[2]}, geode robot cost={self.robot_costs[3]}"


class Factory:
    def __init__(self, blueprint, robots, resources, elapsed_time) -> None:
        self.blueprint = blueprint
        self.robots = robots
        self.resources = resources
        self.elasped_time = elapsed_time
        self.best_robot = 0

    def mine(self) -> None:
        self.resources = self.resources + self.robots
        self.elasped_time += 1

    def can_build(self, robot_id) -> bool:
        return (self.resources >= self.blueprint.robot_costs[robot_id]).all()

    def should_build(self, robot_id) -> bool:
        match robot_id:
            case 0:
                return self.robots[0] < max(self.blueprint.robot_costs[1][0], self.blueprint.robot_costs[2][0]) and self.resources[0] < 5
            case 1:
                return self.robots[1] < self.blueprint.robot_costs[2][1] and self.resources[1] < self.blueprint.robot_costs[2][1]
            case 2:
                return self.robots[2] < self.blueprint.robot_costs[3][2] and self.resources[2] < self.blueprint.robot_costs[3][2]
            case 3:
                return True

    def build(self, robot_id) -> bool:
        self.resources = self.resources - self.blueprint.robot_costs[robot_id]
        self.robots[robot_id] += 1
        if robot_id >= self.best_robot:
            # Each robot allow to build the next
            self.best_robot = min(robot_id+1, 3)

    def time_is_up(self) -> bool:
        return self.elasped_time >= 24

    def copy(self):
        return Factory(self.blueprint, self.robots.copy(), self.resources.copy(), self.elasped_time)

    def __str__(self) -> str:
        return f"Factory #{self.blueprint.id}: robots={self.robots} resources={self.resources} time={self.elasped_time}"


call_count = 0


def step(factory: Factory) -> int:
    global call_count
    call_count += 1

    if factory.time_is_up():
        # print(factory)
        return factory.resources[3]

    result = [0]

    for id in range(3, -1, -1):
        if factory.can_build(id):
            if factory.should_build(id):
                a = factory.copy()
                a.mine()
                a.build(id)
                result.append(step(a))

    if not factory.can_build(factory.best_robot):
        factory.mine()
        result.append(step(factory))
    return max(result)


qualities = []
for line in open("in.txt"):
    numbers = [int(k) for k in re.findall("[0-9]+", line)]
    blueprint = Blueprint(numbers[0],
                          np.array([numbers[1], 0, 0, 0]),
                          np.array([numbers[2], 0, 0, 0]),
                          np.array([numbers[3], numbers[4], 0, 0]),
                          np.array([numbers[5], 0, numbers[6], 0]))

    print(blueprint)
    robots = np.array([1, 0, 0, 0])
    resources = np.array([0, 0, 0, 0])
    elasped_time = 0
    factory = Factory(blueprint, robots, resources, elasped_time)

    geodes = step(factory)
    print("geodes", geodes)
    print("call_count", call_count)
    qualities.append(geodes * factory.blueprint.id)

print("total quality", sum(qualities))
