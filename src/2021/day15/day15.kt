import java.io.File
import java.util.PriorityQueue

typealias Point = Pair<Int, Int>

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun shortestDist(grid: List<List<Int>>): Int {
    var dist = mutableMapOf<Point, Int>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            dist[Pair(i, j)] = Int.MAX_VALUE
        }
    }
    var visited = mutableSetOf<Point>()
    var curr = Pair(0, 0)
    dist[curr] = 0
    val target = Pair(grid.size - 1, grid[0].size - 1)

    val compareByDist: Comparator<Point> = compareBy { dist.getValue(it) }
    val pq = PriorityQueue<Point>(compareByDist)
    fun visit(i: Int, j: Int, distSoFar: Int) {
        if (visited.contains(Pair(i, j))) {
            return
        }
        if (i < 0 || j < 0 || i >= grid.size || j >= grid[0].size) {
            return
        }
        if (distSoFar + grid[i][j] < dist.getValue(Pair(i, j))) {
            dist[Pair(i, j)] = distSoFar + grid[i][j]
        }
        pq.add(Pair(i, j))
    }

    while (true) {
        val (x, y) = curr
        val distSoFar = dist.getValue(curr)
        visit(x-1, y, distSoFar)
        visit(x+1, y, distSoFar)
        visit(x, y-1, distSoFar)
        visit(x, y+1, distSoFar)
        visited.add(curr)
        while (visited.contains(curr)) {
            curr = pq.remove()
        }
        if (curr == target) {
            return dist.getValue(target)
        }
    }
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var grid = arrayListOf<List<Int>>()
    for (line in lines) {
        grid.add(line.split("").filter { it != "" }.map(String::toInt))
    }
    var dist = mutableMapOf<Point, Int>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            dist[Pair(i, j)] = Int.MAX_VALUE
        }
    }
    return shortestDist(grid)
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var grid = arrayListOf<List<Int>>()
    for (line in lines) {
        val l = arrayListOf<List<Int>>()
        l.add(line.split("").filter { it != "" }.map(String::toInt))
        for (i in 1..4) {
            l.add(l.last().map { it % 9 + 1 } )
        }
        grid.add(l.flatten())
    }
    val numRows = grid.size
    for (i in 1..4) {
        for (j in 1..numRows) {
            grid.add(grid[grid.size - numRows].map { it % 9 + 1})
        }
    }
    return shortestDist(grid)
}

fun main() {
    check(part1("test") == 40)
    println(part1("input"))
    check(part2("test") == 315)
    println(part2("input"))
}