import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableMapOf

fun readFile(file: String): List<Int> {
    return File(file).readLines()[0].split(",").map(String::toInt)
}

fun part(file: String, days: Int): Long {
    val fish = readFile(file)
    val table = arrayListOf<Long>()
    var curr = mutableMapOf<Int, Long>()
    curr[1] = 1
    for (i in 1..days+1) {
        val size = curr.map { it.value }.sum()
        table.add(size.toLong())
        val next = mutableMapOf<Int, Long>()
        for ((num, count) in curr.entries) {
            if (num == 0) {
                next[8] = next.getOrDefault(8, 0) + count
                next[6] = next.getOrDefault(6, 0) + count
                continue
            }
            next[num-1] = next.getOrDefault(num-1, 0) + count
        }
        curr = next
    }
    var sum: Long = 0
    for (f in fish) {
        sum += table[days-f+1]
    }
    return sum   
}

fun part1(file: String): Long {
    return part(file, 80)
}

fun part2(file: String): Long {
    return part(file, 256)
}

fun main() {
    check(part1("test") == 5934.toLong())
    println(part1("input"))
    check(part2("test") == 26984457539)
    println(part2("input"))
}