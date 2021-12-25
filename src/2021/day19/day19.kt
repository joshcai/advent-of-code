import java.io.File
import kotlin.math.abs

data class Point(val x: Int, val y: Int, val z: Int)
typealias Scanner = List<Point>
data class ScannerInfo(val scanner: Scanner, val index: Int)

fun readFile(file: String): List<ScannerInfo> {
    val lines = File(file).readLines().toMutableList()
    lines.add("")
    val scanners = arrayListOf<ScannerInfo>()
    var currentScanner = arrayListOf<Point>()
    var index = 0
    for (line in lines) {
        if (line.startsWith("---")) {
            continue
        }
        if (line == "") {
            scanners.add(ScannerInfo(currentScanner, index))
            currentScanner = arrayListOf<Point>()
            index += 1
            continue
        }
        val (x, y, z) = line.split(",").map(String::toInt)
        currentScanner.add(Point(x, y, z))
    }
    return scanners
}

fun rotate(a: Scanner, rot: Triple<Int, Int, Int>, neg: Triple<Int, Int, Int>): Set<Point> {
    val negged = a.map { Point(it.x * neg.first, it.y * neg.second, it.z * neg.third)}
    return negged.map {
        val list = arrayOf(it.x, it.y, it.z)
        Point(list[rot.first], list[rot.second], list[rot.third])
    }.toSet()
}

// Whether they match, if they do, return b re-oriented - shifted to fit.
// Triple<can match, list of points re-oriented, point of the scanner>
fun canMatch(a: Scanner, b: Scanner): Triple<Boolean, Scanner, Point> {
    val aSet = a.toSet()
    val rotations = arrayListOf<Triple<Int, Int, Int>>(
        Triple(0, 1, 2),
        Triple(0, 2, 1),
        Triple(1, 0, 2),
        Triple(1, 2, 0),
        Triple(2, 0, 1),
        Triple(2, 1, 0),
    )
    val negs = arrayListOf<Triple<Int, Int, Int>>(
        Triple(1, 1, 1),
        Triple(1, 1, -1),
        Triple(1, -1, 1),
        Triple(1, -1, -1),
        Triple(-1, 1, 1),
        Triple(-1, 1, -1),
        Triple(-1, -1, 1),
        Triple(-1, -1, -1),
    )
    for (rot in rotations) {
        for (neg in negs) {
            val bSet = rotate(b, rot, neg)
            for (aPoint in a) {
                val diffs = bSet.map { Point(aPoint.x - it.x, aPoint.y - it.y, aPoint.z - it.z) }
                for (diff in diffs) {
                    val shifted = bSet.map{ Point(it.x + diff.x, it.y + diff.y, it.z + diff.z) }
                    if (shifted.intersect(aSet).size >= 12) {
                        return Triple(true, shifted.toList(), diff)
                    }
                }
            }
        }
    }
    return Triple(false, b, Point(0, 0, 0))
}

// Returns a pair of <all points, scanner points>
fun part(file: String): Pair<Set<Point>, Set<Point>> {
    val scanners = readFile(file).toMutableList()
    val cleared = arrayListOf<ScannerInfo>(scanners[0])
    scanners.removeFirst()
    val scannerSet = mutableSetOf<Point>(Point(0, 0, 0))
    // Cache of what pairs have been checked already
    val checked = mutableSetOf<Pair<Int, Int>>()
    while (scanners.size > 0) {
        var index = -1
        for (i in scanners.indices) {
            for (clearedScanner in cleared) {
                if (checked.contains(Pair(clearedScanner.index, scanners[i].index))) {
                    continue
                }
                val (matches, bScanner, diff) = canMatch(clearedScanner.scanner, scanners[i].scanner)
                checked.add(Pair(clearedScanner.index, scanners[i].index))
                if (matches) {
                    index = i
                    scannerSet.add(diff)
                    cleared.add(ScannerInfo(bScanner, scanners[i].index))
                    break
                }
            }
            if (index != -1) {
                break
            }
        }
        if (index != -1) {
            scanners.removeAt(index)
        }
    }
    val pointsSet = mutableSetOf<Point>()
    for (scannerInfo in cleared) {
        for (point in scannerInfo.scanner) {
            pointsSet.add(point)
        }
    }
    return Pair(pointsSet, scannerSet)
}
fun part1(file: String): Int {
    val (allPoints, _) = part(file)
    return allPoints.size
}

fun manhattanDistance(p1: Point, p2: Point): Int {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y) + abs(p1.z - p2.z)
}

fun part2(file: String): Int {
    val (_, scannerPoints) = part(file)
    var maxValue = 0
    for (p1 in scannerPoints) {
        for (p2 in scannerPoints) {
            val curr = manhattanDistance(p1, p2)
            if (curr > maxValue) {
                maxValue = curr
            }
        }
    }
    return maxValue
}

fun main() {
    check(part1("test") == 79)
    println(part1("input"))
    check(part2("test") == 3621)
    println(part2("input"))
}