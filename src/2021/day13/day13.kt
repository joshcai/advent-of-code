import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableSetOf

typealias Point = Pair<Int, Int>
typealias Instruction = Pair<String, Int>

fun readFile(file: String): Pair<Set<Point>, List<Instruction>> {
    val lines = File(file).readLines()
    var numbers = true
    var instructions = arrayListOf<Instruction>()
    val set = mutableSetOf<Point>()
    for (line in lines) {
        if (line == "") {
            numbers = false
            continue
        }
        if (numbers) {
            val (x, y) = line.split(",").map(String::toInt)
            set.add(Pair(x, y))
        } else {
            val (axis, numStr) = line.split(" ").last().split("=")
            val num = numStr.toInt()
            instructions.add(Pair(axis, num))
        }
    }
    return Pair(set, instructions)
}

fun step(set: Set<Point>, ins: Instruction): Set<Point> {
    val newSet = mutableSetOf<Point>()
    val (axis, num) = ins
    for (point in set) {
        var (x, y) = point
        if (axis == "x") {
            if (x > num) {
                x = num - (x - num)
            }
        } else {
            if (y > num) {
                y = num - (y - num)
            }
        }
        newSet.add(Pair(x, y))
    }
    return newSet
}

fun part1(file: String): Int {
    val (set, instructions) = readFile(file)
    val newSet = step(set, instructions[0])
    return newSet.size
}
    
fun part2(file: String) {
    var (set, instructions) = readFile(file)
    for (ins in instructions) {
        set = step(set, ins).toMutableSet()
    }
    var maxX = -1
    var maxY = -1
    for (point in set) {
        var (x, y) = point
        if (x > maxX) {
            maxX = x
        }
        if (y > maxY) {
            maxY = y
        }
    }
    for (j in 0..maxY) {
        for (i in 0..maxX) {
            if (set.contains(Pair(i, j))) {
                print("x")
            } else {
                print(" ")
            }
        }
        println("")
    }
}

fun main() {
    check(part1("test") == 17)
    println(part1("input"))
    part2("test")
    part2("input")
}