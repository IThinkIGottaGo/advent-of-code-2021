package day01

import util.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("day01_test")
    check(part1(testInput) == 1)

    val input = readInput("day01")
    println(part1(input))
    println(part2(input))
}