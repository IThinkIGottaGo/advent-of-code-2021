package day13

import util.readInput

/**
 * 第 13 天：透明折纸（Transparent Origami）
 *
 * 你到达了洞穴中另一个火山活动活跃的部分。如果你能做一些热成像工作就好了，这样你就能提前知道哪些洞穴太热以至于无法安全进入。
 *
 * 幸运的是，潜艇似乎配备了热成像摄像仪！但当你激活它的时候，迎接你的是：
 *
 * `感谢您的购买！要激活该红外热成像摄像系统，请输入手册第 1 页上的代码。`
 *
 * 显然，小精灵们还没有使用过该功能。令你惊讶的是，你找到了手册；但当你打开它的时候，第一页掉落了出来。居然是一张很大的 [透明页(transparent paper)](https://en.wikipedia.org/wiki/Transparency_(projection))!
 * 这张透明页被随机的端点标记，并含有如何将其折叠起来的说明（这便是你的谜题输入）。例如：
 *
 * ```
 * 6,10
 * 0,14
 * 9,10
 * 0,3
 * 10,4
 * 4,11
 * 6,0
 * 6,12
 * 4,1
 * 0,13
 * 10,12
 * 3,4
 * 3,0
 * 8,4
 * 1,10
 * 2,14
 * 8,10
 * 9,0
 *
 * 沿着 y=7 折叠
 * 沿着 x=5 折叠
 * ```
 *
 * 上面的第一部分是透明页上标记端点的列表。`0,0` 代表了最左上方的坐标。第一个值 `x` 表示向右增加。第二个值 `y` 表示向下增加。所以，坐标 `3,0` 位于 `0,0` 的右侧，
 * 坐标 `0,7` 位于 `0,0` 的下方。上面的例子中坐标组成了下面这个模式，其中 `#` 号代表位于页面上的某个端点，而 `.` 号则表示的是空白、未被标记的位置。
 *
 * ```
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 * ```
 *
 * 然后，接着是 **折叠说明(fold instructions)** 的列表。每条说明都表明了透明页上的某一行/列，并希望你能把这个页面 **向上折(up)** (即 y=... 行水平向上)或
 * **向左折(left)** (即 `x=...` 列竖直向左)。在上面的例子中，第一个折叠的说明是 `沿着 y=7 折叠` ，这指明了所有构成 y 等于 7 这行的位置（下图中由 `-` 标记的地方）：
 *
 * ```
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * -----------
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 * ```
 *
 * 又因为这是一个水平行，所以从底部 **向上** 折叠。在折叠完成后，某些端点可能会重合，但位于折行上的端点则不会再出现。折叠完成后的结果看起来像下面这样：
 *
 * ```
 * #.##..#..#.
 * #...#......
 * ......#...#
 * #...#......
 * .#.#..#.###
 * ...........
 * ...........
 * ```
 *
 * 现在只能看见 17 个端点了（注意：有点的地方是用 `#` 号标注的，`.` 号表示空白）
 *
 * 比如说，可以注意到在透明页折叠之前左下方角落有两个端点；在折叠完成之后，这些端点跑到左上方角落去了（即在 `0,0` 和 `0,1`）。又因为这张纸是透明的，结果在它们下面的端点（位于 `0,3`）
 * 依然是可见的，这正如它可以透过透明页看到的那样。
 *
 * 还注意到某些端点最终 **重叠(overlapping)** 在了一起；在这种情况下，多个端点合并在了一起，就变成了单个端点。
 *
 * 第二次折叠的说明则是 `沿着 x=5 折叠` ，其表明了是下面这列：
 *
 * ```
 * #.##.|#..#.
 * #...#|.....
 * .....|#...#
 * #...#|.....
 * .#.#.|#.###
 * .....|.....
 * .....|.....
 * ```
 *
 * 因为这是一个竖直列，所以 **向左折** ：
 *
 * ```
 * #####
 * #...#
 * #...#
 * #...#
 * #####
 * .....
 * .....
 * ```
 *
 * 上面的折叠说明构成了一个方形！
 *
 * 透明页非常的大，所以现在，先专注于只完成第一次折叠。在上面例子中的第一次折叠完成后，可以看到有 **17** 个端点 —— 那些在折叠完成后重叠在一起的端点视作为单个端点。
 *
 * 第一个问题：**在你的透明页上只按照第一个折叠说明折叠后，可以见到多少个端点？**
 *
 * --- 第二部分 ---
 *
 * 根据说明完成整个透明页的折叠。手册上说代码总是由 **八个大写字母** 组成。
 *
 * 第二个问题：**你会得到什么样的代码来激活该红外热成像摄像系统呢？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val (dots, rules) = input.parseInput()
        val paper = dots.generatePaper()
        paper.ruleFold(rules[0])
        return paper.flatten().count { it == '#' }
    }

    // 第二个问题
    fun part2(input: List<String>): String {
        val (dots, rules) = input.parseInput()
        val paper = dots.generatePaper()
        rules.forEach { rule ->
            paper.ruleFold(rule)
        }
        return paper.joinToString("\n") { o ->
            o.joinToString("")
        }
    }

    val testInput = readInput("day13_test")
    check(part1(testInput) == 17)

    val input = readInput("day13")
    check(part1(input) == 701)
    println(part2(input)) // FPEKBEJL
}

private fun List<String>.parseInput(): Pair<List<Dot>, List<Rule>> {
    val dots = mutableListOf<Dot>()
    val rules = mutableListOf<Rule>()
    val regex = """[\s\S]+(\w+)=(\d+)$""".toRegex()
    for (s in this) {
        if (s.contains(',')) {
            val (x, y) = s.split(',')
            dots.add(Dot(x.toInt(), y.toInt()))
        } else if (s.contains('=')) {
            val (type, line) = regex.find(s)!!.destructured
            rules.add(Rule(type, line.toInt()))
        }
    }
    return Pair(dots, rules)
}

private fun List<Dot>.generatePaper(): MutableList<MutableList<Char>> {
    val column = maxOf { it.x } + 1
    val row = maxOf { it.y } + 1
    val paper = mutableListOf<MutableList<Char>>()
    var temp = mutableListOf<Char>()
    repeat(row * column) {
        val char = if (contains(Dot(it % column, (it - it % column) / column))) '#' else '.'
        temp.add(char)
        if ((it + 1) % column == 0) {
            paper.add(temp)
            temp = mutableListOf()
        }
    }
    return paper
}

private fun MutableList<MutableList<Char>>.ruleFold(rule: Rule) {
    val line = rule.line
    if (rule.type == FoldType.UP) {
        foldIt(line)
    } else if (rule.type == FoldType.LEFT) {
        reformat()
        foldIt(line)
        reformat()
    }
}

private fun MutableList<MutableList<Char>>.reformat() {
    val row = size
    val column = first().size
    val source = flatten()
    val list = mutableListOf<MutableList<Char>>()
    var rows = mutableListOf<Char>()
    repeat(column) { c ->
        repeat(row) { r ->
            rows.add(source[r * column + c])
        }
        list.add(rows)
        rows = mutableListOf()
    }
    clear()
    addAll(list)
}

private fun MutableList<MutableList<Char>>.foldIt(line: Int) {
    val row = size
    val column = this[0].size
    var rowBias = 1
    while (line + rowBias <= lastIndex && line - rowBias >= 0) {
        var i = 0
        while (i < column) {
            val b = if (this[line + rowBias][i] == '#' || this[line - rowBias][i] == '#') '#' else '.'
            this[line + rowBias][i] = b
            this[line - rowBias][i] = b
            ++i
        }
        ++rowBias
    }
    this[line].fill('.')
    if (line < row - 1 - line) {
        repeat(line + 1) { removeFirst() }
        reverse()
    } else {
        repeat(row - line) { removeLast() }
    }
}

@JvmRecord
private data class Dot(
    val x: Int,
    val y: Int
)

private class Rule(type: String, val line: Int) {
    val type: FoldType = if (type == "x") FoldType.LEFT else if (type == "y") FoldType.UP else error("data error!")

    override fun toString(): String {
        return "Rule(type=$type, line=$line)"
    }
}

private enum class FoldType {
    UP, LEFT
}