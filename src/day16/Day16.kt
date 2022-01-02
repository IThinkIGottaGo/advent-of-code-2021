package day16

import util.readInputOneLine

/**
 * --- 第 16 天：数据包解码 ---
 *
 * 正当你离开洞穴并到达开放水域的时候，你收到了来自船上的小精灵们发来的信息。
 *
 * 该信息是通过浮力交换传输系统 (Buoyancy Interchange Transmission System (BITS)) 发送的，这是一种将数字表达式打包为二进制序列的方法。
 * 你潜艇的电脑已经以十六进制的形式保存好了该信息（这便是你的谜题输入）。
 *
 * 解码消息的第一步就是把十六进制的表现形式转换为二进制。十六进制中的每个字符都对应了二进制数据中的四个比特位：
 *
 * ```
 * 0 = 0000
 * 1 = 0001
 * 2 = 0010
 * 3 = 0011
 * 4 = 0100
 * 5 = 0101
 * 6 = 0110
 * 7 = 0111
 * 8 = 1000
 * 9 = 1001
 * A = 1010
 * B = 1011
 * C = 1100
 * D = 1101
 * E = 1110
 * F = 1111
 * ```
 *
 * BITS 的消息在最外层只有一个**包**，该包自身又包含了很多其他的包。这个包的十六进制形式有可能编码了在末尾的一些额外的 0 比特；而这些不属于该消息的一部分并且应该予以忽略。
 *
 * 每个包都以一个标准的头部开始：前三位编码了包的 **版本号(version)**, 然后接下来三位编码了包的 **类型 ID(type ID)** 。这两个值都是数字；
 * 在任一包中编码的所有数字都用高位在前的二进制表示。比如，以二进制序列 `100` 编码的版本号即代表数字 4。
 *
 * 类型 ID 为 `4` 的包则代表了一个 **字面量值(literal value)**。字面量值的包编码了一个二进制数字。为此，二进制数字用先导零填充，直到长度为 4 的倍数，
 * 然后再将它每 4 位分成一组。除了最后一组之外，其余的组都用比特 1 作为前缀，最后一组则用比特 0 作为前缀。这些由 5 位组成的组紧跟在包的头部后面。
 * 比如，十六进制字符串 `D2FE28` 将变成：
 *
 * ```
 * 110100101111111000101000
 * VVVTTTAAAAABBBBBCCCCC
 * ```
 *
 * 每个位下面都有一个标签来便于阐释它的用途：
 *
 * - 用 `V` 打标的三个位 (`110`) 是包版本号，即 `6`。
 * - 用 `T` 打标的三个位 (`100`) 是包类型 ID，即 `4`，意味着该包是一个字面量值。
 * - 用 `A` 打标的五个位 (`10111`) 则从比特 1 开始（不是最后一组，继续）并包含目标数字的第一个 4 位，`0111`。
 * - 用 `B` 打标的五个位 (`11110`) 也从比特 1 开始（不是最后一组，继续）并包含目标数字的再一个 4 位，`1110`。
 * - 用 `C` 打标的五个位 (`11110`) 则从比特 0 开始（最后一组，包的末尾）并包含目标数字的最后 4 位，`0101`。
 * - 末尾三个未被打标的 0 比特则是因为十六进制表示而导致的，因此应当予以忽略。
 *
 * 所以，这个包代表的字面量值的二进制表示则是 `011111100101`，即十进制中的 `2021`。
 *
 * 所有其他类型的包（即任何类型 ID 不为 4 的包）则代表了一个 **操作(operator)**, 可以在其所含的一个或多个子包上执行某些计算。暂时，这些特定的操作不重要；
 * 我们现在只需聚焦于子包层次的解析上。
 *
 * 一个操作包内含一个或多个子包。为了能够表示其中的二进制数据对应了其中的哪个子包，操作包可以通过包头后紧跟着的位来表示以下两个模式中的一种；
 * 这也被称为 **长度类型 ID(length type ID)**：
 *
 * - 如果长度类型 ID 是 `0`，则接下来的 **15** 位表示以 **位形式** 所表示的，该包中子包的 **总长度** 。
 * - 如果长度类型 ID 是 `1`，则接下来的 **11** 位表示由 **该包直接所包含的，子包的个数**。
 *
 * 最终，在长度类型 ID，和 15-位或 11-位字段之后，子包出现了。
 *
 * 比如说，下面是一个长度类型 ID 为 `0` 的操作包（十六进制字符串为 `38006F45291200`），并包含了两个子包：
 *
 * ```
 * 00111000000000000110111101000101001010010001001000000000
 * VVVTTTILLLLLLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBBBBBB
 * ```
 *
 * - 用 `V` 打标的三个位 (`001`) 是包版本号，即 `1`。
 * - 用 `T` 打标的三个位 (`110`) 是包类型 ID，即 `6`，意味着该包是一个操作包。
 * - 用 `I` 打标的位 (`0`) 则是长度类型 ID，表明接下来的 15 位代表了再后续有多少位属于子包。
 * - 用 `L` 打标的 15 位 (`000000000011011`) 以位的形式表示了子包的长度，即 `27`。
 * - 用 `A` 打标的 11 位则是第一个子包，其字面量表示的数字是 `10`。
 * - 用 `B` 打标的 16 位则是第二个子包，其字面量表示的数字是 `20`。
 *
 * 在读取了 11 位和 16 位子包的数据后，L (27) 中所表示的总长度已然达到，所以对该包的解析到此为止。
 *
 * 作为另一个例子，下面是一个长度类型 ID 为 `1` 的操作包（十六进制字符串为 `EE00D40C823060`），并包含了三个子包：
 *
 * ```
 * 11101110000000001101010000001100100000100011000001100000
 * VVVTTTILLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBCCCCCCCCCCC
 * ```
 *
 * - 用 `V` 打标的三个位 (`111`) 是包版本号，即 `7`。
 * - 用 `T` 打标的三个位 (`011`) 是包类型 ID，即 `3`，意味着该包是一个操作包。
 * - 用 `I` 打标的位 (`1`) 则是长度类型 ID，表明接下来的 11 位代表了子包的个数。
 * - 用 `L` 打标的 11 位 (`00000000011`) 表示了子包的个数，即 `3`。
 * - 用 `A` 打标的 11 位则是第一个子包，其字面量表示的数字是 `1`。
 * - 用 `B` 打标的 11 位则是第二个子包，其字面量表示的数字是 `2`。
 * - 用 `C` 打标的 11 位则是第三个子包，其字面量表示的数字是 `3`。
 *
 * 在完整读取了 3 个子包的数据后，L (3) 中所表示的子包个数已然达到，所以对该包的解析到此为止。
 *
 * 目前，解析该消息中包的层次结构，并将**所有的版本号数字累加起来**。
 *
 * 下面还有一些十六进制编码的消息的例子：
 *
 * - `8A004A801A8002F478` 是一个操作包 (版本号是 4)，其中含有一个子包 (版本号是 1)，子包又包含了另一个子包（版本号是 5），子子包中又含有一个字面量值（版本号是 6）；
 * 因此这个包版本号的总和为 **16**。
 * - `620080001611562C8802118E34` 是一个操作包（版本号是 3），其中含有两个子包；每个子包又都是一个操作包，并且包含了两个字面量值。这个包的版本号总和为 **12**。
 * - `C0015000016115A2E0802F182340` 和前面的例子有着相同的结构，但其最外层的包使用了不同的长度类型 ID。这个包的版本号总和为 **23**。
 * - `A0016C880162017C3686B18A3D4780` 是一个操作包并含有另一个操作包，另一个操作包中又还含有一个操作包，并且最后这个操作包又含有 5 个字面量值；它的版本号总和为 **31**。
 *
 * 第一个问题：解码你的十六进制编码的 BITS 消息的结构；**如果你将其中所有包的版本号累加起来你会得到多少？**
 *
 * --- 第二部分 ---
 *
 * 现在你已经有你消息解码后的结构了，你可以计算表达式所表示的值是多少了。
 *
 * 如上面所说，字面量值 (类型 ID 为 4) 代表了一个数字。而剩下的类型 ID 则更加有趣。
 *
 * - 类型 ID 为 `0` 的包是 **求和(sum)** 包 - 它们将其所拥有的子包的值全都加起来就是它们的值。如果只有一个子包，那么它们的值就是这个子包的值。
 * - 类型 ID 为 `1` 的包是 **乘积(product)** 包 - 它们将其所拥有的子包的值全都乘起来就是它们的值。如果只有一个子包，那么它们的值就是这个子包的值。
 * - 类型 ID 为 `2` 的包是 **最小值(minimum)** 包 - 它们的值就是它子包中值最小的那个子包的值。
 * - 类型 ID 为 `3` 的包是 **最大值(maximum)** 包 - 它们的值就是它子包中值最大的那个子包的值。
 * - 类型 ID 为 `5` 的包是 **大于(greater than)** 包 - 如果它们的第一个子包的值大于第二个子包，则它们的值就是 **1**, 反之，它们的值就是 **0**。
 * 这样类型 ID 的包有且只会有两个子包。
 * - 类型 ID 为 `6` 的包是 **小于(greater than)** 包 - 如果它们的第一个子包的值小于第二个子包，则它们的值就是 **1**, 反之，它们的值就是 **0**。
 * 这样类型 ID 的包有且只会有两个子包。
 * - 类型 ID 为 `7` 的包是 **等于(equal to)** 包 - 如果它们的第一个子包的值等于第二个子包，则它们的值就是 **1**, 反之，它们的值就是 **0**。
 * 这样类型 ID 的包有且只会有两个子包。
 *
 * 通过使用上述规则，你现在能算出你 BITS 信息中这个最外层的包的值是多少了。
 *
 * 比如：
 *
 * - `C200B40A82` 是对 `1` 和 `2` 的求和，所以它的值就是 **3**。
 * - `04005AC33890` 是对 `6` 和 `9` 相乘，所以它的值就是 **54**。
 * - `880086C3E88112` 是找出 `7`, `8` 和 `9` 之中的最小值，所以它的值就是 **7**。
 * - `CE00C43D881120` 是找出 `7`, `8` 和 `9` 之中的最大值，所以它的值就是 **9**。
 * - `D8005AC2A8F0` 则产生 `1`, 因为 `5` 小于 `15`.
 * - `F600BC2D8F` 则产生 `0`, 因为 `5` 不大于 `15`.
 * - `9C005AC2F8F0` 则产生 `0`, 因为 `5` 不等于 `15`.
 * - `9C0141080250320F1802104A08` 则产生 `1`, 因为 `1 + 3 = 2 * 2`.
 *
 * 第二个问题：**如果你对你的十六进制编码的 BITS 消息按如上规则对表达式求值，你会得到多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: String): Int {
        val bitsInput = convertHexToBits(input)
        val operator = parseOperatorPacket(bitsInput, 1, 1).packets[0] as Operator
        var sumOfVersion = 0
        operator.forEach {
            sumOfVersion += it.version
        }
        return sumOfVersion
    }

    // 第二个问题
    fun part2(input: String): Long {
        val bitsInput = convertHexToBits(input)
        val operator = parseOperatorPacket(bitsInput, 1, 1).packets[0] as Operator
        return operator.evaluate { typeId, values ->
            when (typeId) {
                0 -> values.sum()
                1 -> values.fold(1, Long::times)
                2 -> values.minOrNull()!!
                3 -> values.maxOrNull()!!
                5 -> if (values[0] > values[1]) 1 else 0
                6 -> if (values[0] < values[1]) 1 else 0
                7 -> if (values[0] == values[1]) 1 else 0
                else -> error("unknown type id!")
            }
        }
    }

    val testInput = readInputOneLine("day16_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 54L)

    val input = readInputOneLine("day16")
    check(part1(input) == 925)
    check(part2(input) == 342997120375)
}

private fun convertHexToBits(hex: String): String =
    hex.map { hexMap[it] }.joinToString("")

private fun parseOperatorPacket(input: String, type: Int, num: Int): Result {
    var startIndex = 0
    var limit = 0
    val subPacket = mutableListOf<Packet>()
    while (limit < num) {
        val version = input.substring(startIndex, startIndex + 3).toInt(2)
        startIndex += 3
        val typeId = input.substring(startIndex, startIndex + 3).toInt(2)
        startIndex += 3
        if (typeId == 4) {
            val (literalValue, endIndex) = calcLiteralValue(input.substring(startIndex))
            subPacket.add(Literal(version, typeId, literalValue))
            startIndex += endIndex + 1
            if (type == 0) limit = startIndex else limit++
        } else {
            if (input[startIndex++].digitToInt() == 0) {
                val length = input.substring(startIndex, startIndex + 15).toInt(2)
                startIndex += 15
                val (operator, endIndex) = parseOperatorPacket(
                    input.substring(startIndex, startIndex + length),
                    0,
                    length
                )
                subPacket.add(Operator(version, typeId, operator))
                startIndex += endIndex + 1
                if (type == 0) limit = startIndex else limit++
            } else {
                val number = input.substring(startIndex, startIndex + 11).toInt(2)
                startIndex += 11
                val (operator, endIndex) = parseOperatorPacket(input.substring(startIndex), 1, number)
                subPacket.add(Operator(version, typeId, operator))
                startIndex += endIndex + 1
                if (type == 0) limit = startIndex else limit++
            }
        }
    }
    return Result(subPacket, startIndex - 1)
}

private fun calcLiteralValue(input: String): Value {
    var startIndex = 0
    var binaryString = ""
    var stop = false
    while (!stop) {
        stop = input[startIndex++].digitToInt() == 0
        binaryString += input.substring(startIndex, startIndex + 4)
        startIndex += 4
    }
    return Value(binaryString.toLong(2), startIndex - 1)
}

private sealed class Packet(
    val version: Int,
    val typeID: Int,
)

private class Operator(
    version: Int,
    typeID: Int,
    val subPacket: List<Packet>
) : Packet(version, typeID) {
    override fun toString(): String {
        return "Operator(version=$version, typeID=$typeID subPacket=$subPacket)"
    }

    fun forEach(action: (Packet) -> Unit) {
        action(this)
        subPacket.forEach {
            when (it) {
                is Operator -> it.forEach(action)
                is Literal -> action(it)
            }
        }
    }

    fun evaluate(eval: (Int, List<Long>) -> Long): Long {
        val values = mutableListOf<Long>()
        subPacket.forEach {
            when (it) {
                is Operator -> values.add(it.evaluate(eval))
                is Literal -> values.add(it.value)
            }
        }
        return eval(typeID, values)
    }
}

private class Literal(
    version: Int,
    typeID: Int,
    val value: Long
) : Packet(version, typeID) {
    override fun toString(): String {
        return "Literal(version=$version, typeID=$typeID subPacket=$value)"
    }
}

private data class Result(
    val packets: List<Packet>,
    val endIndex: Int
)

private data class Value(
    val value: Long,
    val endIndex: Int
)

private val hexMap = mutableMapOf<Char, String>().apply {
    put('0', "0000")
    put('1', "0001")
    put('2', "0010")
    put('3', "0011")
    put('4', "0100")
    put('5', "0101")
    put('6', "0110")
    put('7', "0111")
    put('8', "1000")
    put('9', "1001")
    put('A', "1010")
    put('B', "1011")
    put('C', "1100")
    put('D', "1101")
    put('E', "1110")
    put('F', "1111")
}