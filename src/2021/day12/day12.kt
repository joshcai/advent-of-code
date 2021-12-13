import java.io.File
import kotlin.collections.arrayListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mapOf
import kotlin.collections.setOf
import kotlin.collections.mutableSetOf

fun readFile(file: String): List<String> {
    return File(file).readLines()
}

fun part1(file: String): Int {
    val lines = readFile(file)
    var edges = mutableMapOf<String, ArrayList<String>>().withDefault { arrayListOf<String>() }
    for (line in lines) {
        val (a, b) = line.split("-")
        val aEdges = edges.getValue(a)
        aEdges.add(b)
        edges[a] = aEdges
        val bEdges = edges.getValue(b)
        bEdges.add(a)
        edges[b] = bEdges
    }
    var counter = 0

    fun recurse(curr: String, visited: Set<String>) {
        if (curr == "end") {
            counter += 1
            return
        }
        var nextSet = visited.toMutableSet()
        nextSet.add(curr)
        for (edge in edges.getValue(curr)) {
            if (!edge[0].isUpperCase() && visited.contains(edge)) {
                continue
            }
            recurse(edge, nextSet)
        }
    }
    recurse("start", setOf<String>())
    return counter
}

fun part2(file: String): Int {
    val lines = readFile(file)
    var edges = mutableMapOf<String, ArrayList<String>>().withDefault { arrayListOf<String>() }
    for (line in lines) {
        val (a, b) = line.split("-")
        val aEdges = edges.getValue(a)
        aEdges.add(b)
        edges[a] = aEdges
        val bEdges = edges.getValue(b)
        bEdges.add(a)
        edges[b] = bEdges
    }
    var counter = 0

    fun recurse(curr: String, visited: Map<String, Int>) {
        if (curr == "end") {
            counter += 1
            return
        }
        var nextMap = visited.toMutableMap()
        nextMap[curr] = nextMap.getOrDefault(curr, 0) + 1
        var visitedSmallCave = false
        for ((key, value) in nextMap) {
            if (value > 1 && !key[0].isUpperCase()) {
                visitedSmallCave = true
            }
        }
        for (edge in edges.getValue(curr)) {
            if (edge == "start") {
                continue
            }
            if (!edge[0].isUpperCase() && visitedSmallCave &&
                visited.contains(edge)) {
                continue
            }
            recurse(edge, nextMap)
        }
    }
    recurse("start", mapOf<String, Int>())
    return counter
}

fun main() {
    check(part1("test") == 10)
    check(part1("test2") == 19)
    check(part1("test3") == 226)
    println(part1("input"))
    check(part2("test") == 36)
    check(part2("test2") == 103)
    check(part2("test3") == 3509)
    println(part2("input"))
}