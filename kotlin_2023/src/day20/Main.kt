package day20

import readInput
import verify
import java.util.*

const val day = 20

fun main() {
    println("Day $day")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 32000000,
    )

    val mediumInput = readInput(day, "medium")
    verify(
        name = "Part1_medium",
        actual = part1(mediumInput),
        expected = 11687500,
    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 731517480,
    )
    verify(
        name = "Part2",
        actual = part2(input),
        expected = 0,
    )
}


data class Pulse(
    val from: Module,
    val to: Module,
    val type: PulseType,
)

enum class PulseType {
    Low,
    High,
}

abstract class Module(
    val name: String,
    private val eventSink: Queue<Pulse>
) {
    private val destinations: MutableList<Module> = mutableListOf()

    fun addDestination(to: Module) {
        destinations.add(to)
        to.registerInputModule(this)
    }

    open fun registerInputModule(from: Module) {}

    abstract fun receive(from: String, pulseType: PulseType)

    fun send(pulseType: PulseType) {
        destinations.forEach {
            eventSink.offer(Pulse(from = this, to = it, type = pulseType))
        }
    }
}

class BroadcastModule(name: String, eventSink: Queue<Pulse>) : Module(name, eventSink) {
    override fun receive(from: String, pulseType: PulseType) {
        send(pulseType)
    }

    override fun toString(): String {
        return name
    }
}

class FlipFlopModule(name: String, eventSink: Queue<Pulse>) : Module(name, eventSink) {
    private var isOn: Boolean = false

    override fun receive(from: String, pulseType: PulseType) {
        when (pulseType) {
            PulseType.Low -> {
                if (isOn) {
                    send(PulseType.Low)
                } else {
                    send(PulseType.High)
                }
                isOn = !isOn
            }

            PulseType.High -> {}
        }
    }

    override fun toString(): String {
        return "name $isOn"
    }
}

class ConjunctionModule(name: String, eventSink: Queue<Pulse>) : Module(name, eventSink) {
    private val lastReceivedPulses: MutableMap<String, PulseType> = mutableMapOf()
    private var allHigh = false

    override fun registerInputModule(from: Module) {
        lastReceivedPulses[from.name] = PulseType.Low
    }

    override fun receive(from: String, pulseType: PulseType) {
        lastReceivedPulses[from] = pulseType

        if (allHigh && pulseType == PulseType.Low) {
            allHigh = false
        } else if (!allHigh && pulseType == PulseType.High) {
            allHigh = lastReceivedPulses.none { it.value == PulseType.Low }
        }

        if (allHigh) {
            send(PulseType.Low)
        } else {
            send(PulseType.High)
        }
    }

    override fun toString(): String {
        return "name ${lastReceivedPulses.entries.joinToString { "${it.key}-${it.value.name}" }}"
    }
}

fun createModules(lines: List<String>, queue: Queue<Pulse>): MutableMap<String, Module> {
    val modules: MutableMap<String, Module> = mutableMapOf()

    for (line in lines) {
        val identifier = line.split(" -> ").first()
        when {
            identifier.startsWith("%") -> {
                val name = identifier.toName()
                modules[name] = FlipFlopModule(name, queue)
            }

            identifier.startsWith("&") -> {
                val name = identifier.toName()
                modules[name] = ConjunctionModule(name, queue)
            }

            else -> {
                modules[identifier] = BroadcastModule(identifier, queue)
            }
        }
    }

    for (line in lines) {
        val name = line.split(" -> ")
            .first()
            .toName()

        val destinationNames = line.split(" -> ")
            .last()
            .split(", ")

        destinationNames.forEach {
            if (modules[it] == null) {
                modules[it] = BroadcastModule(it, queue)
            }

            modules[name]!!.addDestination(modules[it]!!)
        }
    }

    val buttonModule = BroadcastModule("button", queue)
    buttonModule.addDestination(modules["broadcaster"]!!)
    modules["button"] = buttonModule

    return modules
}

fun String.toName(): String {
    return if (startsWith("%") || startsWith("&")) {
        drop(1)
    } else {
        this
    }
}

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
            //  println("${pulse.from.name} -${pulse.type.name}-> ${pulse.to.name}")
            when (pulse.type) {
                PulseType.Low -> lowCount++
                PulseType.High -> highCount++
            }
            pulse.to.receive(pulse.from.name, pulse.type)
        }
    }

    return lowCount * highCount
}

fun part2(lines: List<String>): Int {
    val queue: Queue<Pulse> = LinkedList()
    val modules = createModules(lines, queue)

    val initialPulse = Pulse(
        from = modules["button"]!!,
        to = modules["broadcaster"]!!,
        type = PulseType.Low
    )

    var count = 0

    while (true) {
        count++
        queue.offer(initialPulse)
        while (queue.isNotEmpty()) {
            val pulse = queue.remove()
            //  println("${pulse.from.name} -${pulse.type.name}-> ${pulse.to.name}")

            if (pulse.to.name == "rx" && pulse.type == PulseType.Low) {
                return count
            }

            pulse.to.receive(pulse.from.name, pulse.type)
        }
    }
}
