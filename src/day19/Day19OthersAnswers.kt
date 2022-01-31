package day19

import util.readInputOneLine
import kotlin.math.absoluteValue

/**
 * Day19 第一、二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day19.kt)
 * 的解答。
 *
 * 解法思路首先每个探测仪都总计有 24 种方向可以选择（比如 x 轴可以有 上下左右前后 六个朝向，而 x 轴固定某个方向后，y 和 z 轴又可以在 4 个象限中旋转
 * (谜题中要求所有探测器都按照 90 度整数倍数旋转，所以不会出现转 30、45 度之类的情况)，共计 6 * 4 = 24 种）。因此在 Point 类中增加了 face
 * 方法来返回一个新的 Point 面向不同的方向，以及 rotate 方法来旋转，这两个方法都接受数字来使得朝向或旋转不同的方向，这个数字将通过遍历来确保每一个方向都会被尝试。
 */
fun main() {
    fun part1(input: String): Int {
        val scanners = parseInput(input)
        return solve(scanners).beacons.size
    }

    fun part2(input: String): Int {
        val scanners = parseInput(input)
        return solve(scanners).scanners.pairs().maxOf { it.first distanceTo it.second }
    }

    val testInput = readInputOneLine("day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInputOneLine("day19")
    check(part1(input) == 436)
    check(part2(input) == 10918)
}

private typealias PointsSet = Set<Point>
private typealias PointsSetList = List<PointsSet>

private fun parseInput(input: String): PointsSetList = input.split("\n\n").map { scanner ->
    scanner.lines().drop(1).map { Point.of(it) }.toSet()
}

private fun solve(scanners: PointsSetList): Solution {
    val baseSector = scanners.first().toMutableSet()
    val foundScanners = mutableSetOf(Point(0, 0, 0))
    val unmappedSectors = ArrayDeque<PointsSet>().apply { addAll(scanners.drop(1)) }
    while (unmappedSectors.isNotEmpty()) {
        val thisSector = unmappedSectors.removeFirst()
        when (val transform = findTransformIfIntersects(baseSector, thisSector)) {
            null -> unmappedSectors.add(thisSector)
            else -> {
                baseSector.addAll(transform.beacons)
                foundScanners.add(transform.scanner)
            }
        }
    }
    return Solution(foundScanners, baseSector)
}

private fun findTransformIfIntersects(left: PointsSet, right: PointsSet): Transform? =
    (0 until 6).firstNotNullOfOrNull { facing ->
        (0 until 4).firstNotNullOfOrNull { rotation ->
            val rightReoriented = right.map { it.face(facing).rotate(rotation) }.toSet()
            left.firstNotNullOfOrNull { s1 ->
                rightReoriented.firstNotNullOfOrNull { s2 ->
                    val difference = s1 - s2
                    val moved = rightReoriented.map { it + difference }.toSet()
                    if (moved.intersect(left).size >= 12) {
                        Transform(difference, moved)
                    } else null
                }
            }
        }
    }

private fun PointsSet.pairs(): List<Pair<Point, Point>> =
    flatMapIndexed { i: Int, p1: Point ->
        drop(i).map { p2 -> p1 to p2 }
    }

@JvmRecord
private data class Transform(val scanner: Point, val beacons: PointsSet)

@JvmRecord
private data class Solution(val scanners: PointsSet, val beacons: PointsSet)

@JvmRecord
private data class Point(val x: Int, val y: Int, val z: Int) {
    fun face(facing: Int): Point =
        when (facing) {
            0 -> this
            1 -> Point(x, -y, -z)
            2 -> Point(x, -z, y)
            3 -> Point(-y, -z, x)
            4 -> Point(y, -z, -x)
            5 -> Point(-x, -z, -y)
            else -> error("Invalid facing.")
        }

    fun rotate(rotation: Int): Point =
        when (rotation) {
            0 -> this
            1 -> Point(-y, x, z)
            2 -> Point(-x, -y, z)
            3 -> Point(y, -x, z)
            else -> error("Invalid rotation.")
        }

    infix fun distanceTo(other: Point): Int =
        (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)

    companion object {
        @JvmStatic
        fun of(coordinate: String): Point {
            val (cx, cy, cz) = coordinate.split(",")
            return Point(cx.toInt(), cy.toInt(), cz.toInt())
        }
    }
}