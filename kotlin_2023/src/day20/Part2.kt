package day20

import java.util.*

fun part2(lines: List<String>): Long {
    val queue: Queue<Pulse> = LinkedList()
    val modules = createModules(lines, queue)

    val initialPulse = Pulse(
        from = modules["button"]!!,
        to = modules["broadcaster"]!!,
        type = PulseType.Low
    )

    var count = 0
    var preModules = mutableMapOf<String, Int>(
        "xm" to 0,
        "pv" to 0,
        "qh" to 0,
        "hz" to 0,
    )

    while (true) {
        count++
        queue.offer(initialPulse)
        while (queue.isNotEmpty()) {
            val pulse = queue.remove()
            //  println("${pulse.from.name} -${pulse.type.name}-> ${pulse.to.name}")

            if (pulse.type == PulseType.High && preModules.containsKey(pulse.from.name)) {
                if (preModules[pulse.from.name] == 0) {
                    preModules[pulse.from.name] = count
                }

                if (preModules.all { it.value > 0 }) {
                    return preModules.values.fold(1L) { acc, value -> acc * value.toLong() }
                }
            }

            pulse.to.receive(pulse.from.name, pulse.type)
        }
    }
}
