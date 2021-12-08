package day08

import util.partitionWithIndex
import util.readInput
import util.removeFirstIf

/**
 * --- 第 8 天：七段搜索 ---
 *
 * 在你刚进入安全的洞穴中时，鲸鱼就一头扎进了洞口，并把洞口弄塌了。传感器指示在更深的地方有另一个出口，所以你别无选择，只能继续前进。
 *
 * 在你的潜艇缓慢通过洞穴系统时，你注意到你的潜艇上显示四位数码的 [七段数显](https://en.wikipedia.org/wiki/Seven-segment_display) 发生了故障；
 * 它们肯定在逃跑的过程中损坏了。要是没有它们你会遇到很多麻烦，所以你最好弄清楚哪里出了问题。
 *
 * 七段数显的每个数字，都是由打开或关闭这七段数显中名为 a 到 g 的某一段表示的：
 *
 * ```
 *   0:      1:      2:      3:      4:
 *  aaaa    ....    aaaa    aaaa    ....
 * b    c  .    c  .    c  .    c  b    c
 * b    c  .    c  .    c  .    c  b    c
 *  ....    ....    dddd    dddd    dddd
 * e    f  .    f  e    .  .    f  .    f
 * e    f  .    f  e    .  .    f  .    f
 *  gggg    ....    gggg    gggg    ....
 *
 *   5:      6:      7:      8:      9:
 *  aaaa    aaaa    aaaa    aaaa    aaaa
 * b    .  b    .  .    c  b    c  b    c
 * b    .  b    .  .    c  b    c  b    c
 *  dddd    dddd    ....    dddd    dddd
 * .    f  e    f  .    f  e    f  .    f
 * .    f  e    f  .    f  e    f  .    f
 *  gggg    gggg    ....    gggg    gggg
 * ```
 *
 * 所以，要表示 1，只需要片段 c 和 f 打开；其他的则关闭。要表示 7，则只需要片段 a，c，f 打开。
 *
 * 问题是，在每个数显上控制片段的信号都混淆了。潜艇仍然试图通过在信号线 a 到 g 上产生输出来显示数字，但现在这些信号线是**随机**连接到片段上的。
 * 更糟的是，对每个四位数显来说，其线/段的连接都被分开混合了！（不过，所有数字都在某个使用**相同连接的数显之中**。）
 *
 * 所以，或许你已经知道了即使只有信号线 b 和 g 是打开的，也不意味着 b 和 g 的**片段**就被打开了：只使用两个片段就能表示数位的是 1，所以它必然意味着 c 和 f 片段应该被打开。
 * 尽管有了这些信息，但你还是无法分辨哪条线 (b/g) 连到哪个片段 (c/f) 上。为此，你还需要收集更多的信息。
 *
 * 对于每个数显，你看着这些信号变化了一会儿，并记录了你所看到的**所有十个各不相同的信号模式**，然后写下一个**四位数的输出值**（这便是你的谜题输入）。
 * 使用这些信号模式，你应该就能够计算出哪个模式对应哪个数字。
 *
 * 比如说，下面是你笔记中可能看到的一条记录：
 *
 * ```
 * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
 * ```
 *
 * 每条记录都由十个**各不相同的信号模式**，一个 | 分隔符，以及最终**四位数输出值**组成。在同一条记录中，都使用了相同的线/段连接（但你并不知道连接的实际是什么）。
 * 这些各不相同的信号模式对应于潜艇试图使用当前的线/段连接来呈现十种不同数字的方式。因为 7 是唯一使用了三段来表示的数字，在上面的例子中 dab 就意味着表示 7，
 * 信号段 d，a，b 则被打开。因为 4 是唯一使用了四段来表示的数字，则 eafb 则意味着表示 4，信号段 e，a，f 和 b 则被打开。
 *
 * 使用这些信息，你就应该能够计算出对应于十个数字中的每一个的信号线组合了。然后，你就能够解码四位数输出值了。不幸的是，在上面的例子中，所有在输出值中的数字
 * (cdfeb fcadb cdfeb cdbaf) 都是五段组成的，且更难推断。
 *
 * 现在，**先专注在容易的数字上**。考虑下面这个更大的例子：
 *
 * ```
 * be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | *fdgacbe* cefdb cefbgd *gcbe*
 * edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb *cgb* *dgebacf* *gc*
 * fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | *cg* *cg* fdcagb *cbg*
 * fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec *cb*
 * aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | *gecf* *egdcabf* *bgf* bfgea
 * fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | *gebdcfa* *ecba* *ca* *fadegcb*
 * dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | *cefg* dcbef *fcge* *gbcadfe*
 * bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | *ed* bcgafe cdgba cbgef
 * egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | *gbdfcae* *bgc* *cg* *cgb*
 * gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | *fgae* cfgab *fg* bagce
 * ```
 *
 * 因为数字 1，4，7 和 8 都各自是独一无二的片段数，所以你应该能够分辨出哪些信号组合对应于这些数字。**只计数这些输出值中的数字**（即每行 | 符号之后的部分），
 * 在上面的例子中，共有 **26** 个数字使用了独一无二的片段数（上面用 * 号包围的部分）。
 *
 * 第一个问题：**在输出值中，数字 1，4，7 或 8 总共出现了多少次？**
 *
 * --- 第二部分 ---
 *
 * 通过一点推断，你现在应该能够确定剩下的数字。再看看前面的第一个例子:
 *
 * ```
 * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
 * ```
 *
 * 经过仔细的分析，信号线和片段之间的映射只有在下面这种配置中才有意义:
 *
 * ```
 *  dddd
 * e    a
 * e    a
 *  ffff
 * g    b
 * g    b
 *  cccc
 * ```
 *
 * 所以，这些各自不同的信号模式将对应为下面这些数字：
 *
 * ```
 * - acedgfb: 8
 * - cdfbe: 5
 * - gcdfa: 2
 * - fbcad: 3
 * - dab: 7
 * - cefabd: 9
 * - cdfgeb: 6
 * - eafb: 4
 * - cagedb: 0
 * - ab: 1
 * ```
 *
 * 于是，四位数组成的输出值现在可以被解码了：
 *
 * - cdfeb: **5**
 * - fcadb: **3**
 * - cdfeb: **5**
 * - cdbaf: **3**
 *
 * 因此，该条目的输出值即为 **5353**。
 *
 * 第二个更大例子中的每个条目都按上面相同的过程，就可以确定每个条目的输出值了：
 *
 * ```
 * fdgacbe cefdb cefbgd gcbe: 8394
 * fcgedb cgb dgebacf gc: 9781
 * cg cg fdcagb cbg: 1197
 * efabcd cedba gadfec cb: 9361
 * gecf egdcabf bgf bfgea: 4873
 * gebdcfa ecba ca fadegcb: 8418
 * cefg dcbef fcge gbcadfe: 4548
 * ed bcgafe cdgba cbgef: 1625
 * gbdfcae bgc cg cgb: 8717
 * fgae cfgab fg bagce: 4315
 * ```
 *
 * 将这个更大例子中产生的全部输出值加和起来将得到 **61229**。
 *
 * 第二个问题：对于每个条目，确定其所有的线/段连接，并解码四位数的输出值。**如果你将所有这些输出值都加起来你会得到多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val lengths = listOf(2, 3, 4, 7)
        var count = 0
        input.asSequence()
            .map { it.split('|')[1] }
            .map { it.trim().split("""\s+""".toRegex()) }
            .forEach { output ->
                if (output.isNotEmpty()) {
                    count += output.filter { it.length in lengths }.size
                }
            }
        return count
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val (signals, outputs) = input.asSequence()
            .map { it.split('|') }
            .flatMap { it }
            .partitionWithIndex { i, _ -> i % 2 == 0 }
        var sum = 0
        (signals.indices).forEach { i ->
            val sigs = split(signals[i])
            val outs = split(outputs[i])
            val one = sigs.removeFirstIf { it.length == 2 }
            val four = sigs.removeFirstIf { it.length == 4 }
            val seven = sigs.removeFirstIf { it.length == 3 }
            val eight = sigs.removeFirstIf { it.length == 7 }
            val three = sigs.removeFirstIf { it.length == 5 && it.containLetters(one) }
            val nine = sigs.removeFirstIf { it.containSameLetters(four union three) }
            val five = sigs.removeFirstIf { it.length == 5 && (nine subtract it).length == 1 }
            val two = sigs.removeFirstIf { it.length == 5 }
            val six = sigs.removeFirstIf { (it union one).containSameLetters(eight) }
            val zero = sigs.last()
            val digits = listOf(zero, one, two, three, four, five, six, seven, eight, nine)
            var digitStr = ""
            outs.forEach out@{ out ->
                digits.forEachIndexed { index, digit ->
                    if (digit.length == out.length && out.containSameLetters(digit)) {
                        digitStr += index.toString()
                        return@out
                    }
                }
            }
            sum += digitStr.toInt()
        }
        return sum
    }

    val testInput = readInput("day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("day08")
    check(part1(input) == 344)
    check(part2(input) == 1048410)
}

private fun split(s: String): MutableList<String> = s.trim().split("""\s+""".toRegex()).toMutableList()

private fun String.containLetters(other: String) = this.toSet().containsAll(other.toSet())

private fun String.containSameLetters(other: String) = this.toSet() == other.toSet()

private infix fun String.union(other: String): String = (this.toSet() union other.toSet()).joinToString("")

private infix fun String.subtract(other: String): String = (this.toSet() subtract other.toSet()).joinToString("")