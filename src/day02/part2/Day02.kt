package day02.part2

import util.readInput
import util.splitBySpace
import util.stringsToPair

/**
 * --- 第二部分 ---
 *
 * 根据你的计算，这个计划路线似乎根本没有任何意义。你找到了潜艇手册，并发现这个过程实际上稍微更复杂一些。
 *
 * 除了水平位置和深度，你还需要追踪第三个值，**目标(aim)**，它也是从 0 开始计算。这些命令的含义也意味着与你最初的想法完全不同。
 *
 * - 下降 X 会**增加** X 个单位到你的 aim 上。
 * - 上升 X 会**减少** X 个单位到你的 aim 上。
 * - 前进 X 会做下面这两件事：
 *      - 它会增加你的水平位置 X 个单位
 *      - 它会增加你的深度 aim **乘以** X 个单元。
 *
 * 请再次注意，由于你在潜艇上面，上升和下降可能会跟你预期的增减相反：”下降“指的是深度增加的方向。
 *
 * 现在，上面例子会做不同的事情：
 *
 * - 前进 5 会增加 5 到你的水平位置上，水平总计为 5。因为你的 aim 是 0，所以深度不变。
 * - 下降 5 会增加 5 到你的 aim 上，使得 aim 为 5。
 * - 前进 8 会增加 8 到你的水平位置上，水平总计为 13。因为你的 aim 是 5，所以深度增加了 8*5=40。
 * - 上升 3 会减少 3 到你的 aim 上，使得 aim 为 2。
 * - 下降 8 会增加 8 到你的 aim 上，使得 aim 为 10。
 * - 前进 2 会增加 2 到你的水平位置上，水平总计为 15。因为你的 aim 是 10，所以深度增加了 2*10=20，深度总计为 60。
 *
 * 在执行完这些新命令后，你的水平位置将会为 15，深度为 60。（将它们相乘则结果是 **900**）
 *
 * 第二个问题：使用上述新的命令解释规则，并按照计划路线来计算你的水平位置和深度。你**最终的水平位置，再乘以你最终的深度，结果是多少？**
 */
fun main() {
    // 第二个问题
    fun part2(input: List<String>): Int {
        val result = Result()
        input.asSequence()
            .map(String::splitBySpace)
            .map { Action.act(it.stringsToPair()) }
            .forEach { it.move(result) }
        return result.position * result.deep
    }

    val testInput = readInput("day02_test")
    check(part2(testInput) == 900)

    val input = readInput("day02")
    check(part2(input) == 1251263225)
}

private data class Result(
    var position: Int = 0,
    var deep: Int = 0,
    var aim: Int = 0,
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
        r.deep += r.aim * i
    }
}

private class Up(val i: Int) : Action() {
    override fun move(r: Result) {
        r.aim -= i
    }
}

private class Down(val i: Int) : Action() {
    override fun move(r: Result) {
        r.aim += i
    }
}