ope_monkeys = {}
val_monkeys = {}
for line in open("in.txt"):
    parts = line.split(": ")
    if parts[0] == "humn":
        continue
    try:
        value = int(parts[1])
        val_monkeys[parts[0]] = value
    except ValueError:
        operation = parts[1][:-1].split(" ")
        ope_monkeys[parts[0]] = operation


print("values:", val_monkeys)
print("operations:", ope_monkeys)

while "root" not in val_monkeys:
    made_changes = False
    to_delete = set()
    for k in ope_monkeys:
        if k == "root":
            continue
        v = ope_monkeys[k]
        if v[0] in val_monkeys and v[2] in val_monkeys:
            made_changes = True
            print("perform", v)
            to_delete.add(k)
            if v[1] == "+":
                val_monkeys[k] = val_monkeys[v[0]] + val_monkeys[v[2]] 
            elif v[1] == "*":
                val_monkeys[k] = val_monkeys[v[0]] * val_monkeys[v[2]] 
            elif v[1] == "/":
                val_monkeys[k] = int(val_monkeys[v[0]] / val_monkeys[v[2]])
            elif v[1] == "-":
                val_monkeys[k] = val_monkeys[v[0]] - val_monkeys[v[2]] 
    for k in to_delete:
        del ope_monkeys[k]
    if not made_changes:
        break

# print(val_monkeys)
# print(ope_monkeys)

equal_monkey = ""
if ope_monkeys["root"][0] in val_monkeys:
    equal_monkey = ope_monkeys["root"][2]
    equal_value = ope_monkeys["root"][0]
else:
    equal_monkey = ope_monkeys["root"][0]
    equal_value = ope_monkeys["root"][2]

val_monkeys[equal_monkey] = val_monkeys[equal_value]

del ope_monkeys["root"]

print("values:", val_monkeys)
print("operations:", ope_monkeys)

reversed_op = {}
for k in ope_monkeys:
    v = ope_monkeys[k]
    if v[1] == "+":
        reversed_op[v[0]] = [k, "-", v[2]]
        reversed_op[v[2]] = [k, "-", v[0]]
    elif v[1] == "-":
        reversed_op[v[0]] = [k, "+", v[2]]
        reversed_op[v[2]] = [v[0], "-", k]
    if v[1] == "*":
        reversed_op[v[0]] = [k, "/", v[2]]
        reversed_op[v[2]] = [k, "/", v[0]]
    if v[1] == "/":
        reversed_op[v[0]] = [k, "*", v[2]]
        reversed_op[v[2]] = [v[0], "/", k]

print("reversed:", reversed_op)

while "humn" not in val_monkeys:
    to_delete = set()
    for k in reversed_op:
        v = reversed_op[k]
        if v[0] in val_monkeys and v[2] in val_monkeys:
            print("perform", v)
            to_delete.add(k)
            if v[1] == "+":
                val_monkeys[k] = val_monkeys[v[0]] + val_monkeys[v[2]] 
            elif v[1] == "*":
                val_monkeys[k] = val_monkeys[v[0]] * val_monkeys[v[2]] 
            elif v[1] == "/":
                val_monkeys[k] = int(val_monkeys[v[0]] / val_monkeys[v[2]])
            elif v[1] == "-":
                val_monkeys[k] = val_monkeys[v[0]] - val_monkeys[v[2]] 
    for k in to_delete:
        del reversed_op[k]

print(val_monkeys["humn"])
