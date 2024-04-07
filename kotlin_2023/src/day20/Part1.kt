package day20

import java.util.*

fun part1(lines: List<String>): Int {
    val queue: Queue<Pulse> = LinkedList()
    val modules = createModules(lines, queue)

    val broadcasterModule = modules["broadcaster"]!!
    val initialPulse = Pulse(
        from = modules["button"]!!,
        to = broadcasterModule,
        type = PulseType.Low
    )

    var lowCount = 0
    var highCount = 0

    repeat(1000) {
        queue.offer(initialPulse)
        while (queue.isNotEmpty()) {
            val pulse = queue.remove()
            when (pulse.type) {
                PulseType.Low -> lowCount++
                PulseType.High -> highCount++
            }
            pulse.to.receive(pulse.from.name, pulse.type)
        }
    }

    return lowCount * highCount
}