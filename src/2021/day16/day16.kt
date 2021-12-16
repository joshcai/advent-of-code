import java.io.File

fun readFile(file: String): String {
    return File(file).readLines()[0]
}

fun getCharMap(): Map<Char, String> {
    return mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111",
    )
}

fun decodeLiteral(bits: String): Triple<Long, Int, String> {
    var curr = ""
    var counter = 0
    while (true) {
        val segment = bits.substring(counter * 5, (counter + 1) * 5)
        counter += 1
        curr += segment.substring(1, 5)
        if (segment[0] == '0') {
            break
        }
    }
    val number = curr.toLong(2)
    return Triple(number, counter * 5, bits.substring(counter * 5, bits.length))
}

fun part1(hex: String): Int {
    val charMap = getCharMap()
    val finalStr = hex.toCharArray().map{charMap.getValue(it)}.joinToString(separator="")
    var allVersions = 0

    fun decode(bits: String): Pair<Int, String> {
        if (bits.length < 7) {
            return Pair(bits.length, "")
        }
        val version = bits.substring(0, 3).toInt(2)
        allVersions += version
        val typeId = bits.substring(3, 6).toInt(2)
        if (typeId == 4) {
            val (_, length, rest) = decodeLiteral(bits.substring(6, bits.length))
            return Pair(length + 6, rest)
        } else {
            if (bits[6] == '0') {
                val totalLength = bits.substring(7, 22).toInt(2)
                var rest = bits.substring(22, bits.length)
                var lengthCounter = 0
                while (lengthCounter < totalLength) {
                    var (currLength, rest2) = decode(rest)
                    rest = rest2
                    lengthCounter += currLength
                }
                return Pair(22 + totalLength, rest)
            } else if (bits[6] == '1') {
                val iters = bits.substring(7, 18).toInt(2)
                var rest = bits.substring(18, bits.length)
                var lengthCounter = 0
                for (i in 1..iters) {
                    var (currLength, rest2) = decode(rest)
                    rest = rest2
                    lengthCounter += currLength
                }
                return Pair(18 + lengthCounter, rest)
            }
        }
        return Pair(-1, "")
    }
    decode(finalStr)
    return allVersions
}

fun part2(hex: String): Long {
    val charMap = getCharMap()
    val finalStr = hex.toCharArray().map{charMap.getValue(it)}.joinToString(separator="")

    fun decode(bits: String): Triple<Long, Int, String> {
        if (bits.length < 7) {
            return Triple(0, bits.length, "")
        }
        val typeId = bits.substring(3, 6).toInt(2)
        if (typeId == 4) {
            val (num, length, rest) = decodeLiteral(bits.substring(6, bits.length))
            return Triple(num, length + 6, rest)
        } 
        var subNumbers = arrayListOf<Long>()
        var rest = ""
        var offset = 0
        if (bits[6] == '0') {
            val totalLength = bits.substring(7, 22).toInt(2)
            rest = bits.substring(22, bits.length)
            var lengthCounter = 0
            while (lengthCounter < totalLength) {
                var (num, currLength, rest2) = decode(rest)
                subNumbers.add(num)
                rest = rest2
                lengthCounter += currLength
            }
            offset = 22 + totalLength
        } else if (bits[6] == '1') {
            val iters = bits.substring(7, 18).toInt(2)
            rest = bits.substring(18, bits.length)
            var lengthCounter = 0
            for (i in 1..iters) {
                var (num, currLength, rest2) = decode(rest)
                subNumbers.add(num)
                rest = rest2
                lengthCounter += currLength
            }
            offset = 18 + lengthCounter
        }

        var finalNum = 0.toLong()
        when (typeId) {
            0 -> finalNum = subNumbers.sum()
            1 -> finalNum = subNumbers.reduce { acc, i ->  acc * i }
            2 -> {
                var min = Long.MAX_VALUE
                for (num in subNumbers) {
                    if (num < min) {
                        min = num
                    }
                }
                finalNum = min
            }
            3 -> {
                var max = Long.MIN_VALUE
                for (num in subNumbers) {
                    if (num > max) {
                        max = num
                    }
                }
                finalNum = max
            }
            5 -> {
                if (subNumbers[0] > subNumbers[1]) {
                    finalNum = 1
                } else {
                    finalNum = 0
                }
            }
            6 -> {
                if (subNumbers[0] < subNumbers[1]) {
                    finalNum = 1
                } else {
                    finalNum = 0
                }
            }
            7 -> {
                if (subNumbers[0] == subNumbers[1]) {
                    finalNum = 1
                } else {
                    finalNum = 0
                }
            }
        }
        return Triple(finalNum.toLong(), offset, rest)
    }
    val (finalVal, _, _) = decode(finalStr)
    return finalVal
}

fun main() {
    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)
    println(part1(readFile("input")))

    check(part2("C200B40A82") == 3.toLong())
    check(part2("04005AC33890") == 54.toLong())
    check(part2("880086C3E88112") == 7.toLong())
    check(part2("CE00C43D881120") == 9.toLong())
    check(part2("D8005AC2A8F0") == 1.toLong())
    check(part2("F600BC2D8F") == 0.toLong())
    check(part2("9C005AC2F8F0") == 0.toLong())
    check(part2("9C0141080250320F1802104A08") == 1.toLong())
    println(part2(readFile("input")))
}