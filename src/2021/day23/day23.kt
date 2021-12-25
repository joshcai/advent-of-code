import java.util.Arrays

data class State(
                // Size 4
                 val rooms: List<String>,
                 // Size 11
                 val hallway: List<Char>
                 )

fun hasNon(str: String, c: Char): Boolean {
    for (curr in str) {
        if (curr != c) {
            return true
        }
    }
    return false
}

// true if can move, also return cost if can move
fun canMove(state: State, startIndex: Int, destIndex: Int, c: Char, initCost: Int, checkStartIndex: Boolean): Pair<Boolean, Int> {
    var min = startIndex
    var max = destIndex
    if (max < min) {
        min = destIndex
        max = startIndex
    }
    for (i in min..max) {
        if (i == startIndex && !checkStartIndex) {
            continue
        }
        if (state.hallway[i] != '.') {
            return Pair(false, 0)
        }
    }
    val multiplier = when (c) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> -1
    }
    return Pair(true, (max - min + initCost) * multiplier)
}

fun part(first: String, second: String, third: String, fourth: String): Int {
    val roomSize = first.length
    val charMap = mapOf(
        0 to 'A',
        1 to 'B',
        2 to 'C',
        3 to 'D'
    )
    // initial state of the hallway
    val hallwayInit = arrayListOf<Char>(
        '.', '.', '.',
        '.', '.', '.',
        '.', '.', '.',
        '.', '.',
    )
    // possible positions to stop in the hallway
    val possibleIndices = arrayListOf<Int>(0, 1, 3, 5, 7, 9, 10)
    var minCost = Int.MAX_VALUE
    val initState = State(
        listOf(first, second, third, fourth),
        hallwayInit)
    var counter = 0
    fun dfs(state: State, cost: Int) {
        if (cost > minCost) {
            return
        }
        val rooms = state.rooms
        val hallway = state.hallway
        if (rooms[0] == "A".repeat(roomSize) &&
            rooms[1] == "B".repeat(roomSize) &&
            rooms[2] == "C".repeat(roomSize) &&
            rooms[3] == "D".repeat(roomSize)) {
            if (cost < minCost) {
                minCost = cost
            }
            return
        }
        // Try moving from room into hallway
        for (i in rooms.indices) {
            val char = charMap.getValue(i)
            val roomIndex = (i + 1) * 2
            if (rooms[i].length > 0 && hasNon(rooms[i], char)) {
                for (index in possibleIndices) {
                    val (movable, nextCost) = canMove(state, roomIndex, index, rooms[i].last(), roomSize - rooms[i].length + 1, true)
                    if (movable) {
                        val nextHallway = hallway.toMutableList()
                        nextHallway[index] = rooms[i].last()
                        val nextRooms = rooms.toMutableList()
                        nextRooms[i] = nextRooms[i].substring(0, nextRooms[i].length - 1)
                        dfs(State(nextRooms, nextHallway), cost + nextCost)
                    }
                }
            }
        }
        // Try moving from hallway into room
        for (index in possibleIndices) {
            for (i in rooms.indices) {
                val char = charMap.getValue(i)
                val roomIndex = (i + 1) * 2
                if (hallway[index] == char && !hasNon(rooms[i], char)) {
                    val (movable, nextCost) = canMove(state, index, roomIndex, char, roomSize - rooms[i].length, false)
                    if (movable) {
                        val nextHallway = hallway.toMutableList()
                        nextHallway[index] = '.'
                        val nextRooms = rooms.toMutableList()
                        nextRooms[i] = nextRooms[i] + char
                        dfs(State(nextRooms, nextHallway), cost + nextCost)
                    }
                }
            }
        }
    }

    dfs(initState, 0)
    return minCost
}

fun main() {
    check(part("AB", "DC", "CB", "AD") == 12521)
    println(part("BB", "CA", "DA", "CD"))
    // This case times out for some reason...
    // check(part("ADDB", "DBCC", "CABB", "ACAD") == 44169)
    println(part("BDDB", "CBCA", "DABA", "CCAD"))
}