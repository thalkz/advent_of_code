ope_monkeys = {}
val_monkeys = {}
for line in open("small.txt"):
    parts = line.split(": ")
    try:
        value = int(parts[1])
        val_monkeys[parts[0]] = value
    except ValueError:
        operation = parts[1][:-1].split(" ")
        ope_monkeys[parts[0]] = operation

print(val_monkeys)
print(ope_monkeys)

while "root" not in val_monkeys:
    to_delete = set()
    for k in ope_monkeys:
        v = ope_monkeys[k]
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
        del ope_monkeys[k]

print(val_monkeys["root"])
