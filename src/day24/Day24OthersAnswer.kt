package day24

import util.readInput
import kotlin.math.max
import kotlin.math.min

/**
 * Day24 第一、二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day24.kt)
 * 的解答。
 *
 * 整个真实题目的输入由每 18 行构成一个区块，总共 14 个块组成。每个块之间，可能不同的地方只有三处，即每个块的第 5、6、16 行可能不同，其余指令行都完全相同。
 * 因此我们把这可能存在不同的三处赋值到 Parameters 类的 a，b，c 三个字段上，这个过程由 parseMagicParameters 方法完成。
 *
 * 每个块中所列出的 18 行指令基本上就是对前面 a，b，c 处所赋值的字段进行了 magicFunction 方法中的内容，另外题目中提到了需要对除法四舍五入，
 * 但 Java/Kotlin 已经自动的为我们解决了这个问题，所以这里无需特殊处理。
 *
 * 为了能够返回谜题中要求的最大值和最小值， solve() 方法接受 magicParameters 参数，该参数由 14 个块中的 Parameters(a, b, c) 组成。
 * 为了能够追踪计算后 z 的值，zValues 这个 Map 变量的 key 则用来存储 z 值，而 value 用来保存找到的最大/最小值。最外层循环遍历这 14 个块，
 * 但是再对某一个块的处理结束前，我们不希望覆盖了 zValues 中现有的值，所以创建了 zValuesThisRound 来用作每个块遍历过程中的 zValues，
 * 并在每个块结束后使用它取代掉外部原本的 zValues。
 *
 * 在每个块的处理过程中，遍历初始提供的 zValues Map 集合（注意最开始时是传入了 (0, (0,0)）作为第一个元素），并内循环再遍历可能的数码 1..9
 * 共 10 个 digit（题目给定 model number 只能由 1-9 组成），每次都将最外层遍历中的 parameters, 当前遍历的 digit 和外循环遍历 zValues
 * 的 z 值一同传入 magicFunction 中得到新的 z 值。若该块中 parameters 解析的第一个可能存在不同的命令
 * （此时由 parameters 的 a 字段持有该命令的操作数）的操作数为 1，或者是 26 且这个新的 z 值小于正在遍历 zValues 中的 z 值（即这一位数码合法的情况下），
 * 则将这个新 z 值作为 key 存入 newValueForZ 中，而 value 则取决于，如果这个新 z 值作为 key 已经在 newValueForZ 中存在，那么就和新的 value
 * 进行比较，将更小/更大的新 value 存入 value 中。而新 value 是通过当前 zValues 中当前遍历的 z 值对应的 value 中，所存储的最大/最小值乘以 10
 * 并加上我们正在遍历的数码 digit 而得出的。
 *
 * 在循环全部完成的最后，在 z 值 Map 中存储 key 为 0 的 value 即存储了我们的最大/最小值。
 */
fun main() {
    fun part1(input: List<String>): Long {
        val magicParameters = parseMagicParameters(input)
        return solve(magicParameters).max
    }

    fun part2(input: List<String>): Long {
        val magicParameters = parseMagicParameters(input)
        return solve(magicParameters).min
    }

    val input = readInput("day24")
    check(part1(input) == 99598963999971L)
    check(part2(input) == 93151411711211L)
}

private fun solve(magicParameters: List<Parameters>): MinMax {
    var zValues = mutableMapOf(0L to MinMax())
    magicParameters.forEach { parameters ->
        val zValuesThisRound = mutableMapOf<Long, MinMax>()
        zValues.forEach { (z, minMax) ->
            (1..9).forEach { digit ->
                val newValueForZ = magicFunction(parameters, z, digit.toLong())
                if (parameters.a == 1 || (parameters.a == 26 && newValueForZ < z)) {
                    zValuesThisRound[newValueForZ] = MinMax(
                        min(zValuesThisRound[newValueForZ]?.min ?: Long.MAX_VALUE, minMax.min * 10 + digit),
                        max(zValuesThisRound[newValueForZ]?.max ?: Long.MIN_VALUE, minMax.max * 10 + digit)
                    )
                }
            }
        }
        zValues = zValuesThisRound
    }
    return zValues.getValue(0)
}

private fun magicFunction(parameters: Parameters, z: Long, w: Long): Long =
    if (z % 26 + parameters.b != w) ((z / parameters.a) * 26) + w + parameters.c
    else z / parameters.a

private fun parseMagicParameters(input: List<String>): List<Parameters> =
    input.chunked(18).map {
        Parameters(
            it[4].substringAfterLast(" ").toInt(),
            it[5].substringAfterLast(" ").toInt(),
            it[15].substringAfterLast(" ").toInt(),
        )
    }

@JvmRecord
private data class Parameters(val a: Int, val b: Int, val c: Int)

@JvmRecord
private data class MinMax(val min: Long = 0, val max: Long = 0)