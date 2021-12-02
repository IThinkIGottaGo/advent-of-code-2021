package day2

import util.readInput
import util.splitBySpace
import util.stringsToPair

/**
 * --- 第 2 天：下潜！---
 *
 * 现在，你需要搞清楚怎么去驾驶这个东西了。
 *
 * 看起来潜艇可以接受一系列命令，比如前进 1，下降 2，或上升 3：
 *
 * - 前进(forward) X 会将水平位置增加 X 个单位。
 * - 下降(down) X 会**增加** X 个单位的深度。
 * - 上升(up) X 会**减少** X 个单位的深度。
 *
 *
 * 要注意，由于你在潜艇上面，上升和下降会影响你的**深度**，所以，它们的结果可能会跟你预期的相反。（即上升是深度降低而不是升高，下降是深度升高而不是降低）
 *
 * 潜艇现在好像已经有了一个行进计划（这便是你的谜题输入）。你需要搞清楚的是它具体的去向。比如说：
 *
 * ```
 * 前进 5
 * 下降 5
 * 前进 8
 * 上升 3
 * 下降 8
 * 前进 2
 * ```
 * 你的水平位置和深度都是从 0 开始。上面的步骤将会像下面这样修改你的水平位置和深度：
 *
 * - 前进 5 会增加 5 到你的水平位置上，水平总计为 5。
 * - 下降 5 会增加 5 到你的深度上，使得深度为 5。
 * - 前进 8 会增加 8 到你的水平位置上，水平总计为 13。
 * - 上升 3 会减少 3 到你的深度上，使得深度为 2。
 * - 下降 8 会增加 8 到你的深度上，使得深度为 10。
 * - 前进 2 会增加 2 到你的水平位置上，水平总计为 15。
 *
 * 按照上述的命令，你的水平位置将会为 15，深度为 10。（将它们相乘则结果是 **150**）
 *
 * 第一个问题：在按照计划路线来计算你的水平位置和深度。你**最终的水平位置，再乘以你最终的深度，结果是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val result = Result()
        input.asSequence()
            .map(String::splitBySpace)
            .map { Action.act(it.stringsToPair()) }
            .forEach { it.move(result) }
        return result.position * result.deep
    }

    val testInput = readInput("day02_test")
    check(part1(testInput) == 150)

    val input = readInput("day02")
    check(part1(input) == 1580000)
}

private data class Result(
    var position: Int = 0,
    var deep: Int = 0,
)

private sealed class Action {
    abstract fun move(r: Result)

    companion object {
        fun act(p: Pair<String, Int>): Action =
            when (p.first) {
                "forward" -> Forward(p.second)
                "up" -> Up(p.second)
                "down" -> Down(p.second)
                else -> error("unknown action!")
            }
    }
}

private class Forward(val i: Int) : Action() {
    override fun move(r: Result) {
        r.position += i
    }
}

private class Up(val i: Int) : Action() {
    override fun move(r: Result) {
        r.deep -= i
    }
}

private class Down(val i: Int) : Action() {
    override fun move(r: Result) {
        r.deep += i
    }
}
