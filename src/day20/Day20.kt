package day20

import util.readInput

/**
 * --- 第 20 天：海沟地图 ---
 *
 * 在扫描器全部部署完毕后，你就将它们的注意力全部转向绘制海沟底部的地图。
 *
 * 当你从扫描器获得返回的图像时，这些图像看起来就像是随机的噪声。或许你可以将输入的图像和图像增强算法相结合，使其更加清晰一些。
 *
 * 比如：
 *
 * ```
 * ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
 * #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
 * .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
 * .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
 * .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
 * ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
 * ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
 *
 * #..#.
 * #....
 * ##..#
 * ..#..
 * ..###
 * ```
 *
 * 上面的第一部分就是 **图像增强算法 (image enhancement algorithm)** 了。一般它们只会占据一整行，但这里为了便于阅读因此在这个例子中用多行分隔开。
 * 上面的第二部分就是 **输入图像 (input image)** ，由 **亮像素 (light pixels)** (`#`) 和 **暗像素(dark pixels)** (`.`) 组成的一个二维方阵。
 *
 * 图像增强算法描述了应该如何通过将输入图像中的所有像素，**同时的(simultaneously)** 转换成输出图像来予以增强。每个输出图像中的像素都是由以该像素为中心的一个 3x3
 * 的正方形来确定的。所以，如果要确定输出图像中位于 `(5,10)` 像素的值，需要从输入图像中考虑以下九个像素：`(4,9)`, `(4,10)`, `(4,11)`, `(5,9)`,
 * `(5,10)`, `(5,11)`, `(6,9)`, `(6,10)`, 和 `(6,11)` 。这九个输入像素将会合并成一个二进制数字，并用作 **图像增强算法** 字符串中的索引。
 *
 * 比如，要确定与输入图像中正中间像素对应的输出像素，由 `[...]` 标记范围内的像素都需要被予以考虑。
 *
 * ```
 * # . . # .
 * #[. . .].
 * #[# . .]#
 * .[. # .].
 * . . # # #
 * ```
 *
 * 从框住的左上角开始一行一行的读取，这些像素是 `...` ，然后是 `#..` ，再是 `.#.`; 将它们合并起来就会变成 `...#...#..` ，再将暗像素 (.) 变成 `0`,
 * 并把亮像素 (`#`) 变成 `1` ，就能得到二进制数字 `000100010` ，即十进制中的 `34` 。
 *
 * 图像增强算法的字符串长度正好为 512 字符长，可以完全覆盖 9 位二进制数字所有可能的情况。该字符串的头一些字符如下（数字从 0 开始）：
 *
 * ```
 * 0         10        20        30  34    40        50        60        70
 * |         |         |         |   |     |         |         |         |
 * ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
 * ```
 *
 * 在上面这组字符的中间，在索引 34 的位置可以找到字符：`#` 。所以，在输出图像中间的输出像素就应该是 `#` ，即一个 **亮像素**。
 *
 * 这个处理过程可以重复的执行来计算输出图像中的每一个像素。
 *
 * 随着图像技术的发展，在这里被操作的图像的大小是 **无限的 (infinite)** 。无限输出图像中的 **每个 (every)** 像素都需要基于输入图像的有关像素来精确的计算。
 * 你所拥有的小小的输入图像只是实际无限输入图像中的一小块区域；而无限输入图像中其余的部分则都由暗像素 (`.`) 组成。出于示例的目的，也为了节省空间，
 * 下面只展示无限输入和输出图像的一小部分。
 *
 * 因此，起始的输入图像，看起来像下面这样，在下面没有展示出来的则是向着无限远处不断延展的暗像素 (`.`)：
 *
 * ```
 * ...............
 * ...............
 * ...............
 * ...............
 * ...............
 * .....#..#......
 * .....#.........
 * .....##..#.....
 * .......#.......
 * .......###.....
 * ...............
 * ...............
 * ...............
 * ...............
 * ...............
 * ```
 *
 * 通过每次对每个像素同时的应用图像增强算法，可以获得下面这样的图像：
 *
 * ```
 * ...............
 * ...............
 * ...............
 * ...............
 * .....##.##.....
 * ....#..#.#.....
 * ....##.#..#....
 * ....####..#....
 * .....#..##.....
 * ......##..#....
 * .......#.#.....
 * ...............
 * ...............
 * ...............
 * ...............
 * ```
 *
 * 通过成像技术的进一步发展，上面的输出图像还可以被用作为一个输入图像！这使得它可以 **再一次 (a second time)** 进行增强：
 *
 * ```
 * ...............
 * ...............
 * ...............
 * ..........#....
 * ....#..#.#.....
 * ...#.#...###...
 * ...#...##.#....
 * ...#.....#.#...
 * ....#.#####....
 * .....#.#####...
 * ......##.##....
 * .......###.....
 * ...............
 * ...............
 * ...............
 * ```
 *
 * 真是难以置信 - 现在一些小细节真的开始显现出来了。在将原始的输入图像增强两次之后，有 **35** 个像素被点亮了。
 *
 * 第一个问题：从原始输入图像开始，并应用图像增强算法两次，请注意考虑到图像的大小是无限的。**在结果图像中有多少个像素被点亮了？**
 *
 * --- 第二部分 ---
 *
 * 你仍然不能很好的分辨出图像的细节。或许你只是 [增强(enhance)](https://en.wikipedia.org/wiki/Kernel_(image_processing))** 的还不够多。
 *
 * 如果你将上面示例中的图像增强总共 **50** 次，在最终的输出图像中就能够点亮 **3351** 个像素了。
 *
 * 第二个问题：从原始输入图像开始，并应用图像增强算法 50 次。**在结果图像中有多少个像素被点亮了？**
 */
fun main() {
    // 第一个问题
    fun part1(input: List<String>): Int {
        val iea = input[0]
        var image = input.drop(2)
            .map { it.toMutableList() }
            .toMutableList()
            .initNewPixels(true)
        repeat(2) {
            image = image.imageEnhancement(iea)
            image.initNewPixels(false)
        }
        return image.calcLitPixel()
    }

    // 第二个问题
    fun part2(input: List<String>): Int {
        val iea = input[0]
        var image = input.drop(2)
            .map { it.toMutableList() }
            .toMutableList()
            .initNewPixels(true)
        repeat(50) {
            image = image.imageEnhancement(iea)
            image.initNewPixels(false)
        }
        return image.calcLitPixel()
    }

    val testInput = readInput("day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("day20")
    check(part1(input) == 5229)
    check(part2(input) == 17009)
}

private typealias Image = MutableList<MutableList<Char>>

private fun Image.imageEnhancement(iea: String): Image {
    val rows: Image = mutableListOf()
    for (i in indices) {
        val columns = mutableListOf<Char>()
        for (j in this[i].indices) {
            val nineSquareBinary = buildString {
                append(topLeftValue(i, j))
                append(topValue(i, j))
                append(topRightValue(i, j))
                append(leftValue(i, j))
                append(thisValue(i, j))
                append(rightValue(i, j))
                append(bottomLeftValue(i, j))
                append(bottomValue(i, j))
                append(bottomRightValue(i, j))
            }
            columns.add(iea[nineSquareBinary.toInt(2)])
        }
        rows.add(columns)
    }
    return rows
}

private fun Image.initNewPixels(initDarkPixel: Boolean): Image {
    var infinite = '.'
    if (!initDarkPixel && this[0][0] == '#') infinite = '#'
    forEach {
        repeat(2) { _ ->
            it.add(0, infinite)
            it.add(infinite)
        }
    }
    repeat(2) { _ ->
        add(0, infinite.toString().repeat(this[0].size).toMutableList())
        add(infinite.toString().repeat(this[0].size).toMutableList())
    }
    return this
}

private fun Image.calcLitPixel(): Int {
    var sum = 0
    forEach { row -> sum += row.count { it == '#' } }
    return sum
}

private fun Image.topLeftValue(i: Int, j: Int): String {
    return if (i == 0 || j == 0) currentPixel(i, j)
    else if (this[i - 1][j - 1] == '#') "1" else "0"
}

private fun Image.topValue(i: Int, j: Int) =
    if (i == 0) currentPixel(i, j)
    else if (this[i - 1][j] == '#') "1" else "0"

private fun Image.topRightValue(i: Int, j: Int) =
    if (i == 0 || j == this[0].lastIndex) currentPixel(i, j)
    else if (this[i - 1][j + 1] == '#') "1" else "0"

private fun Image.leftValue(i: Int, j: Int) =
    if (j == 0) currentPixel(i, j)
    else if (this[i][j - 1] == '#') "1" else "0"

private fun Image.thisValue(i: Int, j: Int) =
    if (this[i][j] == '#') "1" else "0"

private fun Image.rightValue(i: Int, j: Int) =
    if (j == this[0].lastIndex) currentPixel(i, j)
    else if (this[i][j + 1] == '#') "1" else "0"

private fun Image.bottomLeftValue(i: Int, j: Int): String =
    if (i == this.lastIndex || j == 0) currentPixel(i, j)
    else if (this[i + 1][j - 1] == '#') "1" else "0"

private fun Image.bottomValue(i: Int, j: Int) =
    if (i == this.lastIndex) currentPixel(i, j)
    else if (this[i + 1][j] == '#') "1" else "0"

private fun Image.bottomRightValue(i: Int, j: Int): String =
    if (i == this.lastIndex || j == this[0].lastIndex) currentPixel(i, j)
    else if (this[i + 1][j + 1] == '#') "1" else "0"

private fun Image.currentPixel(i: Int, j: Int): String =
    if (this[i][j] == '#') "1"
    else if (this[i][j] == '.') "0"
    else error("impossible pixel.")