import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableListOf
import kotlin.collections.mutableSetOf

typealias Numbers = List<Int>

data class Board(val numbers: List<List<Int>>, 
                 val set: Set<Int>, 
                 val currSet: MutableSet<Int>,
                 val canWin: MutableSet<Int>,
                 var hasWon: Boolean = false)

fun readFile(file: String): Pair<Numbers, List<Board>> {
    var lines = File(file).readLines()
    // To trigger another read of the board states at the end
    lines += ""
    val numbers = lines[0].split(",").map(String::toInt)
    var curr = arrayListOf<List<Int>>()
    val all = arrayListOf<Board>()
    for (i in lines.indices) {
        if (i < 2) {
            continue
        }
        val line = lines[i]
        if (line == "") {
            val set = mutableSetOf<Int>()
            for (row in curr) {
                set.addAll(row)
            }
            all.add(Board(curr, set, mutableSetOf<Int>(), mutableSetOf<Int>()))
            curr = arrayListOf<List<Int>>()
            continue
        }
        curr.add(line.split(" ")
                     .filter { it != "" }
                     .map(String::trim)
                     .map(String::toInt))
    }
    return Pair(numbers, all)
}

fun uncalledSum(board: Board): Int {
    var sum = 0
    for (row in board.numbers) {
        for (cell in row) {
            if (!board.currSet.contains(cell)) {
                sum += cell
            }
        }
    }
    return sum
}

fun updateBoard(board: Board, num: Int) {
    board.currSet.add(num)
    for (row in board.numbers) {
        val curr = arrayListOf<Int>()
        for (cell in row) {
            if (!board.currSet.contains(cell)) {
                curr.add(cell)
            }
            if (curr.size > 1) {
                break
            }
        }
        if (curr.size == 1) {
            board.canWin.add(curr[0])
        }
    }
    for (i in board.numbers[0].indices) {
        val curr = arrayListOf<Int>()
        for (j in board.numbers.indices) {
            val cell = board.numbers[j][i]
            if (!board.currSet.contains(cell)) {
                curr.add(cell)
            }
            if (curr.size > 1) {
                break
            }
        }
        if (curr.size == 1) {
            board.canWin.add(curr[0])
        }
    }
}

fun part1(file: String): Int {
    val (numbers, boards) = readFile(file)
    for (num in numbers) {
        for (board in boards) {
            if (board.canWin.contains(num)) {
                board.currSet.add(num)
                return uncalledSum(board) * num
            }
            if (!board.set.contains(num)) {
                continue
            }
            updateBoard(board, num)
        }
    }
    return -1
}

fun part2(file: String): Int {
    val (numbers, boards) = readFile(file)
    var currBoards = boards.toMutableList()
    var numBoards = boards.size
    var numWon = 0
    for (num in numbers) {
        for (board in currBoards) {
            if (board.hasWon) {
                continue
            }
            if (board.canWin.contains(num)) {
                board.currSet.add(num)
                board.hasWon = true
                numWon += 1
                if (numWon == numBoards) {
                    return uncalledSum(board) * num
                }
            }
            if (!board.set.contains(num)) {
                continue
            }
            updateBoard(board, num)
        }
    }
    return -1
}

fun main() {
    check(part1("test") == 4512)
    println(part1("input"))
    check(part2("test") == 1924)
    println(part2("input"))
}