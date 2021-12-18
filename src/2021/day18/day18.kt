import java.io.File

data class Num(var left: Num? = null, var right: Num? = null, var literal: Int = -1)


fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun toString(num: Num): String {
    fun recurse(num: Num): String {
        if (num.literal >= 0) {
            return num.literal.toString()
        }
        return "[" + recurse(num.left!!) + "," + recurse(num.right!!) + "]"
    }
    return recurse(num)
}

fun explode(num: Num): Pair<Num, Boolean> {
    val numStr = toString(num)
    val numQueue = arrayListOf<Num>()
    val reg = Regex("(?<=[\\[\\]\\,])|(?=[\\[\\]\\,])")
    var list = numStr.split(reg)
    var depth = 0
    var carryRight = 0
    var changed = false
    for (c in list) {
        if (c == "," || c == "") {
            continue
        }
        if (c == "[") {
            depth += 1
            continue
        }
        if (c == "]") {
            val right = numQueue.removeLast()
            val left = numQueue.removeLast()
            if (depth == 5 && !changed) {
                changed = true
                if (numQueue.size > 0) {
                    if (numQueue.last().literal >= 0) {
                        val last = numQueue.last()
                        last.literal += left.literal
                    } else {
                        // throw Exception("Weird")
                        var rightMost = numQueue.last()
                        while (rightMost.literal == -1) {
                            rightMost = rightMost.right!!
                        }
                        rightMost.literal += left.literal
                    }
                }
                numQueue.add(Num(literal=0))
                carryRight = right.literal
            } else {
                numQueue.add(Num(left=left, right=right))
            }
            depth -= 1
            continue
        }
        var curr = c.toString().toInt()
        if (carryRight != 0) {
            curr += carryRight
            carryRight = 0
        }
        numQueue.add(Num(literal=curr))
    }
    if (numQueue.size != 1) {
        throw Exception("num queue size should be 1 after exploding")
    }
    return Pair(numQueue.first(), changed)
}

fun splitNum(n: Int): Pair<Int, Int> {
    if (n % 2 == 0) {
        return Pair(n/2, n/2)
    }
    return Pair(n/2, n/2 + 1)
}

fun split(num: Num): Pair<Num, Boolean> {
    var changed = false
    fun recurse(num: Num) {
        if (num.literal >= 0) {
            return
        }
        if (num.left!!.literal >= 10 && !changed) {
            changed = true
            val left = num.left
            val (a, b) = splitNum(left!!.literal)
            num.left = Num(left=Num(literal=a), right=Num(literal=b))
            return
        }
        val left = num.left
        recurse(left!!)
        if (num.right!!.literal >= 10 && !changed) {
            changed = true
            val right = num.right
            val (a, b) = splitNum(right!!.literal)
            num.right = Num(left=Num(literal=a), right=Num(literal=b))
            return
        }
        val right = num.right
        recurse(right!!)
    }
    recurse(num)
    return Pair(num, changed)
}

fun reduce(num: Num): Num {
    val (newNum, changed) = explode(num)
    if (changed) {
        return reduce(newNum)
    }
    val (splitNum, changed2) = split(num)
    if (changed2) {
        return reduce(splitNum)
    }
    // Already reduced
    return splitNum
}

fun toNum(line: String): Num {
    val numQueue = arrayListOf<Num>()
    val reg = Regex("(?<=[\\[\\]\\,])|(?=[\\[\\]\\,])")
    var list = line.split(reg)
    for (c in list) {
        if (c == "[" || c == "," || c == "") {
            continue
        }
        if (c == "]") {
            val right = numQueue.removeLast()
            val left = numQueue.removeLast()
            numQueue.add(Num(left=left, right=right))
            continue
        }
        val curr = c.toString().toInt()
        numQueue.add(Num(literal=curr))
    }
    if (numQueue.size != 1) {
        throw Exception("num queue size should be 1 after parsing")
    }
    return numQueue.first()
}

fun magnitude(num: Num): Int {
    if (num.literal >= 0) {
        return num.literal
    }
    return 3 * magnitude(num.left!!) + 2 * magnitude(num.right!!)
}

fun part1(file: String): Int {
    val lines = readFile(file)
    val numQueue = arrayListOf<Num>()
    for (line in lines) {
        numQueue.add(toNum(line))
        if (numQueue.size > 2) {
            throw Exception("unexpected number of items in queue")
        }
        if (numQueue.size == 2) {
            val right = numQueue.removeLast()
            val left = numQueue.removeLast()
            numQueue.add(reduce(Num(left=left, right=right)))
        }
    }
    return magnitude(numQueue.first())
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var max = 0
    for (i in lines.indices) {
        for (j in lines.indices) {
            if (j <= i) {
                continue
            }
            var next = magnitude(reduce(Num(left=toNum(lines[i]), right=toNum(lines[j]))))
            if (next > max) {
                max = next
            }
            next = magnitude(reduce(Num(left=toNum(lines[j]), right=toNum(lines[i]))))
            if (next > max) {
                max = next
            }
        }
    }
    return max
}

fun main() {
    check(toString(toNum("[[[[[9,8],1],2],3],4]")) == "[[[[[9,8],1],2],3],4]")
    check(toString(toNum("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")) == "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
    println("passed parse")

    check(toString(split(toNum("[[[[0,7],4],[15,[0,13]]],[1,1]]")).first) == "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")
    check(toString(split(toNum("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")).first) == "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")
    println("passed split")

    check(toString(explode(toNum("[[[[[9,8],1],2],3],4]")).first) == "[[[[0,9],2],3],4]")
    check(toString(explode(toNum("[7,[6,[5,[4,[3,2]]]]]")).first) == "[7,[6,[5,[7,0]]]]")
    check(toString(explode(toNum("[[6,[5,[4,[3,2]]]],1]")).first) == "[[6,[5,[7,0]]],3]")
    check(toString(explode(toNum("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")).first) == "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    check(toString(explode(toNum("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")).first) == "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    println("passed explode")

    check(magnitude(toNum("[[1,2],[[3,4],5]]")) == 143)
    check(magnitude(toNum("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")) == 1384)
    check(magnitude(toNum("[[[[1,1],[2,2]],[3,3]],[4,4]]")) == 445)
    check(magnitude(toNum("[[[[3,0],[5,3]],[4,4]],[5,5]]")) == 791)
    check(magnitude(toNum("[[[[5,0],[7,4]],[5,5]],[6,6]]")) == 1137)
    check(magnitude(toNum("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")) == 3488)
    println("passed magnitude")

    check(part1("test") == 4140)
    check(part1("test2") == 3488)
    println(part1("input"))

    check(part2("test") == 3993)
    println(part2("input"))
}