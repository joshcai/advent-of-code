import java.util.Arrays

fun part1(aStart: Int, bStart: Int): Int {
    val score = arrayListOf<Int>(0, 0)
    val curr = arrayListOf<Int>(aStart, bStart)
    var index = 0
    var die = 1
    while (true) {
        curr[index] 
        var nextNum = die * 3 + 3
        die += 3
        var moves = nextNum % 10
        curr[index] += moves
        if (curr[index] > 10) {
            curr[index] -= 10
        }
        score[index] += curr[index]
        if (score[index] >= 1000) {
            return score[(index + 1) % 2] * (die - 1)
        }
        index = (index + 1) % 2
    }
}

// Based on https://www.reddit.com/r/adventofcode/comments/rl6p8y/2021_day_21_solutions/hpkxh2c/
fun part2(aStart: Int, bStart: Int): Long {
    // map of frequencies of sums when rolling three die
    val incMap = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
    fun wins(p1: Int, score1: Int, p2: Int, score2: Int): Pair<Long, Long> {
        if (score2 >= 21) {
            return Pair(0.toLong(), 1.toLong())
        }

        var wins1 = 0.toLong()
        var wins2 = 0.toLong()
        for ((roll, freq) in incMap) {
            var nextPos = p1 + roll
            if (nextPos > 10) {
                nextPos -= 10
            }
            var (prevWins1, prevWins2) = wins(p2, score2, nextPos, score1 + nextPos)
            wins1 += freq * prevWins2
            wins2 += freq * prevWins1
        }
        return Pair(wins1, wins2)
    }
    val (final1, final2) = wins(aStart, 0, bStart, 0)
    if (final1 > final2) {
        return final1
    }
    return final2
}

fun main() {
    check(part1(4, 8) == 739785)
    println(part1(6, 8))
    check(part2(4, 8) == 444356092776315)
    println(part2(6, 8))
}