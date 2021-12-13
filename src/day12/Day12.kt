package day12

import util.readInput
import util.removeFromLastUntil

/**
 * --- 第 12 天：穿过(Passage)路径 ---
 *
 * 由于你潜艇的地下子系统未能处在最佳状态（subterranean subsystems subsisting suboptimally），唯一能够让你尽快离开这个洞穴的方法就是自己找条路出来。
 * 不仅仅是找出 **一条** 路径 —— 唯一能让你知道是否找到了 **最佳** 路径的方式就是把 **所有的** 路径都找出来。
 *
 * 幸运的是，传感器绝大部分都还在工作，所以你可以绘制一幅有关剩余洞穴的粗略地图（这便是你的谜题输入），比如说：
 *
 * ```
 * start-A
 * start-b
 * A-c
 * A-b
 * b-d
 * A-end
 * b-end
 * ```
 *
 * 上面列出了所有的洞穴是如何连接在一起的。你从名为 start 的洞穴开始，然后你的目的地则是名为 end 的洞穴。各个类似 b-d 这样的条目意味着洞穴 b 和洞穴 d 是互相连接的 -
 * 也就是说，你可以在它们两个之间移动。
 *
 * 所以，上面的洞穴系统大概看起来就像下面这样：
 *
 * ```
 *     start
 *     /   \
 * c--A-----b--d
 *     \   /
 *      end
 * ```
 *
 * 你的目标就是找出从 start 开始到 end 结束之间有多少条不同的 **路径(paths)** 存在，并且访问小洞穴时不能超过一次。在这里有两种类型的洞穴： **大(big)** 洞穴（以大写字母标注的，如 A）
 * 和 **小(small)** 洞穴（以小写字母标注的，如 b）。不止一次的去访问小洞穴是浪费时间的，但大洞穴则足够的大，意味着多次访问它们可能是有意义的。因此，所有你要查找的路径都应该是
 * **至多访问小洞穴一次** ，且可以 **访问大洞穴任意次** 的路径。
 *
 * 根据给定的规则，有 **10** 条路径可以通过上面这个示例的洞穴系统：
 *
 * ```
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,end
 * ```
 *
 * （上面列表中的每一行都对应了一条路径；路径中访问过的洞穴则按照访问的顺序列在其中并用逗号分隔开。）
 *
 * 注意，在上面这个洞穴系统中，洞穴 d 没有被任何路径访问过：要是去访问的话，则洞穴 b 将至少会被访问两次（一次是前往洞穴 d，然后一次是从洞穴 d 中返回），又因为 b 是小洞穴，这是不允许的。
 *
 * 下面是一个稍微大点的例子：
 *
 * ```
 * dc-end
 * HN-start
 * start-kj
 * dc-start
 * dc-HN
 * LN-dc
 * HN-end
 * kj-sa
 * kj-HN
 * kj-dc
 * ```
 *
 * 下面列出了能够通过它的 19 条路径：
 *
 * ```
 * start,HN,dc,HN,end
 * start,HN,dc,HN,kj,HN,end
 * start,HN,dc,end
 * start,HN,dc,kj,HN,end
 * start,HN,end
 * start,HN,kj,HN,dc,HN,end
 * start,HN,kj,HN,dc,end
 * start,HN,kj,HN,end
 * start,HN,kj,dc,HN,end
 * start,HN,kj,dc,end
 * start,dc,HN,end
 * start,dc,HN,kj,HN,end
 * start,dc,end
 * start,dc,kj,HN,end
 * start,kj,HN,dc,HN,end
 * start,kj,HN,dc,end
 * start,kj,HN,end
 * start,kj,dc,HN,end
 * start,kj,dc,end
 * ```
 *
 * 最后，下面这个更大的例子能够有 226 条路径通过它：
 *
 * ```
 * fs-end
 * he-DX
 * fs-he
 * start-DX
 * pj-DX
 * end-zg
 * zg-sl
 * zg-pj
 * pj-he
 * RW-he
 * fs-DX
 * pj-RW
 * zg-RW
 * start-pj
 * he-WI
 * zg-he
 * pj-fs
 * start-RW
 * ```
 *
 * 第一个问题：**有多少条路径能够通过这个洞穴系统？（小洞穴至多只能访问一次）**
 *
 * --- 第二部分 ---
 *
 * 在浏览过可用的路径后，你意识到或许你也有时间去访问单个的小洞穴两次。确切的说，大洞穴可以被访问任意数量次，然后你可以选某一个小洞穴，使其至多能被访问 **两次** ，
 * 但剩余的小洞穴依然只能至多访问一次。但是，名为 start 和 end 的洞穴它们每个 **精确的只能访问一次** ： 一旦你离开了 start 洞穴，你就不能再到其中了，
 * 并且当你抵达了 end 洞穴，路径就必须立刻结束。
 *
 * 现在，第一个例子中就有 36 条可能的路径了：
 *
 * ```
 * start,A,b,A,b,A,c,A,end
 * start,A,b,A,b,A,end
 * start,A,b,A,b,end
 * start,A,b,A,c,A,b,A,end
 * start,A,b,A,c,A,b,end
 * start,A,b,A,c,A,c,A,end
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,d,b,A,c,A,end
 * start,A,b,d,b,A,end
 * start,A,b,d,b,end
 * start,A,b,end
 * start,A,c,A,b,A,b,A,end
 * start,A,c,A,b,A,b,end
 * start,A,c,A,b,A,c,A,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,d,b,A,end
 * start,A,c,A,b,d,b,end
 * start,A,c,A,b,end
 * start,A,c,A,c,A,b,A,end
 * start,A,c,A,c,A,b,end
 * start,A,c,A,c,A,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,b,A,c,A,end
 * start,b,A,b,A,end
 * start,b,A,b,end
 * start,b,A,c,A,b,A,end
 * start,b,A,c,A,b,end
 * start,b,A,c,A,c,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,d,b,A,c,A,end
 * start,b,d,b,A,end
 * start,b,d,b,end
 * start,b,end
 * ```
 *
 * 前面稍微大一点的例子现在有 103 条路径可以通过，而后面更大的例子则现在有 3509 条路径可以通过它了。
 *
 * 第二个问题：根据这些给定的新规则，**有多少条路径能够通过这个洞穴系统？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val start = input.buildCaveAndConnected()
            .first { it.type == CaveType.START }
        return recursiveWalkPath(start, mutableListOf(), mutableListOf(), null).size
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val caves = input.buildCaveAndConnected()
        val start = caves.first { it.type == CaveType.START }
        val smallCaves = caves.filter { it.type == CaveType.SMALL }.toMutableList()
        val path = mutableSetOf<String>()
        smallCaves.forEach { cave ->
            recursiveWalkPath(start, mutableListOf(), mutableListOf(), cave).forEach {
                path.add(it.joinToString(","))
            }
        }
        return path.size
    }

    val testInput = readInput("day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("day12")
    check(part1(input) == 4411)
    check(part2(input) == 136767)
}

private fun List<String>.buildCaveAndConnected(): MutableList<Cave> {
    val caves = mutableListOf<Cave>()
    for (s in this) {
        val (t1, t2) = s.split("-").map(::Cave)
        if (!caves.contains(t1)) caves.add(t1)
        if (!caves.contains(t2)) caves.add(t2)
        val c1 = caves[caves.indexOf(t1)]
        val c2 = caves[caves.indexOf(t2)]
        if (!c1.connected.contains(c2)) c1.connected.add(c2)
        if (!c2.connected.contains(c1)) c2.connected.add(c1)
    }
    return caves
}

private fun recursiveWalkPath(
    c: Cave,
    passed: MutableList<Cave>,
    result: MutableList<List<Cave>>,
    twiceSmallCave: Cave?
): MutableList<List<Cave>> {
    if (c.type == CaveType.START) {
        if (!passed.contains(c)) {
            passed.add(c)
            c.connected.forEach { sc ->
                recursiveWalkPath(sc, passed, result, twiceSmallCave)
                passed.removeFromLastUntil { it == c }
            }
        }
        return result
    }
    if (c.type == CaveType.END) {
        passed.add(c)
        result.add(passed.toList())
        return result
    }
    if (c.type == CaveType.BIG) {
        passed.add(c)
        c.connected.forEach { bc ->
            recursiveWalkPath(bc, passed, result, twiceSmallCave)
            passed.removeFromLastUntil { it == c }
        }
    }
    if (c.type == CaveType.SMALL) {
        val smallCount = passed.count { it == c }
        if (twiceSmallCave != c) {
            if (smallCount > 0) return result
        } else {
            if (smallCount > 1) return result
        }
        passed.add(c)
        c.connected.forEach { sc ->
            recursiveWalkPath(sc, passed, result, twiceSmallCave)
            passed.removeFromLastUntil { it == c }
        }
    }
    return result
}

private data class Cave(val name: String) {
    val type: CaveType = when {
        name == "start" -> CaveType.START
        name == "end" -> CaveType.END
        name[0].isUpperCase() -> CaveType.BIG
        else -> CaveType.SMALL
    }
    val connected: MutableList<Cave> = mutableListOf()
}

private enum class CaveType {
    SMALL, BIG, START, END
}