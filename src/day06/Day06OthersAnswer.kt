package day06

import util.readInputOneLine

/**
 * Day06 第二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day06.kt)
 * 的解答。
 *
 * 解法思路转变为建立一个大小为 9 的 Long 类型数组，数组的下标 0-8 则分别用来对应灯笼鱼定时器的各个情况，而下标存储的值用来表示此时有多少条灯笼鱼，
 * 其定时器等同于对应下标。
 *
 * 每经过一天都将数组整体左移一格（代表经过一天），并将左移前最左边下标 0 存储数值赋值到数组的末尾（代表原定时器为 0 的鱼，经过一天后每条都生成了一条新鱼，
 * 新鱼定时器则从 8 开始），再将下标为 8 的数量增加到下标为 6 的数量上（因为生成了新鱼后的每条旧鱼都是从 6 开始倒计时，而刚生出新鱼的旧鱼们数量全部都移动到了下标 8 上）。
 */
fun main() {
    fun part2(input: String): Long {
        val fishesPerDay = LongArray(9).apply { input.split(",").map(String::toInt).forEach { this[it] += 1L } }
        return fishesPerDay.simulateDays(256)
    }

    val testInput = readInputOneLine("day06_test")
    check(part2(testInput) == 26984457539L)

    val input = readInputOneLine("day06")
    check(part2(input) == 1770823541496L)
}

private fun LongArray.simulateDays(days: Int): Long {
    repeat(days) {
        rotateLeftInPlace()
        this[6] += this[8]
    }
    return this.sum()
}

private fun LongArray.rotateLeftInPlace() {
    val leftMost = first()
    copyInto(this, startIndex = 1)
    this[lastIndex] = leftMost
}