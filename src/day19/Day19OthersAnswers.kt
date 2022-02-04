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
 *
 * rotate 旋转方法比较好理解，作者是固定了 z 轴，然后将 x 和 y 轴围绕 z 轴在四个象限各转了一圈，从而获得了 (x,y) 原点，(-y,x) 绕 z 轴逆时针 90度，
 * (-x,-y) 在 (-y,x) 的基础上再绕 z 轴逆时针转 90 度，(y,-x) 在 (-x,-y) 的基础上再绕 z 轴逆时针转 90 度，从而完成了旋转一圈。共计 4 中可能旋转情况。
 *
 * face 朝向方法则相当于朝上 (x,y,z)，朝下 (x,-y,-z)，然后围绕 x 轴向前转 y 和 z 90度 (x,-z,y)，再在这个基础上固定 z 轴旋转 x 和 y 轴一圈得到
 * (-y,-z,x)，(y,-z,-x)，(-x,-z,-y) 三个朝向。共计 6 种。遍历 6 x 4 = 24 种情况即可把每个信标可能的朝向都走一遍。
 *
 * 在单个信标的方向可以调整后，现在要确定任意两个 scanner 扫出的信标集合之间的交集。findTransformIfIntersects 方法接受任意两个扫描器获得的信标集合，
 * 并以左集合为基准，在右集合中变更每个元素的方向，24 种方向中的每一种都由参数 face 和 rotation 传入。传入某一种情况后，先对右集合使用 map 来变换，
 * 变换完毕后，将左集合 left 中的每一个元素，都去和变换后右集合 rightReoriented 中的每一个元素做遍历，每一轮遍历都取 left 集合中当前遍历的元素与
 * rightReoriented 集合中当前遍历的元素做减法，并把结果存储在 difference 中，之后，对 rightReoriented 中的每一个元素都加上这个差异，并转化为 Set
 * 类型（由于都是不可变集合，所以返回的是一个每个元素都增加了差异的新集合），将这个增加了差异的新集合去和左集合求交集，如果两个集合中此时都有的元素超过了
 * 12 个，则说明这两个扫描器公共的信标超过了 12 个，说明这两个扫描器是成配对的，就返回这个差异，以及这个变换后、且增加了差异对齐了左集合的新右集合，
 * 并封装在 Transform 对象中返回，由于 firstNotNullOfOrNull 这个方法只要有返回第一个非 null 的元素就会结束整个遍历过程。因此嵌套的递归都会返回这个
 * Transform 对象，表示找到了这两个成配对的扫描器，以及对齐后的信标集合。
 *
 * 在任意两个 scanner 之间都可以找出成配对的关系之后，solve 方法就可以来对原始这多个扫描器及其信标结果进行处理了。首先取 scanners
 * 中的第一个扫描器扫出来的信标作为 baseSector，并将该信标集合转换为可修改的 Set 类型。再新建两个对象 foundScanners 和 unmappedSectors，
 * 前者用来保存那些找到配对的扫描器，后者则保存除第一个扫描器之外的其他扫描器的信标集合，以便去和第一个信标集合 baseSector 逐个进行变换比对。
 * 正式处理时，只要尚未处理的信标集合还有未处理的内容（unmappedSectors 不为空），就取未配对集合 unmappedSectors 中第一个元素作为 thisSector，
 * 并将其和 baseSector 一同传入 findTransformIfIntersects 方法中，从而判断这两个信标集合之间是否有至少 12 个信标是共同的。若不是，则 transform
 * 返回结果为 null，并将这个取出的 thisSector 重新放回到 unmappedSectors 集合的末尾，并重新开始循环；若是，则将得到差异和对齐后的信标集合。
 * 由于基准扫描器的坐标为 (0,0,0)，因此该差异就是成配对的另一个扫描器相对于基准扫描器的坐标，从而通过 transform.scanner 字段存入找到的扫描器集合
 * foundScanners 中，并将对齐后的信标集合中的每一个信标都加入到 baseSector 中（每一个信标此时也都是与基准信标集合对齐完毕后的），另外由于
 * baseSector 是 Set 类型，因此重复的信标将自动去除，这个集合中最终将存储每一个对齐的，不同的信标。
 *
 * 当 solve 方法全部分析完毕的时候，baseSector 中存储的全部是对齐完毕后的信标，而 foundScanners 中存储的全部为基准扫描器及其相对于基准扫描器的其他扫描器坐标。
 * 因此统一封装在 Solution 对象中返回。第一个问题计算总共有多少个信标只需要计算 solution 对象中 beacons 集合中元素的个数即可。第二个问题则在返回的 scanners
 * 基础上用 pair() 方法构建一个集合中每一个元素都和其他所有元素之间的 pair，并借助 flatMap 方法将其这多个 Pair 的集合整合到一个集合中，透过 distanceTo
 * 计算出第二个问题中距离的最大值。
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