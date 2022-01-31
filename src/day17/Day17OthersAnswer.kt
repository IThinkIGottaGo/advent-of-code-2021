package day17

import util.readInputOneLine
import kotlin.math.absoluteValue

/**
 * Day17 第一、二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day17.kt)
 * 的解答。
 *
 * 解法思路采用了暴力模拟开始点的每个可能的初始速度，x 从 0 到目标区域的最右侧，y 最小值则和目标区域的最下沿坐标一致（因为“再小”则一发射出去就立即落在目标区域的最下沿之外了），
 * y 最大值则是目标区域最下沿坐标取绝对值（这是由于根据谜题下落的机制，上升时的初始速度时多少，下落时必然会回到发射时的的垂直坐标 0 处（但此时 x 可能前进了一段距离），
 * 且回到 y=0 处时恰好和最初向上发射的速度等大反向，因此若向上发射时速度比目标区域下沿坐标“再大”一点的话，就相当于在原点处向下发射比下沿还远一点的距离，将会直接飞出目标区域）。
 *
 * 之后使用无限的 sequence 来按照题目描述，模拟每一步运动的情况，并将每一个落点都 yield 到 sequence 中，结束条件由 sequence 的调用方决定
 * （第一个问题由 takeWhile 处理，直到某个点飞出目标区域位置，且该飞出点不会 yield 到 sequence 中）。Sequence 中 position 用来存储某个轨迹点所属的坐标，
 * 而 actualVelocity 则用来表示同个轨迹点时，此时所内含的 x 和 y 方向上的速度情况，并且这个速度情况将在运动到下一个坐标时作用在运动前所位于的坐标上。
 *
 * 如果由 sequence 产生的若干个轨迹点中存在某点位于目标区域内，则把这若干个轨迹点中 y 最大的值找出来，并巧妙替换为这整个求 x 和 y 坐标最大值的最大值（即整个 maxOf
 * 中最大值替换为这一系列轨迹中最大的 y 值，如果没有点落在目标区域中的轨迹则给其最大值 0 从而使得这无效轨迹不会在求最大值中胜出）。该替换而出的最大值这便是第一个问题的档案、
 *
 * 第二个问题则在 x 侧利用 sumOf 加和由 y 范围返回的，y 范围中有多少条轨迹有点落在目标区域中。而 y 侧则借助 count 只计数那些初始速度按 (x,y) 发射的，
 * 且轨迹中最后点落于目标区域内的初始点。
 *
 * 而控制无限 sequence 的调用方结束条件则是运动中的某个坐标落入了，或者飞出了目标区域为止。然后通过 first 获取这个第一个满足结束条件的坐标，
 * 若是属于目标区域内的坐标，则认为该 Point(x, y) 经过若干步后确实可以落入目标区域，这个起始速度可以被计入 y 侧的统计中。最终完成外部求和便是第二个问题的答案。
 */
fun main() {
    fun part1(input: String): Int {
        val (xRange, yRange) = initRange(input)
        val targetArea = TargetArea(xRange, yRange)
        return (0..xRange.last).maxOf { x ->
            (yRange.first..yRange.first.absoluteValue).maxOf { y ->
                val track = probePositions(Point(x, y)).takeWhile { !targetArea.hasOvershot(it) }
                if (track.any { it in targetArea }) track.maxOf { it.y } else 0
            }
        }
    }

    fun part2(input: String): Int {
        val (xRange, yRange) = initRange(input)
        val targetArea = TargetArea(xRange, yRange)
        return (0..xRange.last).sumOf { x ->
            (yRange.first..yRange.first.absoluteValue).count { y ->
                probePositions(Point(x, y)).first { targetArea.hasOvershot(it) || it in targetArea } in targetArea
            }
        }
    }

    val testInput = readInputOneLine("day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInputOneLine("day17")
    check(part1(input) == 4278)
    check(part2(input) == 1994)
}

private fun initRange(input: String): Pair<IntRange, IntRange> {
    val regex = Regex("""x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)""")
    val (x1, x2, y1, y2) = regex.find(input)!!.destructured
    return Pair(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt())
}

private fun probePositions(velocity: Point): Sequence<Point> = sequence {
    var position = Point(0, 0)
    var actualVelocity = velocity
    while (true) {
        position = Point(position.x + actualVelocity.x, position.y + actualVelocity.y)
        actualVelocity = Point(
            actualVelocity.x + if (actualVelocity.x > 0) -1 else if (actualVelocity.x < 0) 1 else 0,
            actualVelocity.y - 1
        )
        yield(position)
    }
}

@JvmRecord
data class Point(val x: Int, val y: Int)

private class TargetArea(val x: IntRange, val y: IntRange) {
    operator fun contains(point: Point) = point.x in x && point.y in y

    fun hasOvershot(point: Point) = point.x > x.last || point.y < y.first
}