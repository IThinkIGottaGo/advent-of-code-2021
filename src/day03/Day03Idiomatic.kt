package day03

import util.readInput

/**
 * 同步由 **Kotlin by JetBrains** 官方油管频道、**kotlin-hands-on** 或其他渠道提供的解法。
 *
 * 能够更加 *地道(idiomatic)* 的以 Kotlin 语言来呈现代码。
 *
 * 本解法来自视频 [Advent of Code 2021 in Kotlin, Day 3: Binary Diagnostic](https://www.youtube.com/watch?v=mF2PTnnOi8w)
 */
fun main() {
    fun part1(input: List<String>): Int {
        val columns = input[0].indices
        val gammaRate = buildString {
            for (column in columns) {
                val (zeroes, ones) = input.countBitsInColumn(column)
                val commonBit = if (zeroes > ones) "0" else "1"
                append(commonBit)
            }
        }
        // more functional way
        val gammaRate2 = columns
            .map { input.countBitsInColumn(it) }
            .joinToString("") { (zeroes, ones) ->
                if (zeroes > ones) "0" else "1"
            }
        val epsilonRate = gammaRate.invertBinaryString()
        return gammaRate.toInt(2) * epsilonRate.toInt(2)
    }

    fun part2(input: List<String>): Int {
        fun rating(type: RatingType): String {
            val columns = input[0].indices
            var candidates = input
            for (column in columns) {
                val (zeroes, ones) = candidates.countBitsInColumn(column)
                val mostCommon = if (zeroes > ones) '0' else '1'
                // if you use a mutable list, there is also a [removeIf] func could use, not only the [filter]
                candidates = candidates.filter {
                    when (type) {
                        RatingType.OXYGEN -> it[column] == mostCommon
                        RatingType.CO2 -> it[column] != mostCommon
                    }
                }
                if (candidates.size == 1) break
            }
            return candidates.single()
        }
        return rating(RatingType.OXYGEN).toInt(2) * rating(RatingType.CO2).toInt(2)
    }

    val testInput = readInput("day03_test")
    val input = readInput("day03")
    println("P1 Test: " + part1(testInput))
    check(part1(testInput) == 198)
    println("P1 Result: " + part1(input))
    println("P2 Test: " + part2(testInput))
    check(part2(testInput) == 230)
    println("P2 Result: " + part2(input))
}

private enum class RatingType {
    OXYGEN,
    CO2
}

private fun String.invertBinaryString() =
    this.asIterable()
        .joinToString("") { if (it == '0') "1" else "0" }

private fun List<String>.countBitsInColumn(column: Int): BitCount {
    var zeroes = 0
    var ones = 0
    for (line in this) {
        if (line[column] == '0') zeroes++ else ones++
    }
    return BitCount(zeroes, ones)
}

data class BitCount(val zeroes: Int, val ones: Int)