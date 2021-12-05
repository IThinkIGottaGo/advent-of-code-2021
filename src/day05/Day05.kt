package day05

import util.readInput
import kotlin.math.absoluteValue

/**
 * --- 第 5 天：热泉历险 ---
 *
 * 你发现海底上有大量的深海热泉！这些热泉不断产生大量的、不透明的气团，所以如果可能的话最好还是避开它们。
 *
 * 这些热泉倾向于排成一行；潜艇会帮你生成一个列表（这便是你的谜题输入），其中包含了附近一行行的热泉供你查看。比如说：
 *
 * ```
 * 0,9 -> 5,9
 * 8,0 -> 0,8
 * 9,4 -> 3,4
 * 2,2 -> 2,1
 * 7,0 -> 7,4
 * 6,4 -> 2,0
 * 0,9 -> 2,9
 * 3,4 -> 1,4
 * 0,0 -> 8,8
 * 5,5 -> 8,2
 * ```
 *
 * 热泉形成的每一行都以线段 x1,y1 -> x2,y2 形式给出。其中 x1, y1 是线段一端的坐标；x2, y2 则是线段另一端的坐标。
 * 这些线段包含两端的端点。换句话说：
 *
 * - 像 1,1 -> 1,3 这样的条目意味着覆盖了点 1,1、1,2 和 1,3。
 * - 像 9,7 -> 7,7 这样的条目意味着覆盖了点 9,7、8,7 和 7,7。
 *
 * 现在，**只考虑呈水平和垂直的行**：即那些要么是 x1 = x2 或者 y1 = y2 的行。
 *
 * 因此，从上述列表中而来的水平和垂直的行将会产生下面这个图形：
 *
 * ```
 * .......1..
 * ..1....1..
 * ..1....1..
 * .......1..
 * .112111211
 * ..........
 * ..........
 * ..........
 * ..........
 * 222111....
 * ```
 *
 * 在上面这个图形中，最左上角是 0,0 最右下角是 9,9。每一个位置都以**某个点被线段经过的行数**，或者
 * 如果没有线段经过这个位置，则就是一个 . 号。比如，左上角的那对 1 就是从 2,2 -> 2,1 而来的。而最
 * 底下那行则是由 0,9 -> 5,9 和 0,9 -> 2,9 重叠而来。
 *
 * 为了避免最危险的区域，你需要确定**至少有两条线段重叠的点的数量**。在上面的例子中，图形中任何大于
 * 等于 2 的点的个数 —— 总共是 **5** 个点。
 *
 * 第一个问题：在只考虑水平和垂直行的情况下。**有多少个点至少被两条线重叠？**
 *
 * --- 第二部分 ---
 *
 * 不幸的是，只考虑水平线和垂直线并不能给你完整的画面；你还需要考虑**对角线**。
 *
 * 由于深海热泉测绘系统的限制，你列表中的线只可以是水平、垂直或者精确是 45 度的对角线。换句话说：
 *
 * - 像 1,1 -> 3,3 这样的条目意味着覆盖了点 1,1、2,2 和 3,3。
 * - 像 9,7 -> 7,9 这样的条目意味着覆盖了点 9,7、8,8 和 7,9。
 *
 * 考虑上面示例中的所有行，现在将生成下面这样的图形:
 *
 * ```
 * 1.1....11.
 * .111...2..
 * ..2.1.111.
 * ...1.2.2..
 * .112313211
 * ...1.2....
 * ..1...1...
 * .1.....1..
 * 1.......1.
 * 222111....
 * ```
 *
 * 你仍然需要确定**至少有两条线段重叠的点的数量**。在上面的例子中，图形中任何大于
 * 等于 2 的点的个数 —— 现在总共是 **12** 个点了。
 *
 * 第二个问题：考虑以上所有行。**有多少个点至少被两条线重叠？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val points = HashMap<Point, Point>()
        input.asSequence()
            .map {
                val (x1, y1, x2, y2) = it.split("->", ",").map(String::trim).map(String::toInt)
                Pair(Point(x1, y1), Point(x2, y2))
            }.map { Point.comparePairAndSwitch(it) }
            .forEach { (p1, p2) ->
                val (x1, y1) = p1
                val (x2, y2) = p2
                val pair = Pair(p1, p2)
                if (checkHorizontal(pair)) {
                    (x1..x2).forEach {
                        points.putOrAdd(Point(it, y1))
                    }
                } else if (checkVertical(pair)) {
                    (y1..y2).forEach {
                        points.putOrAdd(Point(x1, it))
                    }
                }
            }
        return points.count { it.key.count > 1 }
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val points = HashMap<Point, Point>()
        input.asSequence()
            .map {
                val (x1, y1, x2, y2) = it.split("->", ",").map(String::trim).map(String::toInt)
                Pair(Point(x1, y1), Point(x2, y2))
            }.map { Point.comparePairAndSwitch(it) }
            .forEach { (p1, p2) ->
                val (x1, y1) = p1
                val (x2, y2) = p2
                var pair = Pair(p1, p2)
                if (checkHorizontal(pair)) {
                    (x1..x2).forEach {
                        points.putOrAdd(Point(it, y1))
                    }
                } else if (checkVertical(pair)) {
                    (y1..y2).forEach {
                        points.putOrAdd(Point(x1, it))
                    }
                } else if (checkDiagonal(pair)) {
                    pair = diagonalNormalize(pair)
                    val (dx1, dy1) = pair.first
                    val (dx2, _) = pair.second
                    if (checkDiagonalPointLT2RD(pair)) {
                        (dx1..dx2).forEachIndexed { i, _ ->
                            points.putOrAdd(Point(dx1 + i, dy1 + i))
                        }
                    } else {
                        (dx1..dx2).forEachIndexed { i, _ ->
                            points.putOrAdd(Point(dx1 + i, dy1 - i))
                        }
                    }
                }
            }
        return points.count { it.key.count > 1 }
    }

    val testInput = readInput("day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("day05")
    check(part1(input) == 5690)
    check(part2(input) == 17741)
}

private fun checkHorizontal(pair: Pair<Point, Point>): Boolean {
    return pair.first.y == pair.second.y
}

private fun checkVertical(pair: Pair<Point, Point>): Boolean {
    return pair.first.x == pair.second.x
}

private fun checkDiagonal(pair: Pair<Point, Point>): Boolean {
    val (x1, y1) = pair.first
    val (x2, y2) = pair.second
    return ((y2 - y1) / (x2 - x1)).absoluteValue == 1
}

private fun HashMap<Point, Point>.putOrAdd(p: Point) {
    if (this.containsKey(p)) {
        ++this[p]!!.count
    } else {
        this[p] = p
        ++p.count
    }
}

private fun diagonalNormalize(pair: Pair<Point, Point>): Pair<Point, Point> {
    val (x1, y1) = pair.first
    val (x2, y2) = pair.second
    if (x2 > x1 && y2 > y1 || x1 < x2 && y1 > y2) {
        return pair
    }
    return Pair(pair.second, pair.first)
}

private fun checkDiagonalPointLT2RD(pair: Pair<Point, Point>): Boolean {
    val (x1, y1) = pair.first
    val (x2, y2) = pair.second
    return x2 > x1 && y2 > y1
}

private data class Point(val x: Int, val y: Int) {
    var count: Int = 0

    companion object {
        fun comparePairAndSwitch(pairPoint: Pair<Point, Point>): Pair<Point, Point> {
            if (checkVertical(pairPoint) && pairPoint.first.y > pairPoint.second.y) {
                return Pair(pairPoint.second, pairPoint.first)
            } else if (checkHorizontal(pairPoint) && pairPoint.first.x > pairPoint.second.x) {
                return Pair(pairPoint.second, pairPoint.first)
            }
            return pairPoint
        }
    }
}