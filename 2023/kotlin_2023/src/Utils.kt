import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(day: Int, filename: String) = Path("src/day$day/$filename.txt").readLines()

fun verify(name: String, actual: Any, expected: Any) {
    val correct = actual.toString() == expected.toString()
    if (correct) {
        println("$name $actual CORRECT ✅")
    } else {
        println("$name expected $expected, got $actual ❌")
    }
}
