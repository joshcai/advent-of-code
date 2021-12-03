import java.io.File

fun readFile(file: String): List<String> {
    return File(file)
            .readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var depth = 0
    var horizontal = 0
    for (line in lines) {
        val (command, numStr) = line.split(" ")
        val num = numStr.toInt()
        when (command) {
            "forward" -> horizontal += num
            "down" -> depth += num
            "up" -> depth -= num
        }
    }
    return depth * horizontal
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var depth = 0
    var horizontal = 0
    var aim = 0
    for (line in lines) {
        val (command, numStr) = line.split(" ")
        val num = numStr.toInt()
        when (command) {
            "forward" -> {
                horizontal += num
                depth += num * aim
            }
            "down" -> aim += num
            "up" -> aim -= num
        }
    }
    return depth * horizontal
}

fun main() {
    check(part1("test") == 150)
    println(part1("input"))
    check(part2("test") == 900)
    println(part2("input"))
}