package day20

data class Pulse(
    val from: Module,
    val to: Module,
    val type: PulseType,
)

enum class PulseType {
    Low,
    High,
}