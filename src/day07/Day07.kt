package day07

import util.readInputOneLine
import kotlin.math.absoluteValue

/**
 * --- 第 7 天：鲸鱼的背叛 ---
 *
 * 一只巨大的[鲸鱼](https://en.wikipedia.org/wiki/Sperm_whale)已经决定你的潜艇就是它的下一餐，而且它比你快得多。你无处可逃！
 *
 * 突然，有一群螃蟹（每只都在它们自己的小潜艇里 — 毕竟这里对它们来说也太深了）过来打算拯救你！它们似乎打算在海床上炸一个洞；
 * 传感器表明有一个**庞大的地下洞穴系统**就在他们的目标上方！
 *
 * 这些螃蟹潜艇在它们有足够的能量来炸开一个能让你潜艇通过的洞之前需要进行对齐。但看起来在鲸鱼抓住你之前它们好像来不及完成对齐！或许你可以帮帮它们？
 *
 * 有一个主要问题 —— 螃蟹潜艇只能够水平移动。
 *
 * 你很快搞出了一个**每只螃蟹的水平位置**的列表（这便是你的谜题输入）。螃蟹潜艇的燃料非常有限，所以你得找出一种方式既能够让它们的水平位置匹配，
 * 又尽可能耗费最少的燃料。
 *
 * 比如说，考虑下面这个水平位置：
 *
 * ```
 * 16,1,2,0,4,2,7,1,2,14
 * ```
 *
 * 这意味着有一只螃蟹的水平位置是 16，另一只水平位置是 1，然后以此类推。
 *
 * 每改变水平位置 1 步都会消耗掉该螃蟹 1 点燃料。你可以选择任何水平位置来将它们全部对齐，但其中消耗燃料最少的是将它们对齐到水平位置 2：
 *
 * - 从 16 移动到 2：14 燃料
 * - 从 1 移动到 2：1 燃料
 * - 从 2 移动到 2：0 燃料
 * - 从 0 移动到 2：2 燃料
 * - 从 4 移动到 2：2 燃料
 * - 从 2 移动到 2：0 燃料
 * - 从 7 移动到 2：5 燃料
 * - 从 1 移动到 2：1 燃料
 * - 从 2 移动到 2：0 燃料
 * - 从 14 移动到 2：12 燃料
 *
 * 上述总共花费了 **37** 燃料。这是上例消耗最少的结果；其他消耗更多的结果包括对齐到 1 位置（41 燃料），3 位置（39燃料）或 10 位置（71 燃料）。
 *
 * 第一个问题：确定让螃蟹只用最少的燃料就能对齐的水平位置。**对齐到这个位置它们需要花费多少的燃料？**
 *
 * --- 第二部分 ---
 *
 * 螃蟹们对你提出的解决方案似乎不感兴趣。或许你误解了螃蟹工程？
 *
 * 事实证明，螃蟹的潜艇引擎并不是以常速率来消耗燃料的。相反，在水平位置上每改变 1 步，就比上次多耗费 1 点燃料：第一步耗费 1 点，然后第二步耗费 2 点，
 * 第三步耗费 3 点，以此类推。
 *
 * 螃蟹的每次移动一次比一次更加昂贵。这也改变了它们能够对齐的最佳水平位置；按上面的例子来说，这次变成了 5：
 *
 * - 从 16 移动到 5：66 燃料
 * - 从 1 移动到 5：10 燃料
 * - 从 2 移动到 5：6 燃料
 * - 从 0 移动到 5：15 燃料
 * - 从 4 移动到 5：1 燃料
 * - 从 2 移动到 5：6 燃料
 * - 从 7 移动到 5：3 燃料
 * - 从 1 移动到 5：10 燃料
 * - 从 2 移动到 5：6 燃料
 * - 从 14 移动到 5：45 燃料
 *
 * 这次总共花费了 **168** 燃料。这是新的消耗最少燃料的结果；原本的对齐位置 (2) 现在得花费 206 燃料。
 *
 * 确定让螃蟹只用最少的燃料就能对齐的水平位置，以使得它们可以帮你逃出生天！**对齐到这个位置它们需要花费多少的燃料？**
 */
fun main() {
    // 第一个问题
    fun part1(input: String): Int {
        val sorted = input.split(',').sorted().map(String::toInt)
        var minFuel = Int.MAX_VALUE
        var prevFuel: Int = Int.MAX_VALUE
        (0..sorted.last()).forEach { i ->
            var fuel = 0
            sorted.forEach {
                fuel += (it - i).absoluteValue
            }
            if (minFuel in prevFuel until fuel) return@forEach
            prevFuel = fuel
            if (fuel < minFuel) minFuel = fuel
        }
        return minFuel
    }

    // 第二个问题
    fun part2(input: String): Int {
        val sorted = input.split(',').sorted().map(String::toInt)
        var minFuel = Int.MAX_VALUE
        var prevFuel: Int = Int.MAX_VALUE
        (0..sorted.last()).forEach { i ->
            var fuel = 0
            sorted.forEach {
                val path = (it - i).absoluteValue
                fuel += (path + 1) * path / 2
            }
            if (minFuel in prevFuel until fuel) return@forEach
            prevFuel = fuel
            if (fuel < minFuel) minFuel = fuel
        }
        return minFuel
    }

    val testInput = readInputOneLine("day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInputOneLine("day07")
    check(part1(input) == 335330)
    check(part2(input) == 92439766)
}