import java.io.File

fun readFile(file: String): Pair<String, Map<String, String>> {
    val lines = File(file).readLines()
    val start = lines[0]
    val map = mutableMapOf<String, String>()
    for (i in lines.indices) {
        if (i < 2) {
            continue
        }
        map[lines[i].substring(0, 2)] = lines[i].last().toString()
    }
    return Pair(start, map)
}

fun step(m: Map<String, Long>, template: Map<String, String>): Map<String, Long> {
    val next = mutableMapOf<String, Long>()
    for ((key, value) in m) {
        val char = template.getValue(key)
        next[key[0] + char] = next.getOrDefault(key[0] + char, 0) + value
        next[char + key[1]] = next.getOrDefault(char + key[1], 0) + value
    }
    return next
}

fun part(file: String, steps: Int): Long {
    val (start, template) = readFile(file)
    var currMap = mutableMapOf<String, Long>()
    for (i in 0..start.length-2) {
        val key = start.substring(i, i+2)
        currMap[key] = currMap.getOrDefault(key, 0) + 1
    }
    for (i in 1..steps) {
        currMap = step(currMap, template).toMutableMap()
    }
    var count = mutableMapOf<String, Long>()
    for ((key, value) in currMap) {
        count[key[0].toString()] = count.getOrDefault(key[0].toString(), 0) + value
        count[key[1].toString()] = count.getOrDefault(key[1].toString(), 0) + value
    }
    // Add 1 to the ends since they are the only ones not double counted.
    count[start[0].toString()] = count.getOrDefault(start[0].toString(), 0) + 1
    count[start[start.length-1].toString()] = count.getOrDefault(start[start.length-1].toString(), 0) + 1
    var min = Long.MAX_VALUE
    var max = -1.toLong()
    for ((_, value) in count) {
        if (value < min) {
            min = value
        }
        if (value > max) {
            max = value
        }
    }
    // Divide by two since every character should be double counted after adjustments.
    return (max - min) / 2
}

fun part1(file: String): Int {
    return part(file, 10).toInt()
}

fun part2(file: String): Long {
    return part(file, 40)
}

fun main() {
    check(part1("test") == 1588)
    println(part1("input"))
    check(part2("test") == 2188189693529)
    println(part2("input"))
}