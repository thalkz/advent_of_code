def print_nodes(nodes):
    n = nodes[0]
    for _ in range(len(nodes)):
        print(n.value, end=", ")
        n = n.next
    print()


def length(nodes):
    start = nodes[0]
    end = start.next
    i = 1
    while end != start:
        end = end.next
        i+=1
    return i

def check(nodes):
    n = nodes[0]
    for _ in range(len(nodes)):
        if n.previous.next.value != n.value:
            print("previous next is not the same")
        elif n.next.previous.value != n.value:
            print("next previous is not the same")
        n = n.next

class Node:
    def __init__(self, value) -> None:
        self.value = value

nodes = []
for line in open("in.txt"):
    n = Node(int(line) * 811589153)
    nodes.append(n)

for i in range(len(nodes)):
    next_i = (i+1) % len(nodes)
    prev_i = (i-1) % len(nodes)
    nodes[i].next = nodes[next_i]
    nodes[i].previous = nodes[prev_i]


print(print_nodes(nodes))
print(length(nodes))

for k in range(10):
    for i in range(len(nodes)):
        value = nodes[i].value
        if value % (len(nodes)-1) == 0:
            continue
        nodes[i].previous.next = nodes[i].next
        nodes[i].next.previous = nodes[i].previous
        before = nodes[i]
        after = nodes[i]
        if value > 0:
            for _ in range(value % (len(nodes)-1)):
                before = before.next
            after = before.next
        else:
            for _ in range((-value) % (len(nodes)-1)):
                after = after.previous
            before = after.previous
        before.next = nodes[i]
        nodes[i].previous = before
        after.previous = nodes[i]
        nodes[i].next = after
    
    print("after round", k+1)
    print_nodes(nodes)

# find 0
zero = nodes[0]
while zero.value != 0:
    zero = zero.next

x = zero
for _ in range(1000):
    x = x.next

y = zero
for _ in range(2000):
    y = y.next

z = zero
for _ in range(3000):
    z = z.next

print("x", x.value)
print("y", y.value)
print("z", z.value)
print("result", x.value+y.value+z.value)
