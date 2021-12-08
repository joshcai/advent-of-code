import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun readFile(file: String): List<Int> {
    return File(file).readLines()[0].split(",").map(String::toInt)
}

fun calc(nums: List<Int>, median: Int): Int {
    return nums.map { abs(it - median)}.sum()
}

fun part1(file: String): Int {
    val nums = readFile(file).toMutableList()
    nums.sort()
    return min(calc(nums, nums[nums.size/2]), calc(nums, nums[nums.size/2 + 1]))
}

fun calc2(nums: List<Int>, test: Int): Int {
    return nums.map { abs(it - test) * (abs(it - test) + 1) / 2 }.sum()
}

fun part2(file: String): Int {
    val nums = readFile(file)
    var maxNum = -1
    var minNum = -1
    for (num in nums) {
        if (minNum == -1 || num < minNum) {
            minNum = num
        }
        if (num > maxNum) {
            maxNum = num
        }
    }
    var lo = minNum
    var hi = maxNum
    while (lo < hi) {
        var mid = (lo + hi) / 2
        var midVal = calc2(nums, mid)
        var lowerVal = calc2(nums, mid - 1)
        var higherVal = calc2(nums, mid + 1)
        if (midVal < lowerVal && midVal < higherVal) {
            return midVal
        }

        if (lowerVal < midVal) {
            hi = mid - 1
            continue
        }
        if (higherVal < midVal) {
            lo = mid + 1
        }
    }
    return calc(nums, lo)
}



fun main() {
    check(part1("test") == 37)
    println(part1("input"))
    check(part2("test") == 168)
    println(part2("input"))
}