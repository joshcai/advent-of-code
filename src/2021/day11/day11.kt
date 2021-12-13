import java.io.File
import kotlin.collections.arrayListOf

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun step(curr: List<List<Int>>): Pair<List<List<Int>>, Int> {
    var visited = arrayListOf<ArrayList<Boolean>>()
    var next = arrayListOf<ArrayList<Int>>()
    for (i in curr.indices) {
        visited.add(ArrayList(curr[i].map { false }))
        next.add(ArrayList(curr[i].map { it + 1 }))
    }

    fun inc(i: Int, j: Int) {
        if (i < 0 || j < 0 || i >= curr.size || j >= curr[0].size) {
            return
        }
        next[i][j] += 1
    }
    var counter = 0
    do {
        var flashing = false
        for (i in next.indices) {
            for (j in next.indices) {
                if (visited[i][j]) {
                    continue
                }
                if (next[i][j] > 9) {
                    flashing = true
                    visited[i][j] = true
                    inc(i-1, j)
                    inc(i+1, j)
                    inc(i, j-1)
                    inc(i, j+1)
                    inc(i-1, j-1)
                    inc(i-1, j+1)
                    inc(i+1, j-1)
                    inc(i+1, j+1)
                    counter += 1
                }
            }
        }
    } while (flashing)
    for (i in visited.indices) {
        for (j in visited[i].indices) {
            if (visited[i][j]) {
                next[i][j] = 0
            }
        }
    }
    return Pair(next, counter)
}

fun print(grid: List<List<Int>>) {
    println("-----")
    for (i in grid.indices) {
        println(grid[i])
    }
    println("----")
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var grid = arrayListOf<List<Int>>()
    for (line in lines) {
        grid.add(line.split("").filter { it != "" }.map(String::toInt))
    }
    var counter = 0
    for (i in 1..100) {
        val (next, count) = step(grid)
        grid = ArrayList(next)
        counter += count
    }
    
    return counter
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var grid = arrayListOf<List<Int>>()
    for (line in lines) {
        grid.add(line.split("").filter { it != "" }.map(String::toInt))
    }
    val size = grid.size * grid[0].size
    var i = 1
    while (true) {
        val (next, count) = step(grid)
        if (count == size) {
            return i
        }
        grid = ArrayList(next)
        i += 1
    }
}

fun main() {
    check(part1("test") == 1656)
    println(part1("input"))
    check(part2("test") == 195)
    println(part2("input"))
}