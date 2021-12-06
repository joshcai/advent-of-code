import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableListOf
import kotlin.collections.mutableSetOf
import kotlin.math.max
import kotlin.math.min

data class Points(val x1: Int, val y1: Int,
                  val x2: Int, val y2: Int)

fun readFile(file: String): List<Points> {
    val lines = File(file).readLines()
    val allPoints = arrayListOf<Points>()
    for (line in lines) {
        val (point1, point2) = line.split(" -> ")
        val (x1, y1) = point1.split(",").map(String::toInt)
        val (x2, y2) = point2.split(",").map(String::toInt)
        allPoints.add(Points(x1, y1, x2, y2))
    }
    return allPoints
}

fun order(a: Int, b: Int): Pair<Int, Int> {
    if (a < b) {
        return Pair(a, b)
    }
    return Pair(b, a)
}

fun part1(file: String): Int {
    val allPoints = readFile(file)
    val horizontalPoints = arrayListOf<Points>()
    val verticalPoints = arrayListOf<Points>()
    for (curr in allPoints) {
        if (curr.x1 == curr.x2) {
            verticalPoints.add(curr)
        }
        if (curr.y1 == curr.y2) {
            horizontalPoints.add(curr)
        }
    }
    val set = mutableSetOf<Pair<Int, Int>>()
    for (horizontal in horizontalPoints) {
        for (vertical in verticalPoints) {
            val x = vertical.x1
            val y = horizontal.y1
            var (x1, x2) = order(horizontal.x1, horizontal.x2)
            var (y1, y2) = order(vertical.y1, vertical.y2)
            if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                set.add(Pair(x, y))
            }
        }
    }
    for (i in horizontalPoints.indices) {
        for (j in horizontalPoints.indices) {
            if (j <= i) {
                continue
            }
            if (horizontalPoints[i].y1 != horizontalPoints[j].y1) {
                continue
            }
            var (i1, i2) = order(horizontalPoints[i].x1, horizontalPoints[i].x2)
            var (j1, j2) = order(horizontalPoints[j].x1, horizontalPoints[j].x2)
            var pointMin = max(i1, j1)
            var pointMax = min(i2, j2)
            for (k in pointMin..pointMax) {
                set.add(Pair(k, horizontalPoints[i].y1))
            }
        }
    }
    for (i in verticalPoints.indices) {
        for (j in verticalPoints.indices) {
            if (j <= i) {
                continue
            }
            if (verticalPoints[i].x1 != verticalPoints[j].x1) {
                continue
            }
            var (i1, i2) = order(verticalPoints[i].y1, verticalPoints[i].y2)
            var (j1, j2) = order(verticalPoints[j].y1, verticalPoints[j].y2)
            var pointMin = max(i1, j1)
            var pointMax = min(i2, j2)
            for (k in pointMin..pointMax) {
                set.add(Pair(verticalPoints[i].x1, k))
            }
        }
    }
    return set.size
}

fun part2(file: String): Int {
    val allPoints = readFile(file)
    val set = mutableSetOf<Pair<Int, Int>>()
    val multipleSet = mutableSetOf<Pair<Int, Int>>()
    for (curr in allPoints) {
        if (curr.x1 == curr.x2) {
            val (y1, y2) = order(curr.y1, curr.y2)
            for (i in y1..y2) {
                val pair = Pair(curr.x1, i)
                if (!set.add(pair)) {
                    multipleSet.add(pair)
                }
            }
            continue
        }
        if (curr.y1 == curr.y2) {
            val (x1, x2) = order(curr.x1, curr.x2)
            for (i in x1..x2) {
                val pair = Pair(i, curr.y1)
                if (!set.add(pair)) {
                    multipleSet.add(pair)
                }
            }
            continue
        }
        var xInc = 1
        var yInc = 1
        if (curr.x1 > curr.x2) {
            xInc = -1
        }
        if (curr.y1 > curr.y2) {
            yInc = -1
        }
        var xCurr = curr.x1
        var yCurr = curr.y1
        while (true) {
            val pair = Pair(xCurr, yCurr)
            if (!set.add(pair)) {
                multipleSet.add(pair)
            }
            if (xCurr == curr.x2 && yCurr == curr.y2) {
                break
            }
            xCurr += xInc
            yCurr += yInc
        }
    }
    
    return multipleSet.size
}

fun main() {
    check(part1("test") == 5)
    println(part1("input"))
    check(part2("test") == 12)
    println(part2("input"))
}