package day20

import java.util.*

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