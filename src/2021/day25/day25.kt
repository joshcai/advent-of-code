import java.io.File

typealias Grid = List<List<Char>>

fun readFile(file: String): Grid {
    val lines = File(file).readLines()
    return lines.map{ it.toList() }
}

fun eastStep(grid: List<List<Char>>): Pair<Grid, Int> {
    val nextGrid = arrayListOf<MutableList<Char>>()
    for (i in grid.indices) {
        nextGrid.add(grid[i].toMutableList())
    }
    var moves = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val c = grid[i][j]
            if (c != '>') {
                continue
            }
            var next = j + 1
            if (j == grid[i].size - 1) {
                next = 0
            }
            if (grid[i][next] == '.') {
                moves += 1
                nextGrid[i][next] = '>'
                nextGrid[i][j] = '.'
            }
        }
    }
    return Pair(nextGrid, moves)
}

fun southStep(grid: List<List<Char>>): Pair<Grid, Int> {
    val nextGrid = arrayListOf<MutableList<Char>>()
    for (i in grid.indices) {
        nextGrid.add(grid[i].toMutableList())
    }
    var moves = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val c = grid[i][j]
            if (c != 'v') {
                continue
            }
            var next = i + 1
            if (i == grid.size - 1) {
                next = 0
            }
            if (grid[next][j] == '.') {
                moves += 1
                nextGrid[next][j] = 'v'
                nextGrid[i][j] = '.'
            }
        }
    }
    return Pair(nextGrid, moves)
}

fun part1(file: String): Int {
    var step = 0
    var grid = readFile(file)
    while (true) {
        var moves = 0
        val (nextGrid, eastMoves) = eastStep(grid)
        moves += eastMoves
        val (nextNextGrid, southMoves) = southStep(nextGrid)
        moves += southMoves
        step += 1
        if (moves == 0) {
            return step
        }
        grid = nextNextGrid
    }
    return -1
}

fun main() {
    check(part1("test") == 58)
    println(part1("input"))
}