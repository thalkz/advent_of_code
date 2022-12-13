class Monkey:
    def __init__(self, items, op, testDivBy, ifTrue, ifFalse):
        self.items = items
        self.op = op
        self.testDivBy = testDivBy
        self.ifTrue = ifTrue
        self.ifFalse = ifFalse
        self.count = 0

    def inspect(self, x):
        self.count += 1
        a = 0
        b = 0
        if self.op[0] == "old":
            a = x
        else:
            a = int(self.op[0])
        if self.op[2] == "old":
            b = x
        else:
            b = int(self.op[2])
        if self.op[1] == "+":
            print(self.op)
            print(a, "+", b)
            return int((a + b) / 3)
        elif self.op[1] == "*":
            print(self.op)
            print(a, "*", b)
            return int((a * b) / 3)
        else:
            print("woops, unrecognized operation")

    def test(self, x):
        if x % self.testDivBy == 0:
            return self.ifTrue
        else:
            return self.ifFalse


lines = open("in.txt").read().split("\n")
monkeys = []

for i in range(0, len(lines), 7):
    items = [int(x) for x in lines[i+1].split(": ")[1].split(", ")]
    # print(items)
    op = lines[i+2].split(" = ")[1].split(" ")
    # print(operation)
    testDivBy = int(lines[i+3].split("by ")[1])
    # print(testDivBy)
    ifTrue = int(lines[i+4].split("monkey ")[1])
    # print(ifTrue)
    ifFalse = int(lines[i+5].split("monkey ")[1])
    # print(ifFalse)

    m = Monkey(items, op, testDivBy, ifTrue, ifFalse)
    monkeys.append(m)

# for m in monkeys:
#     print(m.items)
#     print(m.op)
#     print(m.testDivBy)
#     print(m.ifTrue)
#     print(m.ifFalse)
#     print()

for i in range(20):
    for m in monkeys:
        for _ in range(len(m.items)):
            x = m.items.pop(0)
            print(f"item with worry {x} is inspected")
            x = m.inspect(x)
            print(f"item is now {x}")
            target = m.test(x)
            monkeys[target].items.append(x)
            print(f"item with worry {x} is thrown to monkey {target}")

print()

for m in monkeys:
    print(m.items)

s = sorted([m.count for m in monkeys], reverse=True)
print(s)
print(s[0] * s[1])