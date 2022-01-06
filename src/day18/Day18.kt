package day18

import util.readInput
import kotlin.math.max

/**
 * --- 第 18 天：狮子鱼 ---
 *
 * 你下行至海沟中并遇到了一些 [狮子鱼(snailfish)](https://en.wikipedia.org/wiki/Snailfish). 它们说它们有看到雪橇的钥匙！如果你愿意为其中一只小一点的狮子鱼的
 * **数学作业(math homework)** 提供帮助，它们甚至能告诉你钥匙所在的方向。
 *
 * 狮子鱼的数字和普通的数字不同。相反，每个狮子鱼的数字都是一个 **对(pair)** - 即两个元素组成的一个有序列表。这个对中的每个元素都要么是一个普通的数字，要么是另一个对。
 *
 * 这些对可以用 `[x,y]` 的形式写下来，其中 `x` 和 `y` 就是该对中的元素。下面是一些狮子鱼数字的例子，每行都是一个狮子鱼数字：
 *
 * ```
 * [1,2]
 * [[1,2],3]
 * [9,[8,7]]
 * [[1,9],[8,5]]
 * [[[[1,2],[3,4]],[[5,6],[7,8]]],9]
 * [[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
 * [[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
 * ```
 *
 * 这条狮子鱼的作业是有关于 **加法(addition)** 的。若要将两个狮子鱼数字相加，需要把加号左右两边的参数组成一个对来计算。比如 `[1,2] + [[3,4],5]`
 * 就会变成 `[[1,2],[[3,4],5]]` 。
 *
 * 只有一个问题：**狮子鱼数字必须总是归约 (reduced) 的**, 因此将两个狮子鱼数字相加的过程会导致结果中的狮子鱼数字变少。
 *
 * 为了 **对某个狮子鱼数字归约**, 你必须在狮子鱼数字上重复做下面列表中的第一个行为：
 *
 * - 如果有任何对之中 **有 4 个对嵌套在里面**, 那么就从这个对的最左侧开始 **爆裂(explodes)**.
 * - 如果有任何普通数字 **大于等于 10**, 那么就从这个普通数字的最左侧开始 **分割(splits)**.
 *
 * 如果上述列表中的行为不再适用，则该狮子鱼数字就归约完毕。
 *
 * 在归约的过程中，至多只能作用一次行为，之后，流程返回到这个行为列表的顶部。比如，如果 **分割** 行为产生的某个对又满足了 **爆裂** 的标准，
 * 那么在继续执行其他 **分割** 行为前要先对该对进行 **爆裂** 操作。
 *
 * 要 **爆裂** 某个对，把这个对左侧的值增加到这个正在爆裂的对左侧第一个普通的数字上（如果有的话），并且把这个对右侧的值增加到这个正在爆裂的对右侧第一个的普通数字上（如果有的话）。
 * 这些正在爆裂的对总是由两个普通数字组成。然后，就用普通数字 `0` 来替换这个正在爆裂的对。
 *
 * 下面是单个爆裂行为的示例：
 *
 * - `[[[[[9,8],1],2],3],4]` 将变成 `[[[[0,9],2],3],4]` （在 `9` 的左边没有其他普通数字存在，所以它的值就没有增加到任何普通数字上）。
 * - `[7,[6,[5,[4,[3,2]]]]]` 将变成 `[7,[6,[5,[7,0]]]]` （在 `2` 的右边没有其他普通数字存在，所以它的值就没有增加到任何普通数字上）。
 * - [[6,[5,[4,[3,2]]]],1] 将变成 [[6,[5,[7,0]]],3]` 。
 * - `[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]` 将变成  `[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]` （对 [3,2] 不受影响是因为对 [7,3]
 * 先于它的左侧； [3,2] 将会在下一轮行为中再爆裂）。
 * - `[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]` 将变成 `[[3,[2,[8,0]]],[9,[5,[7,0]]]]`
 *
 * 要 **分割** 某个普通数字，就把它替换为一个对即可；把该对左边的普通数字除以 2 并 **向下** 取整，同时，把该对右侧的普通数字除以 2 并 **向上** 取整。
 * 比如 `10` 就变成 [5,5], `11` 变成 [5,6], `12` 变成了 [6,6] 并以此类推。
 *
 * 下面是对 [[[[4,3],4],4],[7,[[8,4],9]]] + [1,1] 找出归约结果的过程：
 *
 * 加法后： [[[[**[4,3]**,4],4],[7,[[8,4],9]]],[1,1]]
 *
 * 爆裂后： [[[[0,7],4],[7,[**[8,4]**,9]]],[1,1]]
 *
 * 爆裂后： [[[[0,7],4],[**15**,[0,13]]],[1,1]]
 *
 * 分割后： [[[[0,7],4],[[7,8],[0,**13**]]],[1,1]]
 *
 * 分割后： [[[[0,7],4],[[7,8],[0,**[6,7]**]]],[1,1]]
 *
 * 爆裂后： [[[[0,7],4],[[7,8],[6,0]]],[8,1]]
 *
 * 一旦不再适用于任何归约操作，余下的狮子鱼数字就是这个相加操作的结果了，即： [[[[0,7],4],[[7,8],[6,0]]],[8,1]].
 *
 * 家庭作业的任务要求将 **一系列的狮子鱼数字**(这便是你的谜题输入) 统统相加起来。列表中的每一行都是一个狮子鱼数字。将第一个狮子鱼数字和第二个加和，
 * 然后再把结果和第三个加和，再把结果和第四个加和，以此类推直到列表中的每一个数字都加和一遍。
 *
 * 比如，下面这个列表求和产生了 `[[[[1,1],[2,2]],[3,3]],[4,4]]` 这样的结果：
 *
 * ```
 * [1,1]
 * [2,2]
 * [3,3]
 * [4,4]
 * ```
 *
 * 下面这个列表求和产生了 `[[[[3,0],[5,3]],[4,4]],[5,5]]` 这样的结果：
 *
 * ```
 * [1,1]
 * [2,2]
 * [3,3]
 * [4,4]
 * [5,5]
 * ```
 *
 * 下面这个列表求和产生了 `[[[[5,0],[7,4]],[5,5]],[6,6]]` 这样的结果：
 *
 * ```
 * [1,1]
 * [2,2]
 * [3,3]
 * [4,4]
 * [5,5]
 * [6,6]
 * ```
 *
 * 下面是一个稍微更大一点的例子：
 *
 * ```
 * [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
 * [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
 * [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
 * [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
 * [7,[5,[[3,8],[1,4]]]]
 * [[2,[2,2]],[8,[8,1]]]
 * [2,9]
 * [1,[[[9,3],9],[[9,0],[0,7]]]]
 * [[[5,[7,4]],7],1]
 * [[[[4,2],2],6],[8,7]]
 * ```
 *
 * 将上面的狮子鱼数字全部加和后最终的求和结果就是： `[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]`:
 *
 * ```
 *   [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
 * + [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
 * = [[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]
 *
 *   [[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]
 * + [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
 * = [[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]
 *
 *   [[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]
 * + [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
 * = [[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]
 *
 *   [[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]
 * + [7,[5,[[3,8],[1,4]]]]
 * = [[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]
 *
 *   [[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]
 * + [[2,[2,2]],[8,[8,1]]]
 * = [[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
 *
 *   [[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
 * + [2,9]
 * = [[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]
 *
 *   [[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]
 * + [1,[[[9,3],9],[[9,0],[0,7]]]]
 * = [[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]
 *
 *   [[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]
 * + [[[5,[7,4]],7],1]
 * = [[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]
 *
 *   [[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]
 * + [[[[4,2],2],6],[8,7]]
 * = [[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]
 * ```
 *
 * 为了检查是否是正确的结果，狮子鱼老师只检查最终求和结果的 **量级(magnitude)**。每一对的量级都是其左元素量级的 3 倍，加上其右元素量级的 2 倍。
 * 而普通数字的量级就是普通数字自身。
 *
 * 比如， `[9,1]` 的量级为 3*9 + 2*1 = **29**; `[1,9]` 的量级则是 3*1 + 2*9 = **21** 。量级的计算是递归的: `[[9,1],[1,9]] 的量级就是 3*29 + 2*21 = **129**.
 *
 * 下面列出了更多量级的例子：
 *
 * - `[[1,2],[[3,4],5]]` 结果 **143**.
 * - `[[[[0,7],4],[[7,8],[6,0]]],[8,1]]` 结果 **1384**.
 * - `[[[[1,1],[2,2]],[3,3]],[4,4]]` 结果 **445**.
 * - `[[[[3,0],[5,3]],[4,4]],[5,5]]` 结果 **791**.
 * - `[[[[5,0],[7,4]],[5,5]],[6,6]]` 结果 **1137**.
 * - `[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]` 结果 **3488**.
 *
 * 所以，下面给出家庭作业的例子：
 *
 * ```
 * [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
 * [[[5,[2,8]],4],[5,[[9,9],0]]]
 * [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
 * [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
 * [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
 * [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
 * [[[[5,4],[7,7]],8],[[8,3],8]]
 * [[9,3],[[9,9],[6,[4,9]]]]
 * [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
 * [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
 * ```
 *
 * 最终的结果为：
 *
 * ```
 * [[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]
 * ```
 *
 * 上述最终结果的量级则是 **4140**。
 *
 * 第一个问题：把在家庭作业中所有的狮子鱼数字按照它们出现的顺序将加和起来。**最终求和结果的整个量级是多少？**
 *
 * --- 第二部分 ---
 *
 * 你注意到家庭作业的背面还有第二个问题：
 *
 * 如果只对两个狮子鱼数字做加法，你能的到的最大的量级是多少？
 *
 * 注意狮子鱼数字的加法是不符合 [交换律(commutative)](https://en.wikipedia.org/wiki/Commutative_property) 的 - 即 `x + y` 和 `y + x`
 * 有可能产生完全不同的结果。
 *
 * 让我们再次考虑上面家庭作业任务示例中的最后那个例子：
 *
 * ```
 * [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
 * [[[5,[2,8]],4],[5,[[9,9],0]]]
 * [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
 * [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
 * [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
 * [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
 * [[[[5,4],[7,7]],8],[[8,3],8]]
 * [[9,3],[[9,9],[6,[4,9]]]]
 * [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
 * [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
 * ```
 *
 * 上面列表中两个狮子鱼数字相加的最大量级是 **3993**。这是由 `[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]] + [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]`
 * 算出的量级，它们归约的结果是 `[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]`.
 *
 * 第二个问题：**将家庭作业任务中任意两个不同的狮子鱼数字加和，加和结果的量级最大是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        return input.map(String::buildSnailfishType)
            .map { it.type }
            .reduce { acc, pair ->
                val sum = acc + pair
                var splited = true
                while (splited) {
                    var leftChanged = true
                    var rightChanged = true
                    while (leftChanged || rightChanged) {
                        val reduce = sum.reduce()
                        leftChanged = reduce.leftChanged
                        rightChanged = reduce.rightChanged
                    }
                    splited = sum.split() != null
                }
                sum
            }.calcMagnitude()
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        var maxMagnitude = 0
        var i = 0
        var j: Int
        while (i < input.lastIndex) {
            j = i + 1
            while (j < input.size) {
                val sum1 = part1(listOf(input[i], input[j]))
                val sum2 = part1(listOf(input[j], input[i]))
                maxMagnitude = max(maxMagnitude, max(sum1, sum2))
                j++
            }
            i++
        }
        return maxMagnitude
    }

    val testInput = readInput("day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("day18")
    check(part1(input) == 3675)
    println(part2(input))
//    check(part2(input) == )
}

private fun String.buildSnailfishType(index: Int = 0): Result {
    var i = index
    var isLeft = true
    var left: SnailfishType? = null
    var right: SnailfishType? = null
    var result: Result? = null
    while (i <= lastIndex) {
        when (val cur = this[i++]) {
            '[' -> {
                result = buildSnailfishType(i)
                if (isLeft) left = result.type
                else right = result.type
                i = result.endIndex
            }
            ']' -> {
                return Result(SnailfishPair(left!!, right!!), i)
            }
            in '0'..'9' -> {
                if (isLeft) left = SnailfishNumber(cur.digitToInt())
                else right = SnailfishNumber(cur.digitToInt())
            }
            ',' -> isLeft = false
        }
    }
    return result!!
}

private operator fun SnailfishType.plus(type: SnailfishType) = SnailfishPair(this, type)

private operator fun SnailfishNumber.plus(type: SnailfishNumber) = SnailfishNumber(this.num + type.num)

private fun SnailfishType.reduce(level: Int = 0): ReduceResult {
    val cur = this as? SnailfishPair ?: error("only pair can reduce.")
    if (level != 3) {
        if (cur.left is SnailfishPair) {
            val (left, leftChanged, right, rightChanged) = cur.left.reduce(level + 1)
            if (left != null && right != null) {
                return if (!rightChanged) {
                    var curRight = cur.right
                    if (curRight is SnailfishNumber) {
                        cur.right = right + curRight
                    } else if (curRight is SnailfishPair) {
                        while (curRight is SnailfishPair && curRight.left is SnailfishPair) {
                            curRight = curRight.left
                        }
                        curRight = curRight as SnailfishPair
                        curRight.left = right + (curRight.left as SnailfishNumber)
                    }
                    ReduceResult(left, leftChanged, right, true)
                } else {
                    ReduceResult(left, leftChanged, right, rightChanged)
                }
            }
        }
        if (cur.right is SnailfishPair) {
            val (left, leftChanged, right, rightChanged) = cur.right.reduce(level + 1)
            if (left != null && right != null) {
                return if (!leftChanged) {
                    var curLeft = cur.left
                    if (curLeft is SnailfishNumber) {
                        cur.left = left + curLeft
                    } else if (curLeft is SnailfishPair) {
                        while (curLeft is SnailfishPair && curLeft.right is SnailfishPair) {
                            curLeft = curLeft.right
                        }
                        curLeft = curLeft as SnailfishPair
                        curLeft.right = left + (curLeft.right as SnailfishNumber)
                    }
                    ReduceResult(left, true, right, rightChanged)
                } else {
                    ReduceResult(left, leftChanged, right, rightChanged)
                }
            }
            return ReduceResult(left, leftChanged, right, rightChanged)
        }
    } else {
        val left = cur.left
        val right = cur.right
        if (left is SnailfishPair) {
            val subLeftNum = left.left as SnailfishNumber
            val subRightNum = left.right as SnailfishNumber
            cur.left = SnailfishNumber(0)
            if (right is SnailfishNumber) {
                cur.right = right + subRightNum
            } else if (right is SnailfishPair) {
                val leftNum = right.left as SnailfishNumber
                right.left = leftNum + subRightNum
            }
            return ReduceResult(subLeftNum, false, subRightNum, true)
        }
        // if there were five pairs would be cast error. wait to optimize.
        // for now loop in the main, to eliminate five pairs case.
        if (right is SnailfishPair) {
            val subLeftNum = right.left as SnailfishNumber
            var leftChanged = false
            val subRightNum = right.right as SnailfishNumber
            cur.right = SnailfishNumber(0)
            if (left is SnailfishNumber) {
                cur.left = left + subLeftNum
                leftChanged = true
            }
            return ReduceResult(subLeftNum, leftChanged, subRightNum, false)
        }
    }
    return ReduceResult(null, false, null, false)
}

private fun SnailfishType.split(): SnailfishPair? {
    when (this) {
        is SnailfishPair -> {
            val newLeft = this.left.split()
            if (newLeft != null) {
                if (newLeft != SnailfishPair.DUMMY) this.left = newLeft
                return SnailfishPair.DUMMY
            }
            val newRight = this.right.split()
            if (newRight != null) {
                if (newRight != SnailfishPair.DUMMY) this.right = newRight
                return SnailfishPair.DUMMY
            }
        }
        is SnailfishNumber -> {
            if (this.num >= 10) {
                val left = this.num / 2
                val right = left.let { if (this.num % 2 == 0) it else it + 1 }
                return SnailfishPair(SnailfishNumber(left), SnailfishNumber(right))
            }
        }
    }
    return null
}

private fun SnailfishType.calcMagnitude(): Int {
    return when (this) {
        is SnailfishPair -> {
            this.left.calcMagnitude() * 3 + this.right.calcMagnitude() * 2
        }
        is SnailfishNumber -> {
            this.num
        }
    }
}

private sealed interface SnailfishType

private data class SnailfishPair(
    var left: SnailfishType,
    var right: SnailfishType
) : SnailfishType {
    override fun toString(): String {
        return "[$left,$right]"
    }

    companion object {
        val DUMMY = SnailfishPair(SnailfishNumber.DUMMY, SnailfishNumber.DUMMY)
    }
}

private data class SnailfishNumber(
    var num: Int,
) : SnailfishType {
    override fun toString(): String {
        return "$num"
    }

    companion object {
        val DUMMY = SnailfishNumber(-1)
    }
}

@JvmRecord
private data class Result(
    val type: SnailfishType,
    val endIndex: Int
)

private data class ReduceResult(
    val left: SnailfishNumber?,
    var leftChanged: Boolean,
    val right: SnailfishNumber?,
    var rightChanged: Boolean
)