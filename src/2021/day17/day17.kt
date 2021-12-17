fun part1(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Int {
    fun check(x: Int, y: Int): Int {
        var highest = 0
        var xVel = x
        var yVel = y
        var currX = 0
        var currY = 0
        for (i in 1..300) {
            currX += xVel
            currY += yVel
            if (currY > highest) {
                highest = currY
            }
            if (xVel > 0) {
                xVel -= 1
            } else if (xVel < 0) {
                xVel = 0
            }
            yVel -= 1
            if (currX >= xMin && currX <= xMax && currY >= yMin && currY <= yMax) {
                return highest
            }
        }
        return -1
    }
    var max = -1
    for (i in 1..300) {
        for (j in -150..150) {
            val next = check(i, j)
            if (next > max) {
                max = next 
            }
        }
    }
    return max
}

fun part2(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Int {
    fun check(x: Int, y: Int): Boolean {
        var highest = 0
        var xVel = x
        var yVel = y
        var currX = 0
        var currY = 0
        for (i in 1..500) {
            currX += xVel
            currY += yVel
            if (currY > highest) {
                highest = currY
            }
            if (xVel > 0) {
                xVel -= 1
            } else if (xVel < 0) {
                xVel = 0
            }
            yVel -= 1
            if (currX >= xMin && currX <= xMax && currY >= yMin && currY <= yMax) {
                return true
            }
        }
        return false
    }
    var counter = 0
    for (i in -1000..1000) {
        for (j in -1000..1000) {
            if (check(i, j)) {
                counter += 1
            }
        }
    }
    return counter
}

fun main() {
    check(part1(20, 30, -10, -5) == 45)
    println(part1(288, 330, -96, -50))
    check(part2(20, 30, -10, -5) == 112)
    println(part2(288, 330, -96, -50))
}