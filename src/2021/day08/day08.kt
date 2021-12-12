import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableSetOf

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var counter = 0
    for (line in lines) {
        val (_, outputDigits) = line.split("|")
        for (word in outputDigits.split(" ")) {
            val size = word.length
            if (size == 2 || size == 4 || size == 3 || size == 7) {
                counter += 1
            }
        }
    }
    return counter
}

fun sortStr(s: String): String {
    val arr = s.toCharArray()
    return arr.sorted().joinToString("")
}

// Check if a contains all letters from b.
fun contains(a: String, b: String): Boolean {
    val set = mutableSetOf<Char>()
    for (c in a) {
        set.add(c)
    }
    for (c in b) {
        if (!set.contains(c)) {
            return false
        }
    }
    return true
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var counter = 0
    for (line in lines) {
        val (ruleDigits, outputDigits) = line.split("|")
        val mapping = mutableMapOf<Int, ArrayList<String>>().withDefault { arrayListOf<String>() }
        for (word in ruleDigits.split(" ")) {
            if (word.length == 0) {
                continue
            }
            val l = mapping.getValue(word.length)
            l.add(sortStr(word))
            mapping[word.length] = l
        }
        val intMapping = mutableMapOf<Int, String>()
        intMapping[1] = mapping.getValue(2)[0]
        intMapping[4] = mapping.getValue(4)[0]
        intMapping[7] = mapping.getValue(3)[0]
        intMapping[8] = mapping.getValue(7)[0]
        for (str in mapping.getValue(6)) {
            if (contains(str, intMapping.getValue(4))) {
                intMapping[9] = str
            } else if (contains(str, intMapping.getValue(1))) {
                intMapping[0] = str
            }
            else {
                intMapping[6] = str
            }
        }
        for (str in mapping.getValue(5)) {
            if (contains(str, intMapping.getValue(1))) {
                intMapping[3] = str
            } else if (contains(intMapping.getValue(6), str)) {
                intMapping[5] = str
            } else {
                intMapping[2] = str
            }
        }
        val strMapping = mutableMapOf<String, Int>()
        for ((key, value) in intMapping) {
            strMapping[value] = key
        }

        var multiplier = 1000
        for (word in outputDigits.split(" ")) {
            if (word.length == 0) {
                continue
            }
            counter += multiplier * strMapping.getValue(sortStr(word))
            multiplier /= 10
        } 
       
    }
    return counter
}

fun main() {
    check(part1("test") == 26)
    println(part1("input"))
    check(part2("test") == 61229)
    println(part2("input"))
}