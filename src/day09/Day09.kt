package day09

import util.readInput

/**
 * --- 第 9 天：冒烟的海盆 ---
 *
 * 这些洞穴看上去像是 [熔岩管道](https://en.wikipedia.org/wiki/Lava_tube) 。某些部分依然有火山活动；小型的热液喷口向洞窟中释放烟雾，
 * 这些烟雾像雨水一样慢慢沉淀下来。
 *
 * 如果你能建模出这些烟雾是如何穿过洞穴的，你或许就能够避免它，并且会更安全。潜艇为你生成了一个附近洞穴底部的高度地图（这便是你的谜题输入）。
 *
 * 烟雾会流向它所在区域的最低点。比如说，考虑下面这副高度地图：
 *
 * ```
 * 2*1*9 9 9 4 3 2 1*0*
 * 3 9 8 7 8 9 4 9 2 1
 * 9 8*5*6 7 8 9 8 9 2
 * 8 7 6 7 8 9 6 7 8 9
 * 9 8 9 9 9 6*5*6 7 8
 * ```
 *
 * 每一个数字都对应了一个特定的高度，其中 9 是最高处，而 0 是最低处。
 *
 * 你的第一个目标就是找出**最低点** —— 该位置比任何它相邻的位置都要更低。绝大部分位置都有 4 个相邻位置（上、下、左和右）；
 * 相应的，位于在地图边缘的位置则有 3 个或 2 个相邻的位置。（对角线位置不计入相邻）
 *
 * 在上面的示例中，有 **4** 个最低点，所有都用 * 号标注了：两个在第一行（一个 1 和一个 0），一个在第三行（一个 5），以及一个在最下面一行（也是一个 5）.
 * 在高度地图上除上述以外的其他位置都有一些更低的相邻位置，所以其他点不是低点。
 *
 * 某个低点的 **风险等级(risk level)** 是由 **某个低点的高度加上 1** 得到的。在上面的例子中，低点的风险登记分别是 2，1，6 和 6。
 * 高度地图上所有这些低点的风险等级总和为 **15**。
 *
 * 第一个问题：在你的高度地图上找到所有的低点。**在你高度地图上所有这些低点的风险总和加起来等于多少？**
 *
 * --- 第二部分 ---
 *
 * 接下来你需要找到最大的海盆(basin)地，这样你就知道哪些区域最好避免前往。
 *
 * 所谓 **盆地/海盆(basin)** 指的是所有位置的烟雾最终都会流向的某个单独的低点。因此，尽管某些盆地非常的小，但该低点都算有一个盆地。
 * 高度为 9 的位置不算在任何某个盆地中，而其他所有的位置总是属于某个盆地的一部分。
 *
 * 盆地的**大小**取决于盆地中位置的个数，包括低点。上面的例子中则共有 4 个盆地。
 *
 * 左上方盆地，大小是 3：
 *
 *
 * **21**99943210
 *
 * **3**987894921
 *
 *  9856789892
 *
 *  8767896789
 *
 *  9899965678
 *
 * 右上方盆地，大小是 9：
 *
 * 21999**43210**
 *
 * 398789**4**9**21**
 *
 * 985678989**2**
 *
 * 8767896789
 *
 * 9899965678
 *
 * 中间的盆地，大小为 14：
 *
 * 2199943210
 *
 * 39**878**94921
 *
 * 9**85678**9892
 *
 * **87678**96789
 *
 * 9**8**99965678
 *
 * 右下方的盆地，大小为 9：
 *
 * 2199943210
 *
 * 3987894921
 *
 * 9856789**8**92
 *
 * 876789**678**9
 *
 * 98999**65678**
 *
 * 找到最大的三个盆地，并将它们的大小乘起来。在上面的例子中是 9 * 14 * 9 = **1134**。
 *
 * 第二个问题：**如果你将三个最大的盆地的大小乘起来，得到的结果是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val column = input[0].length
        val s = input.joinToString("")
        var riskLevel = 0
        for (i in s.indices) {
            if (checkLowPoint(s, column, i)) {
                riskLevel += s[i].digitToInt() + 1
            }
        }
        return riskLevel
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val column = input[0].length
        val s = input.joinToString("")
        val resultSize = mutableListOf<Int>()
        val resultIndex = mutableSetOf<Int>()
        val passedIndex = mutableSetOf<Int>()
        for (i in s.indices) {
            if (checkLowPoint(s, column, i)) {
                resultIndex.add(i)
                passedIndex.add(i)
                resultSize.add(basinSearch(s, column, i, resultIndex, passedIndex).size)
                resultIndex.clear()
                passedIndex.clear()
            }
        }
        resultSize.sort()
        resultSize.reverse()
        val (first, second, third) = resultSize
        return first * second * third
    }

    val testInput = readInput("day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("day09")
    check(part1(input) == 545)
    check(part2(input) == 950600)
}

private fun checkLowPoint(s: String, column: Int, i: Int): Boolean {
    var min = true
    if (i % column != 0 && s[i - 1] <= s[i]) min = false
    if ((i + 1) % column != 0 && s[i + 1] <= s[i]) min = false
    if (i - column >= 0 && s[i - column] <= s[i]) min = false
    if (i + column < s.length && s[i + column] <= s[i]) min = false
    return min
}

private fun basinSearch(
    s: String,
    column: Int,
    i: Int,
    results: MutableSet<Int>,
    passed: MutableSet<Int>
): MutableSet<Int> {
    passed.add(i)
    if (s[i] == '9') return results
    if (!passed.contains(i + 1) && (i + 1) % column != 0) {
        basinSearch(s, column, i + 1, results, passed)
    }
    if (!passed.contains(i - 1) && i % column != 0) {
        basinSearch(s, column, i - 1, results, passed)
    }
    if (!passed.contains(i - column) && i - column >= 0) {
        basinSearch(s, column, i - column, results, passed)
    }
    if (!passed.contains(i + column) && i + column < s.length) {
        basinSearch(s, column, i + column, results, passed)
    }
    results.add(i)
    return results
}