package day25

import util.readInput

/**
 * --- 第 25 天：海参 (Sea Cucumber) ---
 *
 * 就是这里：海沟的底部，最后一个雪橇钥匙可能会在的地方。你潜艇的实验性天线 **仍然不足以** 直接探测到钥匙，但它 **肯定** 就在这。你所需要做的就是 **搜索海底**
 * 并找到它们。
 *
 * 至少，如果可以的话，最好能在海底着陆；不幸的是，海底完全被两大群 [海参 (sea cucumbers)](https://en.wikipedia.org/wiki/Sea_cucumber) 所覆盖，并没有足够大的空间来容纳你的潜艇。
 *
 * 你猜测小精灵们此前肯定做过类似的事，因为你发现了一个手写的深海海洋生物学家的电话号码，贴在了潜艇驾驶舱的墙上。
 *
 * “海参？是的，它们或许正在寻找食物。但别担心，它们是很好预测的生物：它们以完美的直线移动，并且只在前方有空间的时候才向前移动。它们其实非常的有礼貌！”
 *
 * 你解释到你希望能够预测你的潜艇什么时候可以着陆。
 *
 * “噢，这很简单，它们最终会堆积起来并留出足够的空间 —— 等等，你说了潜艇吗？唯一有那么多海参的地方就是马里亚纳海沟的最深处 ——” 你挂断了电话。
 *
 * 这里有两群海参共享着同一片区域；一群总是 **向东** (>) 移动，而另一群总是 **向南** (v) 移动。每个位置至多只能容纳一只海参；其余的位置则是 **空闲的** (.)。
 * 潜艇很有帮助的生成了一副该场景的地图（这便是你的谜题输入）。比如：
 *
 * ```
 * v...>>.vv>
 * .vv>>.vv..
 * >>.>v>...v
 * >>v>>.>.v.
 * v>v.vv.v..
 * >.>>..v...
 * .vv..>.>v.
 * v.v..>>v.v
 * ....v..v.>
 * ```
 *
 * **每一步**，面朝东的海参都会尝试向前移动一个位置，然后面朝南的海参也尝试向前移动一个位置。当海参群向前移动时，群中的每只海参会首先同时的考虑它所面对的那个相邻位置是否有其他海参
 * （即使有另一群海参面对相同的位置），再然后每只面对着空闲位置的海参都会同时的移动到那个位置。
 *
 * 所以，假如有下面这个情况：
 *
 * ```
 * ...>>>>>...
 * ```
 *
 * 在经过一步后，只有最右侧的海参将能够移动：
 *
 * ```
 * ...>>>>.>..
 * ```
 *
 * 再经过一步后，有两个海参能够移动：
 *
 * ```
 * ...>>>.>.>.
 * ```
 *
 * 在单步移动的过程中，面朝东的海参群先行移动，然后再是面朝的海参群移动。所以，假如下面这个情况：
 *
 * ```
 * ..........
 * .>v....v..
 * .......>..
 * ..........
 * ```
 *
 * 在单步移动后，左侧的海参群中，只有面朝南的海参移动了（因为此时没有空间来让左侧面朝东的海参来移动），但两个右侧的海参都移动了（因为面朝东的海参从面朝南海参的路径中先移开了）：
 *
 * ```
 * ..........
 * .>........
 * ..v....v>.
 * ..........
 * ```
 *
 * 另外因为该区域 **水流十分湍急**，从地图右边缘离开的海参会出现在左侧边缘中，以及从地图下边缘离开的海参会出现在上边缘中。海参在移动前总是会检查是否它们的目的地是否是空闲的，
 * 即使这个目的地位于地图的另一边：
 *
 * ```
 * 初始状态：
 * ...>...
 * .......
 * ......>
 * v.....>
 * ......>
 * .......
 * ..vvv..
 *
 * 经过 1 步：
 * ..vv>..
 * .......
 * >......
 * v.....>
 * >......
 * .......
 * ....v..
 *
 * 经过 2 步：
 * ....v>.
 * ..vv...
 * .>.....
 * ......>
 * v>.....
 * .......
 * .......
 *
 * 经过 3 步：
 * ......>
 * ..v.v..
 * ..>v...
 * >......
 * ..>....
 * v......
 * .......
 *
 * 经过 4 步：
 * >......
 * ..v....
 * ..>.v..
 * .>.v...
 * ...>...
 * .......
 * v......
 * ```
 *
 * 为了能够找出一个安全的地方来让你的潜艇着陆，海参需要停止移动。再考虑上面第一个例子：
 *
 * ```
 * 初始状态：
 * v...>>.vv>
 * .vv>>.vv..
 * >>.>v>...v
 * >>v>>.>.v.
 * v>v.vv.v..
 * >.>>..v...
 * .vv..>.>v.
 * v.v..>>v.v
 * ....v..v.>
 *
 * 经过 1 步：
 * ....>.>v.>
 * v.v>.>v.v.
 * >v>>..>v..
 * >>v>v>.>.v
 * .>v.v...v.
 * v>>.>vvv..
 * ..v...>>..
 * vv...>>vv.
 * >.v.v..v.v
 *
 * 经过 2 步：
 * >.v.v>>..v
 * v.v.>>vv..
 * >v>.>.>.v.
 * >>v>v.>v>.
 * .>..v....v
 * .>v>>.v.v.
 * v....v>v>.
 * .vv..>>v..
 * v>.....vv.
 *
 * 经过 3 步：
 * v>v.v>.>v.
 * v...>>.v.v
 * >vv>.>v>..
 * >>v>v.>.v>
 * ..>....v..
 * .>.>v>v..v
 * ..v..v>vv>
 * v.v..>>v..
 * .v>....v..
 *
 * 经过 4 步：
 * v>..v.>>..
 * v.v.>.>.v.
 * >vv.>>.v>v
 * >>.>..v>.>
 * ..v>v...v.
 * ..>>.>vv..
 * >.v.vv>v.v
 * .....>>vv.
 * vvv>...v..
 *
 * 经过 5 步：
 * vv>...>v>.
 * v.v.v>.>v.
 * >.v.>.>.>v
 * >v>.>..v>>
 * ..v>v.v...
 * ..>.>>vvv.
 * .>...v>v..
 * ..v.v>>v.v
 * v.v.>...v.
 *
 * ...
 *
 * 经过 10 步：
 * ..>..>>vv.
 * v.....>>.v
 * ..v.v>>>v>
 * v>.>v.>>>.
 * ..v>v.vv.v
 * .v.>>>.v..
 * v.v..>v>..
 * ..v...>v.>
 * .vv..v>vv.
 *
 * ...
 *
 * 经过 20 步：
 * v>.....>>.
 * >vv>.....v
 * .>v>v.vv>>
 * v>>>v.>v.>
 * ....vv>v..
 * .v.>>>vvv.
 * ..v..>>vv.
 * v.v...>>.v
 * ..v.....v>
 *
 * ...
 *
 * 经过 30 步：
 * .vv.v..>>>
 * v>...v...>
 * >.v>.>vv.>
 * >v>.>.>v.>
 * .>..v.vv..
 * ..v>..>>v.
 * ....v>..>v
 * v.v...>vv>
 * v.v...>vvv
 *
 * ...
 *
 * 经过 40 步：
 * >>v>v..v..
 * ..>>v..vv.
 * ..>>>v.>.v
 * ..>>>>vvv>
 * v.....>...
 * v.v...>v>>
 * >vv.....v>
 * .>v...v.>v
 * vvv.v..v.>
 *
 * ...
 *
 * 经过 50 步：
 * ..>>v>vv.v
 * ..v.>>vv..
 * v.>>v>>v..
 * ..>>>>>vv.
 * vvv....>vv
 * ..v....>>>
 * v>.......>
 * .vv>....v>
 * .>v.vv.v..
 *
 * ...
 *
 * 经过 55 步：
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv...>..>
 * >vv.....>.
 * .>v.vv.v..
 *
 * 经过 56 步：
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv....>.>
 * >vv......>
 * .>v.vv.v..
 *
 * 经过 57 步：
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv.....>>
 * >vv......>
 * .>v.vv.v..
 *
 * 经过 58 步：
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv.....>>
 * >vv......>
 * .>v.vv.v..
 * ```
 *
 * 在这个例子中，海参经过 **58** 步后就完全停止了移动。
 *
 * 第一个问题：找出某个安全的位置来让你的潜艇着陆。**经过多少步第一次让所有的海参都不再移动？**
 *
 * --- 第二部分 ---
 *
 * 突然的，实验性天线的控制台亮了起来：
 *
 * ```
 * 侦测到雪橇钥匙！
 * ```
 *
 * 根据控制台所说，钥匙就位于 **潜艇的正下方**。你已经着陆在它的上方了！通过使用潜艇上搭载的机械手臂，你将雪橇钥匙移动到进气阀处。
 *
 * 现在，你只需要把它们及时的交给圣诞老人来拯救你的圣诞节就行了！你查看了你的时钟 - 现在正是圣诞节。但你不可能及时的回到地面。
 *
 * 就在你开始失去希望的时候，你注意到雪橇钥匙上有一个按钮： **远程启动 (remote start)** 。你可以从海底来启动雪橇！你只需要某种方式来 **增益钥匙的信号 (boost the signal)** ，
 * 以便让信号能够到达雪橇。好消息是潜艇上有实验性的天线！不过，你肯定需要有 [50 颗星星](#) 才能增益到那么远。
 *
 * 实验性天线的控制台再次亮了起来：
 *
 * ```
 * 检测到能量源。
 * 正在从设备“雪橇钥匙”中集成能量源...完毕。
 * 正在安装设备驱动...完毕。
 * 正在调整实验性天线...完毕。
 * 由于正匹配信号相位，增加强度：1 颗星
 * ```
 *
 * 只有拥有了 [49 颗星星](#) 才能继续。
 *
 * 如果你想的话，你现在可以 [再次远程启动雪橇](#) 了。
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val seaCucumbers = input.initSeaCucumbers()
        val row = input.size
        val column = input[0].length
        var steps = 1
        while (true) {
            if (seaCucumbers.move(row, column)) steps++ else break
        }
        return steps
    }

    // 第二个问题
    fun part2() {
        println(
            """
            你使用了全部的 50 颗星星来增益信号并远程启动了雪橇！
            现在，你只需要找到返回地面的方法就行了...
            
            ...你知道螃蟹潜艇来的时候是挂着彩灯来的吗？
            
            恭喜你！你已经完成了 Advent of Code 2021 的每一个谜题了！
            我希望你们能够从解决这些问题中获得乐趣，就像我在制作它们的时候一样。
            我很乐意聆听你自己的冒险故事；你可以通过网站上的联系方式和推特找到我。
            
            如果你还想在未来看到更多像这样的事件，请考虑赞助 Advent of Code 以及将它们分享给其他人。
            
            想了解未来的更多项目，你可以在推特上 follow 我。
        """.trimIndent()
        )
    }

    val testInput = readInput("day25_test")
    check(part1(testInput) == 58)

    val input = readInput("day25")
    check(part1(input) == 406)
    part2()
}

private fun List<String>.initSeaCucumbers(): MutableList<SeaCucumber> {
    val seaCucumber = mutableListOf<SeaCucumber>()
    forEach { line ->
        line.forEach { symbol ->
            seaCucumber.add(SeaCucumber(Herd.symbolOf(symbol)))
        }
    }
    return seaCucumber
}

private fun MutableList<SeaCucumber>.move(row: Int, column: Int): Boolean {
    var copy = toMutableList()
    var modified = moveEast(copy, column, false)
    copy = toMutableList()
    modified = moveSouth(copy, column, row, modified)
    return modified
}

private fun MutableList<SeaCucumber>.moveEast(
    copy: MutableList<SeaCucumber>,
    column: Int,
    modified: Boolean
): Boolean {
    var mayModified = modified
    copy.forEachIndexed { index, seaCucumber ->
        val herd = seaCucumber.herd
        if (herd == Herd.EAST) {
            var nextPosition = index + 1
            if (nextPosition % column == 0) nextPosition -= column
            if (copy[nextPosition].herd == Herd.EMPTY) {
                switchSeaCucumber(index, nextPosition)
                mayModified = true
            }
        }
    }
    return mayModified
}

private fun MutableList<SeaCucumber>.moveSouth(
    copy: MutableList<SeaCucumber>,
    column: Int,
    row: Int,
    modified: Boolean
): Boolean {
    var mayModified = modified
    copy.forEachIndexed { index, seaCucumber ->
        val herd = seaCucumber.herd
        if (herd == Herd.SOUTH) {
            var nextPosition = index + column
            if (nextPosition > lastIndex) nextPosition -= row * column
            if (copy[nextPosition].herd == Herd.EMPTY) {
                switchSeaCucumber(index, nextPosition)
                mayModified = true
            }
        }
    }
    return mayModified
}

private fun MutableList<SeaCucumber>.switchSeaCucumber(one: Int, other: Int) {
    val temp = this[one]
    this[one] = this[other]
    this[other] = temp
}

private class SeaCucumber(
    val herd: Herd
)

private enum class Herd {
    EAST, SOUTH, EMPTY;

    companion object {
        fun symbolOf(symbol: Char) = when (symbol) {
            '>' -> EAST
            'v' -> SOUTH
            '.' -> EMPTY
            else -> error("unknown symbol.")
        }
    }
}