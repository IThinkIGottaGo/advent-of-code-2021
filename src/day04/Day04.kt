package day04

import util.firstOrElse
import util.groupByIndexed
import util.readInput

/**
 * --- 第 4 天：巨型乌贼 ---
 *
 * 你已经位于海平面以下 1.5 公里(差不多 1 英里)处了，深到看不见任何阳光。然而，你能看到有一只巨大的乌贼，它附着在你的潜艇外面。
 *
 * 也许它想玩[宾果游戏 (bingo)](https://en.wikipedia.org/wiki/Bingo_(American_version))?
 *
 * 宾果游戏是在一组棋盘上进行的，每个棋盘又由 5x5 的数字方阵组成。数字是随机选择的，被选择的数字所出现的板子上还会对该数进行**标记**。
 * (数字有可能不会出现在所有的板子上。)如果某个棋盘上任意一行或一列的所有数字都被打上了标记，则该棋盘**胜出**。(对角线不计入在内)。
 *
 * 潜艇上有一个 **宾果子系统 (bingo subsystem)** 可以帮助乘客（当前就是你和巨型乌贼）打发时间。它会自动生成一个随机顺序来绘制数字和一组随机棋盘
 * （这便是你的谜题输入）。例如:
 *
 * ```
 * 7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
 *
 * 22 13 17 11  0
 *  8  2 23  4 24
 * 21  9 14 16  7
 *  6 10  3 18  5
 *  1 12 20 15 19
 *
 *  3 15  0  2 22
 *  9 18 13 17  5
 * 19  8  7 25 23
 * 20 11 10 24  4
 * 14 21 16 12  6
 *
 * 14 21 17 24  4
 * 10 16 15  9 19
 * 18  8 23 26 20
 * 22 11 13  6  5
 *  2  0 12  3  7
 * ```
 *
 * 在前 5 个数字绘制后（7，4，9，5，和 11），还暂时没有胜出的棋盘，但这些棋盘像下面这样被标记出来（下面使用水平相邻显示以节省空间）：
 *
 *```
 * 22  13  17 *11*    0       3   15   0   2  22       14   21 17  24  *4*
 *  8   2  23  *4*   24     *9*   18  13  17 *5*       10   16 15 *9*   19
 * 21 *9*  14   16  *7*      19    8 *7*  25  23       18    8 23  26   20
 *  6  10   3   18  *5*      20 *11*  10  24 *4*       22 *11* 13   6  *5*
 *  1  12  20   15   19      14   21  16  12   6        2    0 12   3  *7*
 *```
 *
 * 在接下来 6 个数字绘制后（17，23，2，0，14 和 21），还是没有分出胜者：
 *
 *```
 *   22   13 *17* *11*  *0*       3  15  *0*  *2*   22     *14* *21* *17*  24 *4*
 *    8  *2* *23*  *4*   24     *9*  18   13 *17*  *5*       10   16   15 *9*  19
 * *21*  *9* *14*   16  *7*      19   8  *7*   25 *23*       18    8 *23*  26  20
 *    6   10    3   18  *5*      20 *11*  10   24  *4*       22 *11*   13   6 *5*
 *    1   12   20   15   19    *14* *21*  16   12    6      *2*  *0*   12   3 *7*
 *```
 *
 * 最后，绘制 24：
 *
 *```
 *   22   13 *17* *11*  *0*       3  15  *0*  *2*   22     *14* *21* *17* *24* *4*
 *    8  *2* *23*  *4* *24*     *9*  18   13 *17*  *5*       10   16   15  *9*  19
 * *21*  *9* *14*   16  *7*      19   8  *7*   25 *23*       18    8 *23*   26  20
 *    6   10    3   18  *5*      20 *11*  10   24  *4*       22 *11*   13    6 *5*
 *    1   12   20   15   19    *14* *21*  16   12    6      *2*  *0*   12    3 *7*
 *```
 *
 * 这时，第三块棋盘胜出了，因为它完成了至少有一整行或一整列的数字全被标记（在本例中，被标记完全的是整个顶部行：**14 21 17 24 4**）。
 *
 * 现在可以计算胜出的棋盘上的**分数**了。首先找出该棋盘上所有**未被标记数字的总和**；在本例中，总和为 188。然后，
 * 将这个总和乘以棋盘刚才获胜时**最后调用的数字**，即 24，来得到最后的分数，188 * 24 = **4512**。
 *
 * 第一个问题：为了保证能够胜过巨型乌贼，你得先搞清楚哪块棋盘将会胜出。**如果你选择了会胜出的那块棋盘，那么这块棋盘的最终分数是多少？**
 *
 * --- 第二部分 ---
 *
 * 换个角度，尝试一种不同的策略可能才是明智的选择：让巨型乌贼胜出。
 *
 * 你并不知道一只巨型乌贼可以同时玩多少个宾果棋盘，所以与其浪费时间数它的手臂，更安全的做法是**找出哪块棋盘是最后才会胜出的**，然后就选择那块。
 * 这样一来，无论巨型乌贼选择哪个棋盘，它都必将先胜出。
 *
 * 在上面的例子中，第二块棋盘是最后胜出的，当走到最后 13 被选择时，这块棋盘的中间竖栏才被完全标记。如果你一直玩到直到这时，第二块棋盘上没有标记数字的总和将等于 148，
 * 最终得分则为 148 * 13 = **1924**。
 *
 * 第二个问题：找出哪块棋盘是最后一个胜出的。**一旦这块棋盘胜出了，它的最终分数是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val mutableInput = input.toMutableList()
        val selectList = mutableInput.removeFirst().split(',')
        val grids = mutableInput
            .flatMap { it.split("""\s+""".toRegex()) }
            .filterNot(String::isBlank)
            .map(::Element)
            .windowed(25, 25)
            .toList()
        selectList.forEach { selectNum ->
            grids.asSequence().flatten().forEach { if (it.e == selectNum) it.mark = true }
            grids.forEach {
                if (checkGridWin(it)) {
                    return calcUnmarkedSum(it) * selectNum.toInt()
                }
            }
        }
        error("Should not be here.")
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val mutableInput = input.toMutableList()
        val selectList = mutableInput.removeFirst().split(',')
        var grids = mutableInput
            .flatMap { it.split("""\s+""".toRegex()) }
            .filterNot(String::isBlank)
            .map(::Element)
            .windowed(25, 25)
            .toList()
        selectList.forEach { selectNum ->
            grids.asSequence().flatten().forEach { if (it.e == selectNum) it.mark = true }
            var lastGrid: List<Element> = listOf()
            grids = grids.filterNot { grid -> checkGridWin(grid).also { if (it) lastGrid = grid } }
            if (grids.isEmpty()) {
                return calcUnmarkedSum(lastGrid) * selectNum.toInt()
            }
        }
        error("Should not be here.")
    }

    val testInput = readInput("day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("day04")
    check(part1(input) == 32844)
    check(part2(input) == 4920)
}

private data class Element(val e: String) {
    var mark: Boolean = false
}

private fun checkGridWin(grid: List<Element>): Boolean {
    val winLine = grid.asSequence().windowed(5, 5).map { it.all(Element::mark) }.firstOrElse({ it }, false)
    if (winLine) return true
    val winColumn = grid.groupByIndexed { i, _ -> i % 5 }.values.asSequence().map { it.all(Element::mark) }
        .firstOrElse({ it }, false)
    if (winColumn) return true
    return false
}

private fun calcUnmarkedSum(grid: List<Element>): Int = grid.sumOf {
    if (it.mark) 0 else it.e.toInt()
}