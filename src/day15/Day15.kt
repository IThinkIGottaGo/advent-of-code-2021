package day15

import util.readInput

/**
 * --- 第 15 天：石鳖(Chiton) ---
 *
 * 你已经快抵达洞穴的出口了，洞穴的岩壁也变得越来越靠近。尽管你的潜艇只能几乎刚刚好的容纳其中，但主要的问题在于岩壁上覆盖满了 [石鳖](https://en.wikipedia.org/wiki/Chiton)，
 * 并且最好不要撞到它们中的任何一个。
 *
 * 洞穴很大，但它的岩顶很低，这限制了你的移动只能是二维的。洞穴形状看起来就像正方形；对石鳖的密度进行了快速扫描后，你得到了一个涵盖整个洞穴 **风险等级(risk level)** 的映射（这便是你的谜题输入）。
 * 比如：
 *
 * ```
 * 1163751742
 * 1381373672
 * 2136511328
 * 3694931569
 * 7463417111
 * 1319128137
 * 1359912421
 * 3125421639
 * 1293138521
 * 2311944581
 * ```
 *
 * 你的起点是从最左上角开始，目的地是最右下角，并且你不可以斜向移动。每个位置上的数字都代表了该位置的 **风险等级**; 为了能够确定整个路径上的总体风险，只在你 **进入(enter)**
 * 到某个位置时才加上该位置的风险（所以不计算你开始位置的风险，因为起始位置不存在“进入”的问题，除非你进入到了这个位置；离开这个位置也不会增加你的总体风险）。
 *
 * 你的目标只找出一条有着 **最低总风险(lowest total risk)** 的路径。在上面这个例子中，最低总风险的路径按如下加粗表示：
 *
 * **1**163751742
 *
 * **1**381373672
 *
 * **2136511**328
 *
 * 369493**15**69
 *
 * 7463417**1**11
 *
 * 1319128**13**7
 *
 * 13599124**2**1
 *
 * 31254216**3**9
 *
 * 12931385**21**
 *
 * 231194458**1**
 *
 * 这个路径的总风险是 **40** （注意起始位置不存在进入的问题，所以它的风险没有计入其中）。
 *
 * 第一个问题：**从最左上方到最右下方的任意路径中，到达时总和风险最低可以是多少？**
 *
 * --- 第二部分 ---
 *
 * 现在你知道如何在洞穴中找出低风险的路径了，你可以试着找到出路。
 *
 * 整个洞穴在 **两个维度上都比你想的要大出 5 倍**；你原本扫描的区域只是整个 5x5 地图中的一个块而已。你原始的地图需要向右和向下平铺重复；而每次向右或向下重复时，
 * 都比其直接上方或作责的这一块塔的风险等级 **高出 1 级**。但是，若风险等级超过 9 则又从 1 开始计算。所以，如果你的原始地图某个位置的风险等级是从 8 开始的，
 * 那么在总共 25 个块上相同位置上的风险等级则如下所示：
 *
 * ```
 * 8 9 1 2 3
 * 9 1 2 3 4
 * 1 2 3 4 5
 * 2 3 4 5 6
 * 3 4 5 6 7
 * ```
 *
 * 上面的每一位数字都是根据位于左上角值为 8 的这个示例位置而产生的。因为完整的地图实际上在两个方向上都大五倍，因此该位置出现了总共 25 次，在上面的每一块就是一次，
 * 并带有其风险等级值。
 *
 * 这里是上面第一个例子完整的放大五倍后的版本，原本的地图部分则在左上角加粗显示：
 *
 * **1163751742**2274862853338597396444961841755517295286
 *
 * **1381373672**2492484783351359589446246169155735727126
 *
 * **2136511328**3247622439435873354154698446526571955763
 *
 * **3694931569**4715142671582625378269373648937148475914
 *
 * **7463417111**8574528222968563933317967414442817852555
 *
 * **1319128137**2421239248353234135946434524615754563572
 *
 * **1359912421**2461123532357223464346833457545794456865
 *
 * **3125421639**4236532741534764385264587549637569865174
 *
 * **1293138521**2314249632342535174345364628545647573965
 *
 * **2311944581**3422155692453326671356443778246755488935
 *
 * 22748628533385973964449618417555172952866628316397
 *
 * 24924847833513595894462461691557357271266846838237
 *
 * 32476224394358733541546984465265719557637682166874
 *
 * 47151426715826253782693736489371484759148259586125
 *
 * 85745282229685639333179674144428178525553928963666
 *
 * 24212392483532341359464345246157545635726865674683
 *
 * (... to long, ignore here)
 *
 * 在配备了完整地图后，你现在可以找出一条从最左上角到最右下角的最低风险路径了：
 *
 * (... to long, ignore here)
 *
 * 这个路径的总风险是 **315** （注意起始位置不存在进入的问题，所以它的风险没有计入其中）。
 *
 * 第二个问题：通过使用完整地图，**从最左上方到最右下方的任意路径中，到达时总和风险最低可以是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val risks = input.joinToString("").map(Char::digitToInt)
        val column = input[0].length
        val unsettled = mutableMapOf<Int, Cell>()
        val settled = mutableMapOf<Int, Cell>()
        unsettled[0] = Cell(0, 0)
        while (unsettled.isNotEmpty()) {
            val cell = unsettled.minByOrNull { it.value.total }!!
            val pos = cell.key
            val c = cell.value
            val total = c.total
            settled[pos] = c
            unsettled.remove(pos)
            val leftCell =
                if (pos % column != 0 && pos - 1 !in settled) Pair(pos - 1, Cell(risks[pos - 1] + total, pos)) else null
            val rightCell = if ((pos + 1) % column != 0 && pos + 1 !in settled) Pair(
                pos + 1,
                Cell(risks[pos + 1] + total, pos)
            ) else null
            val upCell = if (pos - column >= 0 && pos - column !in settled) Pair(
                pos - column,
                Cell(risks[pos - column] + total, pos)
            ) else null
            val downCell = if (pos + column <= risks.lastIndex && pos + column !in settled) Pair(
                pos + column,
                Cell(risks[pos + column] + total, pos)
            ) else null
            listOf(leftCell, rightCell, upCell, downCell).forEach { newCell ->
                if (newCell != null) {
                    if (newCell.first !in unsettled) unsettled[newCell.first] = newCell.second
                    else {
                        val oldCell = unsettled[newCell.first]!!
                        val newTotal = newCell.second.total
                        val newPrev = newCell.second.prev
                        if (newTotal < oldCell.total) {
                            oldCell.total = newTotal
                            oldCell.prev = newPrev
                        }
                    }
                }
            }
        }
        return settled[risks.lastIndex]!!.total
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val repeatedList = mutableListOf<String>()
        val tempList = mutableListOf<String>()
        repeat(input.size) { i ->
            tempList.add(input[i])
            repeat(4) { j ->
                tempList.add(tempList[j].map(Char::digitToInt).map { if (it + 1 > 9) 1 else it + 1 }.joinToString(""))
            }
            repeatedList.add(tempList.joinToString(""))
            tempList.clear()
        }
        tempList.addAll(repeatedList)
        repeat(4) {
            repeat(tempList.size) { size ->
                tempList[size] =
                    tempList[size].map(Char::digitToInt).map { if (it + 1 > 9) 1 else it + 1 }.joinToString("")
            }
            repeatedList.addAll(tempList)
        }
        return part1(repeatedList)
    }

    val testInput = readInput("day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("day15")
    check(part1(input) == 562)
    check(part2(input) == 2874)
}

private data class Cell(
    var total: Int,
    var prev: Int
)