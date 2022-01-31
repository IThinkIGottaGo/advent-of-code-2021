package day14

import util.readInput

/**
 * --- 第 14 天：高聚物延展(Extended Polymerization) ---
 *
 * 在如此深的位置，难以置信的压力已经开始给你的潜艇造成负担。但你的潜艇配备了 [高分子聚合物 (polymerization)](https://en.wikipedia.org/wiki/Polymerization) 设备可以用来产生合适的材料来加强你的潜艇，
 * 而附近有火山活动的洞穴应该能给你提供足够数量的必要输入元素。
 *
 * 潜艇的手册提供了找到最佳聚合物配方的说明；特别的，它还提供了一个 **高聚物模板(polymer template)** 以及一系列 **成对添加(pair insertion)** 的规则（这便是你的谜题输入）。
 * 你只需要找出经过若干次成对添加后，会产生什么样的高聚物即可。
 *
 * 比如：
 *
 * ```
 * NNCB
 *
 * CH -> B
 * HH -> N
 * CB -> H
 * NH -> C
 * HB -> C
 * HC -> B
 * HN -> C
 * NN -> C
 * BH -> H
 * NC -> B
 * NB -> B
 * BN -> B
 * BB -> N
 * BC -> B
 * CC -> N
 * CN -> C
 * ```
 *
 * 第一行就是 **高聚物模板** - 这便是处理过程的开始点。
 *
 * 紧跟着的部分就是 **成对添加** 的规则。像 `AB -> C` 这样的规则表示当元素 `A` 和 `B` 直接相邻的时候，元素 `C` 就会插在它们中间。这些插入全部都是同时发生的。
 *
 * 所以，从高聚物模板 `NNCB` 开始，第一步是先同时考虑所有下面这 3 对：
 *
 * - 第一对 (`NN`) 匹配规则 `NN -> C`，所以元素 **C** 被插入在第一个 `N` 和 第二个 `N` 之间。
 * - 第二对 (`NC`) 匹配规则 `NC -> B`，所以元素 **B** 被插入在 `N` 和 `C` 之间。
 * - 第三对 (`CB`) 匹配规则 `CB -> H`，所以元素 **H** 被插入在 `C` 和 `B` 之间。
 *
 * 注意这些元素是如何叠加的：一对中的第二个元素成为了下一对的第一个元素。还有，由于所有的成对都是同时考虑的，因此被插入的元素在下一轮开始前都不会被考虑进成对的一部分中。
 *
 * 在这个过程的第一步结束后，高聚物现在变成了 N**C**N**B**C**H**B 。
 *
 * 下面就是按照上述规则执行若干步之后产生的结果：
 *
 * ```
 * 模板:      NNCB
 * 第 1 步后: NCNBCHB
 * 第 2 步后: NBCCNBBBCBHCB
 * 第 3 步后: NBBBCNCCNBBNBNBBCHBHHBCHB
 * 第 4 步后: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
 * ```
 *
 * 高聚物增长的非常快。在第 5 步后，它的长度达到了 97；在第 10 步后，它的长度达到了 3073。且在第 10 步后，`B` 出现了 1749 次，`C` 出现了 298 次，
 * `H` 出现了 161 次，`N` 出现了 865 次；取数量最常见元素的个数 (`B`, 1749) 减去数量最少见元素的个数 (`H`, 161) 则会产生 1749 - 161 = **1588** 。
 *
 * 第一个问题：将成对添加的规则作用 10 次到高聚物模板上，并找出结果中出现最常见和最少见的元素。**如果用最常见元素的数量减去最少见元素的数量你能得到什么？**
 *
 * --- 第二部分 ---
 *
 * 产生的聚合物的强度还不足以加固潜艇。你还需要运行更多步成对添加的流程。总计需要运行 **40步** 来完成。
 *
 * 在上面的例子中，最常见的元素 `B` (此时出现 `2192039569602` 次)和最少见的元素 `H` (此时出现 `3849876073` 次)；相减后得到的结果为 **2188189693529**。
 *
 * 第二个问题：将成对添加的规则作用 **40** 次到高聚物模板上，并找出结果中出现最常见和最少见的元素。 **如果用最常见元素的数量减去最少见元素的数量你能得到什么？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Long {
        val rulesMap = mutableMapOf<String, String>()
        val resultMap = mutableMapOf<String, Long>()
        rulesMap.initRules(input.drop(2))
        resultMap.initResults(input[0], rulesMap)
        return resultMap.polymerize(10, rulesMap)
    }

    // 第二个问题
    fun part2(input: List<String>): Long {
        val rulesMap = mutableMapOf<String, String>()
        val resultMap = mutableMapOf<String, Long>()
        rulesMap.initRules(input.drop(2))
        resultMap.initResults(input[0], rulesMap)
        return resultMap.polymerize(40, rulesMap)
    }

    val testInput = readInput("day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("day14")
    check(part1(input) == 3058L)
    check(part2(input) == 3447389044530L)
}

private fun MutableMap<String, String>.initRules(strRules: List<String>) {
    strRules.forEach {
        val (pair, result) = it.split(Regex("""\s+->\s+"""))
        put(pair, result)
    }
}

private fun MutableMap<String, Long>.initResults(template: String, rules: Map<String, String>) {
    rules.keys.forEach {
        put(it, 0L)
    }
    template.windowed(2).forEach {
        val num = this[it] ?: error("rules should not nonexistent")
        this[it] = num + 1
    }
}

private fun MutableMap<String, Long>.polymerize(steps: Int, rules: Map<String, String>): Long {
    val resultMap = this
    repeat(steps) {
        val tempMap = resultMap.toMutableMap()
        tempMap.filter { it.value != 0L }.forEach { (key, value) ->
            val base = rules[key]!!
            resultMap[key[0] + base] = resultMap[key[0] + base]!! + value
            resultMap[base + key[1]] = resultMap[base + key[1]]!! + value
            resultMap[key] = resultMap[key]!! - value
        }
    }
    val charMap = mutableMapOf<Char, Long>()
    resultMap.forEach { (key, value) ->
        key.forEach { char ->
            if (charMap.containsKey(char)) {
                charMap[char] = charMap[char]!! + value
            } else {
                charMap[char] = value
            }
        }
    }
    charMap.forEach { (key, value) ->
        if (value % 2L == 0L) charMap[key] = value / 2
        else charMap[key] = value / 2 + 1
    }
    return charMap.maxOf { it.value } - charMap.minOf { it.value }
}