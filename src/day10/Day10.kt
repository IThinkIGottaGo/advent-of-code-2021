package day10

import util.readInput

/**
 * --- 第 10 天：语法得分 ---
 *
 * 你要求潜艇确定出脱离深海洞穴地最佳路线，但潜艇只回复：
 *
 * ```
 * 导航子系统在以下行发现了语法错误：全部
 * ```
 *
 * **全部？！** 看起来损伤比你想象的还严重。你打开了导航子系统的一份拷贝（这便是你的谜题输入）。
 *
 * 导航子系统的语法是由一系列 **块(chunk)** 构成的行组成的。每一行中都有一个或多个块，而块中又可以包含 0 个或多个其他的块。相邻的块之间没有使用分隔符来分隔；
 * 如果某个块结束了，紧跟着就是下一个块（假如有的话）。每个块的 **开(open)** 和 **闭(close)** 都必须使用下面这四对合法字符匹配对中的一对：
 *
 * - 如果某个块的开字符是 `(`，则它的闭字符必须是 `)`。
 * - 如果某个块的开字符是 `[`，则它的闭字符必须是 `]`。
 * - 如果某个块的开字符是 `{`，则它的闭字符必须是 `}`。
 * - 如果某个块的开字符是 `<`，则它的闭字符必须是 `>`。
 *
 * 所以，`()` 是一个不包含任何其他块的合法的块，`[]` 也一样。更复杂但依然合法的块还包括 `([])`，`{()()()}`，`<([{}])>`，`[<>({}){}[([])<>]]`，
 * `甚至是 (((((((((())))))))))`。
 *
 * 会有某些行是 **不完整的(incomplete)** ，但其他的则是 **损坏的(corrupted)**。所以要先找到损坏的行，并弃置(discard)该行。
 *
 * 损坏的行指的是，其中某个块**使用了错误的闭字符** —— 也就是说，其开字符和闭字符不能构成上面列出的四对合法配对中任何一对。
 *
 * 损坏的块的例子包括 `(]`，`{()()()>`，`(((()))}` 以及 `<([]){()}[{}])`。这样的块可能出现在某行的任意位置，并且它的出现意味着整个一行都要被弃置。
 *
 * 比如，考虑下面这个导航子系统的例子：
 *
 * ```
 * [({(<(())[]>[[{[]{<()<>>
 * [(()[<>])]({[<{<<[]>>(
 * {([(<{}[<>[]}>{[]{[(<()>
 * (((({<>}<{<{<>}{[]{[]{}
 * [[<[([]))<([[{}[[()]]]
 * [{[{({}]{}}([{[{{{}}([]
 * {<[[]]>}<{[{[{[]{()[[[]
 * [<(<(<(<{}))><([]([]()
 * <{([([[(<>()){}]>(<<{{
 * <{([{{}}[<[[[<>{}]]]>[]]
 * ```
 *
 * 其中某些行并没有被损坏，只是不太完整；目前你可以暂时忽略掉这些行。剩下有 5 行是损坏的：
 *
 * - `{([(<{}[<>[]}>{[]{[(<()>` 期望匹配 ]，但找到的是 }。
 * - `[[<[([]))<([[{}[[()]]]` 期望匹配 ]，但找到的是 )。
 * - `[<(<(<(<{}))><([]([]()` 期望匹配 >，但找到的是 )。
 * - `<{([([[(<>()){}]>(<<{{` 期望匹配 ]，但找到的是 >。
 *
 * 每个损坏的行，都在首次遇到不正确的闭字符时就停止。
 *
 * 你知道吗？语法检查器们实际上有个比赛，看谁能够在某个文件中获得语法错误的最高分。这当然是真的！要计算某行中语法错误的分数，就要先获得该行中**第一个不合法的字符**，
 * 并在下面的表格中查找：
 *
 * - `)`：3 点数。
 * - `]`：57 点数。
 * - `}`：1197 点数。
 * - `>`：25137 点数。
 *
 * 在上面的例子中，找到两次不合法的 `)` 字符（2*3 = **6** 点数），找到一次不合法的 `]` 字符（**57** 点数），找到一次不合法的 `}` 字符（**1197** 点数），
 * 以及一次不合法的 `>` 字符（**25137** 点数）。所以，该文件的总语法错误得分就是 6+57+1197+25137 = **26397** 点数！
 *
 * 第一个问题：在导航子系统中找出每个损坏行的第一个不合法字符。**这些错误的总语法错误得分是多少？**
 *
 * --- 第二部分 ---
 *
 * 现在，弃置掉哪些损坏的行。剩下的行都是**不完整的**。
 *
 * 不完整的行中并没有任何不正确的字符 —— 相反，它们只是缺失了某些在行末尾中的闭字符。为了修复导航子系统，你只需要找出能够补全该行中所有开放块的**闭字符序列**即可。
 *
 * 你只能使用指定的闭字符（即 `)`，`]`，`}` 或 `>`），并且你必须以正确的顺序添加它们，这样才能够形成合法的配对，并且所有的块最终都能被闭合。
 *
 * 在上面的例子中，有五个不完全的行：
 *
 * - `[({(<(())[]>[[{[]{<()<>>` —— 可以添加 `}}]])})]` 补全。
 * - `[(()[<>])]({[<{<<[]>>(` —— 可以添加 `)}>]})` 补全。
 * - `(((({<>}<{<{<>}{[]{[]{}` —— 可以添加 `}}>}>))))` 补全。
 * - `{<[[]]>}<{[{[{[]{()[[[]` —— 可以添加 `]]}}]}]}>` 补全。
 * - `<{([{{}}[<[[[<>{}]]]>[]]` —— 可以添加 `])}>` 补全。
 *
 * 你知道补全工具**也有**比赛吗？当然是真的！分数由一个一个的补全字符来确定。工具初始的分数是 0.然后对于每个字符，都先将总分乘以 5，然后将总分增加下表中该字符对应的点数：
 *
 * - `)`：1 点数。
 * - `]`：2 点数。
 * - `}`：3 点数。
 * - `>`：4 点数。
 *
 * 所以，上面最后的哪个补全字符串 `])}>` —— 将会按如下计分：
 *
 * - 初始总分是 0。
 * - 总分乘以 5 还是 0，再加上 `]` 的数值 (2) 得到新的总分 2。
 * - 总分乘以 5 得到 10，再加上 `)` 的数值 (1) 得到新的总分 11。
 * - 总分乘以 5 得到 55，再加上 `}` 的数值 (3) 得到新的总分 58。
 * - 总分乘以 5 得到 290，再加上 `>` 的数值 (4) 得到新总分 294。
 *
 * 上面五行补全字符各自最终总分如下：
 *
 * - `}}]])})]` —— 288957 总点数。
 * - `)}>]})` —— 5566 总点数。
 * - `}}>}>))))` —— 1480781 总点数。
 * - `]]}}]}]}>` —— 995444 总点数。
 * - `])}>` —— 294 总点数。
 *
 * 自动补全工具是非常奇怪 (odd) 的：胜者是通过将所有这些分数进行 **排序** ，然后在再取 **正中间** 的分数。（需要考虑的分数只会有奇数 (odd) 个。）在这个例子中，中间分数是 **288957**,
 * 因为比它大和比它小的分数是一样多的。
 *
 * 第二个问题：找出每个不完整行的补全字符串，对补全字符串计算分数，并将分数排序。**位于正中间的分数是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val illegal = it.findUnmatchedInALine(0, mutableListOf()) ?: return@forEach
            sum += illegal.errorPoints()
        }
        return sum
    }

    // 第二个问题
    fun part2(input: List<String>): Long {
        val incomplete = input.filter { it.findUnmatchedInALine(0, mutableListOf()) == null }
        val score = mutableListOf<Long>()
        incomplete.forEach {
            var sum = 0L
            generateIncompleteStringInALine(it).forEach { c ->
                sum = sum * 5 + Close.parse(c).completionPoints()
            }
            score.add(sum)
        }
        score.sort()
        return score[score.lastIndex / 2]
    }

    val testInput = readInput("day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("day10")
    check(part1(input) == 215229)
    check(part2(input) == 1105996483L)
}

private tailrec fun String.findUnmatchedInALine(i: Int, stack: MutableList<Char>): Close? {
    if (i == length) return null
    if (stack.isEmpty()) {
        stack.add(this[i])
    } else if (Open.isOpen(this[i])) {
        stack.add(this[i])
    } else if (Open.parse(stack.last()).closeCounterpart() == Close.parse(this[i])) {
        stack.removeLast()
    } else return Close.parse(this[i])

    return findUnmatchedInALine(i + 1, stack)
}

private fun generateIncompleteStringInALine(line: String): String {
    val stack = mutableListOf<Char>()
    line.forEach {
        if (Open.isOpen(it)) {
            stack.add(it)
        } else if (Open.parse(stack.last()).closeCounterpart() == Close.parse(it)) {
            stack.removeLast()
        }
    }
    stack.reverse()
    return stack.map {
        Open.parse(it).closeCounterpart().c
    }.joinToString("")
}

private enum class Open(val c: Char) {
    PARENTHESIS('('), BRACKETS('['), BRACE('{'), ANGLE('<');

    fun closeCounterpart() = when (this) {
        PARENTHESIS -> Close.PARENTHESIS
        BRACKETS -> Close.BRACKETS
        BRACE -> Close.BRACE
        ANGLE -> Close.ANGLE
    }

    companion object {
        fun parse(c: Char): Open = values().first { it.c == c }

        fun isOpen(c: Char): Boolean = values().firstOrNull { it.c == c }?.let { true } ?: false
    }
}

private enum class Close(val c: Char) {
    PARENTHESIS(')'), BRACKETS(']'), BRACE('}'), ANGLE('>');

    fun errorPoints() = when (this) {
        PARENTHESIS -> 3
        BRACKETS -> 57
        BRACE -> 1197
        ANGLE -> 25137
    }

    fun completionPoints() = when (this) {
        PARENTHESIS -> 1
        BRACKETS -> 2
        BRACE -> 3
        ANGLE -> 4
    }

    companion object {
        fun parse(c: Char): Close = values().first { it.c == c }
    }
}