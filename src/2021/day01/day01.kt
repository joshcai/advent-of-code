import java.io.File

fun readFile(file: String): List<Int> {
    return File(file)
            .readLines()
            .map(String::toInt)
}

fun part1(file: String): Int {
    val numbers = readFile(file)
    var curr = numbers[0]
    var counter = 0
    for (num in numbers) {
        if (num > curr) {
            counter += 1
        }
        curr = num
    }
    return counter
}

fun part2(file: String): Int {
    val numbers = readFile(file)
    var curr = numbers[0] + numbers[1] + numbers[2]
    var counter = 0
    for (i in numbers.indices) {
        if (i < 3) {
            continue
        }
        val next = curr - numbers[i-3] + numbers[i]
        if (next > curr) {
            counter += 1
        }
        curr = next
    }
    return counter
}

fun main() {
    check(part1("test") == 7)
    println(part1("input"))
    check(part2("test") == 5)
    println(part2("input"))
}