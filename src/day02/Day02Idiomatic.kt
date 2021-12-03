package day02

import util.readInput

/**
 * 同步由 **Kotlin by JetBrains** 官方油管频道、**kotlin-hands-on** 或其他渠道提供的解法。
 *
 * 能够更加 *地道(idiomatic)* 的以 Kotlin 语言来呈现代码。
 *
 * 本解法来自视频 [Advent of Code 2021 in Kotlin, Day 2: Dive!](https://www.youtube.com/watch?v=4A2WwniJdNc)
 */
fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var horizontalPosition = 0
        val operation = input.map { it.split(' ') }
        for ((direction, amountString) in operation) {
            val amount = amountString.toInt()
            when (direction) {
                "up" -> depth -= amount
                "down" -> depth += amount
                "forward" -> horizontalPosition += amount
            }
        }
        return depth * horizontalPosition
    }

    data class Operation(val direction: String, val amount: Int)

    fun part2(input: List<String>): Int {
        var aim = 0
        var depth = 0
        var horizontalPosition = 0
        val operation = input.map { it.split(' ') }.map { Operation(it[0], it[1].toInt()) }
        for ((direction, amount) in operation) {
            when (direction) {
                "up" -> aim -= amount
                "down" -> aim += amount
                "forward" -> {
                    horizontalPosition += amount
                    depth += amount * aim
                }
            }
        }
        return depth * horizontalPosition
    }

    val testInput = readInput("day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02")
    check(part1(input) == 1580000)
    check(part2(input) == 1251263225)
}