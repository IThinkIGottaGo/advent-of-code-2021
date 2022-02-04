package day22

import util.readInput
import kotlin.math.max
import kotlin.math.min

/**
 * Day22 第一、二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day22.kt)
 * 的解答。
 *
 * 解法思路的关键在于想象存在正体积的立方体和负体积的立方体。我们可以想象有一个 x * y * z 的立方体，其所有组成的块都是 on 状态的，然后这时有另一个全部都是 on
 * 组成的立方体重叠过来，其中就会形成一块重叠的部分，这个重叠的部分也是一个立方体，并且整体的体积需要减去一次重叠的部分。另外，再想象我们这个 on
 * 状态的立方体去和另一个全部为 off 的立方体重叠，此时我们需要从整个体积中减去重叠的部分（因为重叠的部分被关闭了）。最后一种情况，全部为 off
 * 的立方体和另一个全部为 off 的立方体重叠，我们也不能把重叠的地方减去两次，这和两个都是 on 的情况类似，因此需要加上一次重叠部分的体积。
 *
 * 为此，每一个立方体要么是正体积，要么是负体积。如果某个立方体是 on 或者 off，那么我们就把它去和所有已知的立方体进行比较，并求出每个的相交区域，
 * 这个相交区域将产生一个新的立方体，但其状态与被比较的立方体相反（若被比较为正体积(on)则交集立方体为负，若被比较为负体积(off)则交集立方体为正），
 *
 * 已知的立方体集合 volume 则由交集立方体（可能为 on 和 off）和谜题输入中为 on 的立方体组成。
 *
 * 为了能够更好的处理交集，为 IntRange 扩展了 isIntersect 和 intersect 方法，来获取某一个轴的交集。还为立方体对象 Cuboid 创建了 isIntersect
 * 和 intersect 方法来对两个立方体求交集部分，只有三个轴上的 IntRange 都存在相交时才认为两个立方体也相交。
 *
 * 立方体的 intersect 方法使用了 !on 来获得相交的立方体的状态始终是和调用方的立方体状态相反的。
 *
 * 最终将已知立方体集合 volume 中的每个立方体都计算其正体积或负体积，并累加在一起便得到了谜题的答案。
 */
fun main() {
    fun part1(input: List<String>): Long {
        val initializeProcedureRegion = Cuboid(true, -50..50, -50..50, -50..50)
        val cuboids = input.map { Cuboid.of(it) }.filter { it isIntersect initializeProcedureRegion }
        return solve(cuboids)
    }

    fun part2(input: List<String>): Long {
        val cuboids = input.map { Cuboid.of(it) }
        return solve(cuboids)
    }

    val testInput = readInput("day22_test")
    check(part1(testInput) == 474140L)
    check(part2(testInput) == 2758514936282235L)

    val input = readInput("day22")
    check(part1(input) == 609563L)
    check(part2(input) == 1234650223944734L)
}

private val regex = Regex("""(\w+) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""")

private fun solve(cuboids: List<Cuboid>): Long {
    val volume = mutableListOf<Cuboid>()
    cuboids.forEach { cube ->
        volume.addAll(volume.mapNotNull { it.intersect(cube) })
        if (cube.on) volume.add(cube)
    }
    return volume.sumOf { it.volume() }
}

private fun IntRange.size() = last - first + 1

private infix fun IntRange.isIntersect(other: IntRange) = first <= other.last && last >= other.first

private infix fun IntRange.intersect(other: IntRange) = max(first, other.first)..min(last, other.last)

@JvmRecord
private data class Cuboid(val on: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    fun volume(): Long = xRange.size().toLong() * yRange.size() * zRange.size() * if (on) 1 else -1

    infix fun isIntersect(other: Cuboid): Boolean =
        xRange isIntersect other.xRange && yRange isIntersect other.yRange && zRange isIntersect other.zRange

    fun intersect(other: Cuboid): Cuboid? =
        if (isIntersect(other)) Cuboid(
            !on,
            xRange intersect other.xRange,
            yRange intersect other.yRange,
            zRange intersect other.zRange
        )
        else null

    companion object {
        @JvmStatic
        fun of(s: String): Cuboid {
            val (type, x1, x2, y1, y2, z1, z2) = regex.find(s)!!.destructured
            return Cuboid(type == "on", x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt(), z1.toInt()..z2.toInt())
        }
    }
}