import java.io.File
import kotlin.math.min
import kotlin.math.max

data class Instruction(val type: String,  
  val xStart: Int, val xEnd: Int, 
  val yStart: Int, val yEnd: Int,
  val zStart: Int, val zEnd: Int
)

fun readFile(file: String): List<Instruction> {
    val lines = File(file).readLines()
    val temp = arrayListOf<Instruction>()
    for (line in lines) {
        val (ins, coords) = line.split(" ")
        val (x, y, z) = coords.split(",")
        val (xStart, xEnd) = x.substring(2, x.length).split(".").filter { it != "" }.map(String::toInt)
        val (yStart, yEnd) = y.substring(2, y.length).split(".").filter { it != "" }.map(String::toInt)
        val (zStart, zEnd) = z.substring(2, z.length).split(".").filter { it != "" }.map(String::toInt)
        temp.add(Instruction(ins, xStart, xEnd, yStart, yEnd, zStart, zEnd))
    }
    return temp
}

fun part1(file: String): Int {
    val instructions = readFile(file)
    val map = mutableMapOf<Triple<Int, Int, Int>, Boolean>()
    for (ins in instructions) {
        if (ins.xEnd < -50 || ins.xEnd > 50 || ins.yEnd < -50 || ins.yStart > 50 
        || ins.zEnd < -50 || ins.zEnd > 50 ) {
            continue
        }
        for (x in ins.xStart..ins.xEnd) {
            for (y in ins.yStart..ins.yEnd) {
                for (z in ins.zStart..ins.zEnd) {
                    var value = ins.type == "on"
                    map.put(Triple(x, y, z), value)
                }
            }
        }
    }
    var counter = 0
    for ((_, value) in map) {
        if (value) {
            counter += 1
        }
    }
    return counter
}

data class Box(
  val xStart: Int, val xEnd: Int, 
  val yStart: Int, val yEnd: Int,
  val zStart: Int, val zEnd: Int,
  val sign: Int
)

// Based on: https://www.reddit.com/r/adventofcode/comments/rlxhmg/2021_day_22_solutions/hpizza8/
fun part2(file: String): Long {
    val instructions = readFile(file)
    val boxes = arrayListOf<Box>()
    for (ins in instructions) {
        val nextBoxes = arrayListOf<Box>()
        for (box in boxes) {
            val xStart = max(box.xStart, ins.xStart)
            val xEnd = min(box.xEnd, ins.xEnd)
            if (xStart > xEnd) {
                continue
            }
            val yStart = max(box.yStart, ins.yStart)
            val yEnd = min(box.yEnd, ins.yEnd)
            if (yStart > yEnd) {
                continue
            }
            val zStart = max(box.zStart, ins.zStart)
            val zEnd = min(box.zEnd, ins.zEnd)
            if (zStart > zEnd) {
                continue
            }
            nextBoxes.add(Box(xStart, xEnd, yStart, yEnd, zStart, zEnd, box.sign * -1))
        }
        boxes.addAll(nextBoxes)
        if (ins.type == "on") {
          boxes.add(Box(ins.xStart, ins.xEnd, ins.yStart, ins.yEnd, ins.zStart, ins.zEnd, 1))
        }
    }

    var counter = 0.toLong()
    for (box in boxes) {
        val x = (box.xEnd - box.xStart + 1).toLong()
        val y = (box.yEnd - box.yStart + 1).toLong()
        val z = (box.zEnd - box.zStart + 1).toLong()
        counter += x * y * z * box.sign.toLong()
    }
    return counter
}

fun main() {
    check(part1("test") == 590784)
    println(part1("input"))
    check(part2("test2") == 2758514936282235)
    println(part2("input"))
}