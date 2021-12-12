import java.io.File
import kotlin.collections.arrayListOf

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    val grid = arrayListOf<List<Int>>()
    for (line in lines) {
        grid.add(line.split("").filter { it != "" }.map(String::toInt))
    }
    var counter = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val curr = grid[i][j]
            if (i - 1 >= 0 && grid[i-1][j] <= curr) {
                continue
            }
            if (i + 1 < grid.size && grid[i+1][j] <= curr) {
                continue
            }
            if (j - 1 >= 0 && grid[i][j-1] <= curr) {
                continue
            }
            if (j + 1 < grid[i].size && grid[i][j+1] <= curr) {
                continue
            }
            counter += curr + 1
        }
    }
    return counter
}

fun part2(file: String): Int {
    val lines = readFile(file)
    val visited = arrayListOf<ArrayList<Boolean>>()
    val grid = arrayListOf<List<Int>>()
    for (line in lines) {
        val numbers = line.split("").filter { it != "" }.map(String::toInt)
        visited.add(ArrayList(numbers.map { false }))
        grid.add(numbers)
    }

    fun fill(i: Int, j: Int): Int {
        if (i < 0 || j < 0 || i >= grid.size || j >= grid[0].size) {
            return 0
        }
        if (visited[i][j]) {
            return 0
        }
        visited[i][j] = true
        if (grid[i][j] == 9) {
            return 0
        }
        var counter = 1
        counter += fill(i-1, j)
        counter += fill(i+1, j)
        counter += fill(i, j-1)
        counter += fill(i, j+1)
        return counter
    }
    val basins = arrayListOf<Int>()
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val count = fill(i, j)
            if (count != 0) {
                basins.add(count)
            }
        }
    }
    basins.sortDescending()
    return basins[0] * basins[1] * basins[2]
}

fun main() {
    check(part1("test") == 15)
    println(part1("input"))
    check(part2("test") == 1134)
    println(part2("input"))
}