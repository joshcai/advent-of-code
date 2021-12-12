import java.io.File
import kotlin.collections.arrayListOf

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var counter = 0
    val scoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val closingMap = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    for (line in lines) {
        val chars = arrayListOf<Char>()
        for (char in line) {
            if (char == '[' || char == '(' || char == '{' || char == '<') {
                chars.add(char)
                continue
            }
            if (chars.last() != closingMap.getValue(char)) {
                counter += scoreMap.getValue(char)
                break
            } else {
                chars.removeLast()
            }
        }
    }
    return counter
}

fun part2(file: String): Long {
    val lines = readFile(file)
    val scoreMap = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
    val closingMap = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    val scores = arrayListOf<Long>()
    for (line in lines) {
        val chars = arrayListOf<Char>()
        var invalid = false
        for (char in line) {
            if (char == '[' || char == '(' || char == '{' || char == '<') {
                chars.add(char)
                continue
            }
            if (chars.last() != closingMap.getValue(char)) {
                invalid = true
                break
            } else {
                chars.removeLast()
            }
        }
        if (invalid) {
            continue
        }
        var score = 0.toLong()
        for (i in chars.indices.reversed()) {
            score *= 5
            score += scoreMap.getValue(chars[i])
        }
        scores.add(score)
    }
    scores.sort()
    return scores[scores.size / 2]
}

fun main() {
    check(part1("test") == 26397)
    println(part1("input"))
    check(part2("test") == 288957.toLong())
    println(part2("input"))
}