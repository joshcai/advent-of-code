import java.io.File
import java.util.Arrays
import kotlin.collections.arrayListOf
import kotlin.math.pow


fun readFile(file: String): List<String> {
    return File(file)
            .readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    val size = lines[0].length
    val zeros = Array(size) { 0 }
    val ones = Array(size) { 0 }
    for (line in lines) {
        for (i in 0..line.length-1) {
            if (line[i] == '0') {
                zeros[i] += 1
            } else {
                ones[i] += 1
            }
        }
    }
    var gamma = 0
    var epsilon = 0
    val two = 2.0
    for (i in zeros.indices) {
        if (ones[i] > zeros[i]) {
            gamma += two.pow(size - i - 1).toInt()
        } else {
            epsilon += two.pow(size - i - 1).toInt()
        }
    }
    return gamma * epsilon
}

fun filter(current: List<String>, index: Int, most: Boolean): List<String> {
    var zeros = arrayListOf<String>()
    var ones = arrayListOf<String>()
    for (curr in current) {
        if (curr[index] == '0') {
            zeros.add(curr)
        } else {
            ones.add(curr)
        }
    }
    if (most) {
        if (ones.size >= zeros.size) {
            return ones
        }
        return zeros
    }
    if (zeros.size <= ones.size) {
        return zeros
    }
    return ones
}

// Convert string of bits to integer
fun convert(s: String): Int {
    val size = s.length
    var num = 0
    val two = 2.0
    for (i in s.indices) {
        if (s[i] == '1') {
            num += two.pow(size - i - 1).toInt()
        }
    }
    return num
}

fun part2(file: String): Int {
    val lines = readFile(file)

    var current = lines
    var index = 0
    while (current.size > 1) {
        current = filter(current, index, true)
        index += 1
    }
    val oxygen = current[0]

    index = 0
    current = lines
    while (current.size > 1) {
        current = filter(current, index, false)
        index += 1
    }
    val co2 = current[0]
    return convert(oxygen) * convert(co2)
}

fun main() {
    check(part1("test") == 198)
    println(part1("input"))
    check(part2("test") == 230)
    println(part2("input"))
}