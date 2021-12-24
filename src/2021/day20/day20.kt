import java.io.File

fun strToList(s: String): List<Int> {
    return s.toCharArray().map {
        if (it == '#') 1 else 0
    }
}

fun readFile(file: String, iterations: Int): Pair<List<Int>, List<List<Int>>> {
    val lines = File(file).readLines()
    val decoder = lines[0]
    val list = arrayListOf<List<Int>>()
    // Create a buffer around the grid based on the number of iterations
    // we need to run. All infinite pixels outside should be the same
    // and in the case of the input / test, should be off on even steps.
    val bufferList = List<Int>(iterations) { 0 }
    for (i in lines.indices) {
        if (i < 2) {
            continue
        }
        val lineList = strToList(lines[i])
        list.add(listOf(bufferList, lineList, bufferList).flatten())
    }
    val verticalBufferList = List<Int>(list[0].size) { 0 }
    for (i in 1..iterations) {
        list.add(0, verticalBufferList)
        list.add(verticalBufferList)
    }
    return Pair(strToList(decoder), list)
}

fun step(grid: List<List<Int>>, decodeList: List<Int>): List<List<Int>> {
    val dirs = arrayOf(
        intArrayOf(-1, -1),
        intArrayOf(-1, 0),
        intArrayOf(-1, 1),
        intArrayOf(0, -1),
        intArrayOf(0, 0),
        intArrayOf(0, 1),
        intArrayOf(1, -1),
        intArrayOf(1, 0),
        intArrayOf(1, 1),
    )
    fun getValue(i: Int, j: Int, value: Int): Int {
        // If we're at the edge, we return the same value as the middle.
        // This works because all infinite pixels should have the same value.
        if (i < 0 || i >= grid.size || j < 0 || j >= grid[0].size) {
            return value
        }
        return grid[i][j]
    }

    fun nextVal(i: Int, j: Int): Int {
        val arr = arrayListOf<Int>()
        val value = grid[i][j]
        for (dir in dirs) {
            arr.add(getValue(i + dir[0], j + dir[1], value))
        }
        val str = arr.map{ it.toString() }.joinToString("")
        return decodeList[str.toInt(2)]
    }

    val next = arrayListOf<List<Int>>()
    for (i in grid.indices) {
        val nextRow = arrayListOf<Int>()
        for (j in grid[i].indices) {
            nextRow.add(nextVal(i, j))
        }
        next.add(nextRow)
    }
    return next
}

fun part(file: String, iterations: Int): Int {
    var (decodeList, grid) = readFile(file, iterations)
    for (i in 1..iterations) {
        grid = step(grid, decodeList)
    }
    var counter = 0
    for (row in grid) {
        for (cell in row) {
            if (cell == 1) {
                counter += 1
            }
        }
    }
    return counter
}

fun part1(file: String): Int {
    return part(file, 2)
}

fun part2(file: String): Int {
    return part(file, 50)
}

fun main() {
    check(part1("test") == 35)
    println(part1("input"))
    check(part2("test") == 3351)
    println(part2("input"))
}