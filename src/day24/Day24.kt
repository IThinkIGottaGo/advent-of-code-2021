package day24

import util.readInput

/**
 * --- 第 24 天：算术逻辑单元 (Arithmetic Logic Unit) ---
 *
 * 一股 [谜之烟雾 (magic smoke)](https://en.wikipedia.org/wiki/Magic_smoke) 开始从潜艇的 [算术逻辑单元 (arithmetic logic unit)](https://en.wikipedia.org/wiki/Arithmetic_logic_unit)
 * 中冒了出来。要是丧失了执行基本算术和逻辑运算的能力，潜艇就不能用它的圣诞灯光来产生酷炫的图案了！
 *
 * 另外潜艇也无法导航。而且氧气系统也无法工作了。
 *
 * 不过别担心 - 你 **大概** 有足够的氧气剩余来让你有充分的时间构建一个新的 ALU。
 *
 * 我们的 ALU 是一个四维处理单元：它有整型变量 w，x，y 和 z。这些变量全都是从 0 开始的。ALU 还支持 **六种指令**。
 *
 * - `inp a` - 读取某个输入值，并将其写到变量 a 中。
 * - `add a b` - 将 a 的值和 b 的值相加，然后将结果存储在变量 a 中。
 * - `mul a b` - 将 a 的值和 b 的值相乘，然后将结果存储在变量 a 中。
 * - `div a b` - 将 a 的值除以 b 的值，将结果截断为整数，然后将结果存储在变量 a 中。（在这里，“截断”指的是向零取整。）
 * - `mod a b` - 将 a 的值除以 b 的值，然后将 **余数** 存储在变量 a 中。（这也被称为 [求余 (modulo)](https://en.wikipedia.org/wiki/Modulo_operation) 操作。）
 * - `eql a b` - 如果 a 和 b 的值相等，然后将值 1 存储在变量 a 中。否则，将值 0 存储在变量 a 中。
 *
 * 在上面所有这些指令中，a 和 b 都是占位符；a 始终都是存储操作结果的变量（w，x，y，z 中的一个），而 b 既可以是一个变量也可以是一个数字。若是数字则可以正数或负数，但总归是整型数。
 *
 * 这个 ALU 并没有 **跳转(jump)** 指令；在这个 ALU 的程序中，每个指令都从上到下执行且只精确运行一次。在最后的指令完成后程序就会终止。
 *
 * （程序的作者应该尤其的小心；尝试在 b=0 上执行除法，或者尝试在 a<0 或 b<=0 的时候执行求余操作的时候都会引发程序崩溃，并且甚至可能会损害 ALU。
 * 这些操作是不会想在任何正经的 ALU 程序中发生的。）
 *
 * 比如，下面是一个 ALU 程序，它可以接受一个输入的数字，然后对它取负，并将它存储到 x 变量中：
 *
 * ```
 * inp x
 * mul x -1
 * ```
 *
 * 下面这个 ALU 程序则接受两个输入数字，然后如果第二个输入的数字比第一个输入的数字大三倍的话，就将 z 设为 1，否则就将 z 设为 0。
 *
 * ```
 * inp z
 * inp x
 * mul z 3
 * eql z x
 * ```
 *
 * 下面这个 ALU 程序接受一个非负整数作为输入，让后将它转化为二进制，并将最低位 (即 1) 存储到 z 中，将第二低位 (即 2) 存储到 y 中，将第三低位（即 4）存储到 x 中，
 * 并将第四低位（即 8）存储到 w 中：
 *
 * ```
 * inp w
 * add z w
 * mod z 2
 * div w 2
 * add y w
 * mod y 2
 * div w 2
 * add x w
 * mod x 2
 * div w 2
 * mod w 2
 * ```
 *
 * 一旦你构建好了替代的 ALU，你就可以安装它到潜艇上了，当 ALU 错误的时候它就会立即恢复它正在做的工作：即验证潜艇的 **型号 (model number)**。
 * 为了做到这一点，ALU 将会运行型号自动检测程序（MOdel Number Automatic Detector program）（MONAD，这便是你的谜题输入）。
 *
 * 潜艇的型号总是由 **14 位数字** 组成的，并且数码只有 1 到 9。数码 0 **不会** 出现在型号中。
 *
 * 当 MONAD 检查某个假定的 14 位型号时，他会使用 14 个独立的 `inp` 指令，每个指令都预期型号中的 **一个数码**，并沿着从先到后的顺序。（所以，
 * 假如要检测的型号是 `13579246899999`, 你就提供 1 给第一个 `inp` 指令，3 给第二个 `inp` 指令，5 给第三个 `inp` 指令，以此类推。）这意味着在操作
 * MONAD 的时候，每个输入指令都应该只给定一个整数值，范围从最小的 1 到最大的 9。
 *
 * 然后，在 MONAD 运行完它所有的指令之后，它就会把 0 存储在变量 z 中来表明型号是 **合法的 (valid)** ，但是，如果型号是 **不合法的 (invalid)** ，
 * 它就会存储其他的非零值在变量 z 中。
 *
 * MONAD 还会在型号上强加额外的，神秘的限制，并且传说说 MONAD 文档的最后一份拷贝被一只 [狸猫 (tanuki)](https://en.wikipedia.org/wiki/Japanese_raccoon_dog) 吃掉了。
 * 所以你还需要通过其他方式来 **找出 MONAD 做了什么**。
 *
 * 第一个问题：为了能够尽可能多的启用潜艇的功能，请找出最大的，不含 0 数码的合法 14 位型号。**能够由 MONAD 接受的最大型号是多少？**
 *
 * --- 第二部分 ---
 *
 * 正当你的潜艇像 [Retro Encabulator](https://www.youtube.com/watch?v=RXJKdh1KZ0w) 那样正在启动各种功能时，你突然意识到，
 * 或许你其实不需要启动这么多潜艇上的功能。
 *
 * 第二个问题：**能够由 MONAD 接受的最小型号是多少？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Long {
        TODO()
    }

    // 第二个问题
    fun part2(input: List<String>): Long {
        TODO()
    }

    val testInput = readInput("day24_test")
//    check(part1(testInput) == )
//    check(part2(testInput) == )

    val input = readInput("day24")
//    check(part1(input) == )
//    check(part2(input) == )
}