package day01

import util.readInputAsInts

/**
 * 同步由 **Kotlin by JetBrains** 官方油管频道、**kotlin-hands-on** 或其他渠道提供的解法。
 *
 * 能够更加 *地道(idiomatic)* 的以 Kotlin 语言来呈现代码。
 *
 * 本解法来自视频 [The Advent of Code 2021 in Kotlin, Day 1: Sonar Sweep](https://www.youtube.com/watch?v=76IzmtOyiHw)
 */
fun main() {
    fun part1(input: List<Int>): Int {
        return input.windowed(2).count { (a, b) -> a < b }
    }

    // A + B + C <=> B + C + D
    fun part2(input: List<Int>): Int {
        return input
            .windowed(4)
            .count {
                it[0] < it[3]
            }
    }

    val input = readInputAsInts("day01")
    println(part1(input))
    println(part2(input))
}