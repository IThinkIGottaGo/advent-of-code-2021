package day21

import util.readInput

/**
 * Day21 第二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [tginsberg](https://github.com/tginsberg/advent-2021-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2021/Day21.kt)
 * 的解答。
 *
 * 解法思路：首先在狄拉克骰子的问题中，由于每名玩家依然要投掷 3 次骰子，且每次骰子都会打开 3 个新的宇宙，且结果分别是 1，2 和 3。因此在某个玩家的一轮投掷中总计会出现 27
 * 种可能的结果（如从 `(1,1,1)`, `(1,1,2)`...`(3,3,3)` 为止）。所以一轮投掷出的需走步数范围也是从 3..9 之间，且在这之中由于有多种组合总计的步数是相同的，
 * quantumDieFrequency 变量写死存储了三次骰子投出的总和以及该总和占 27 种情况中有多少种情况。如 (3 to 1) 表示三次投掷总和为 3 的只有 1 种情况，
 * 即 (1,1,1)，而 (4 to 3) 则表示总和为 4 的有 3 种情况，即 (1,1,2) (1,2,1) (2,1,1) 三种投掷结果。这样就先将 27 种情况简化为 7 种。
 *
 * PlayerState 则是两名玩家各自拥有一个，用来存储该名玩家的此时所处在的位置，及其总分数。各个玩家的行动由 GameState 类管理。其中的 next
 * 方法中 place + die - 1 等效于将游戏中这个标记 1-10 格子的地图替换为了标记 0-9 格子的地图，从而便于通过 mod 10 即可将棋子越过 9 的时候重置为
 * 0-9 之间的值，但最后还得将 1 加回，这样 place 将依然处于原 1-10 地图上的位置，分数 score 也能加上原地图上标记的分数。每次改变某个玩家的位置和分数后都会返回一个新对象。
 *
 * GameState 则管理了两名玩家的 PlayerState，并在调用其中的 next 方法时，只需要传入当前骰子上的数值即可（这里传入的是 3 次投掷的总和，
 * 因为每个人都要投掷骰子三次，等效于投了一次但加和三次的结果），每次传入骰子上数值的时候都会根据当前投掷者的变量 player1Turn 来决定是修改玩家 1
 * 还是玩家 2 的位置及分数，并将该变量取反以便下一次作为另一个玩家的投掷情况来修改另一个玩家的位置和分数。非当前投掷玩家的位置和分数则直接保留，
 * 每次投掷都是返回一个新的对象并包含两名玩家最新的位置和分数。这个类中同样还有计算此时这个 GameState 中是否有哪名玩家率先抵达了胜出分数的方法 isWinner。
 *
 * 由于谜题中要求统计两名玩家整个的胜出次数，所以 WinCount 类用来存储两名玩家累计胜出的次数。并且为了便于将不同情况下得到的胜出次数累加在一起，
 * 提供了加法，和便于与出现的频度相乘，提供了乘法等操作符方法。
 *
 * playQuantum 最终采用递归的方式完成了整个过程，假如尚未出现胜出者，且当前的游戏状态，及其胜出情况未保存在 stateMemory 中，则递归执行本方法，
 * 从而反复的遍历 3、4、5、6.. 等骰子的总和，并且每次投掷调用 next 方法都会生成一个新的 GameState，从而每次递归前的 GameState 各自独立保留。
 *
 * 如第一种情况，情况两人不断投出 3，假设此时玩家 1 先胜出，而后递归的方法分支进入到 when 的第一条分支中，得到玩家 1 胜出一次的计数对象，
 * 再返回到调用者 playQuantum 方法中，将这个返回胜出的次数乘以对应的频度，从而作为 map 转换方法的第一个结果，即两名玩家从头到最后一次前都投出 3；
 * 接着最后一步掷出 3 或 4、5、6...9 胜出，再回到最后一步之前的 state，并继续投掷 4、5、6、7..9 的骰子，如此往复直到此轮 map 消耗完毕；
 * map 中最后获得了一系列最后一轮投出 3、4、5、6..9 的双方胜出次数 WinCounts，再通过 reduce 方法将其加和，并将这个 GameState
 * 下若继续下去的胜出情况存储起来，从而在遇到相同状态下的时候复用结果，从而免于继续计算。最终整个 WinCounts 是由无数小步骤中的 WinCounts
 * （玩家 1 和玩家 2 各个胜出的次数总和）整合起来的结果，该结果便是第二个问题的答案。
 */
fun main() {
    fun part2(input: List<String>): Long {
        val player1Start = input.first().substringAfterLast(" ").toInt()
        val player2Start = input.last().substringAfterLast(" ").toInt()
        return playQuantum(
            GameState(PlayerState(player1Start), PlayerState(player2Start))
        ).max()
    }

    val testInput = readInput("day21_test")
    check(part2(testInput) == 444356092776315L)

    val input = readInput("day21")
    check(part2(input) == 91559198282731L)
}

private val quantumDieFrequency = mapOf<Int, Long>(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
private val stateMemory = mutableMapOf<GameState, WinCounts>()

private fun playQuantum(state: GameState): WinCounts =
    when {
        state.isWinner(21) ->
            if (state.player1.score > state.player2.score) WinCounts(1, 0) else WinCounts(0, 1)
        state in stateMemory ->
            stateMemory.getValue(state)
        else -> quantumDieFrequency.map { (die, frequency) ->
            playQuantum(state.next(die)) * frequency
        }.reduce { a, b -> a + b }.also { stateMemory[state] = it }
    }

@JvmRecord
private data class WinCounts(val player1: Long, val player2: Long) {
    operator fun plus(other: WinCounts) = WinCounts(player1 + other.player1, player2 + other.player2)

    operator fun times(other: Long) = WinCounts(player1 * other, player2 * other)

    fun max(): Long = maxOf(player1, player2)
}

@JvmRecord
private data class GameState(val player1: PlayerState, val player2: PlayerState, val player1Turn: Boolean = true) {
    fun next(die: Int) = GameState(
        if (player1Turn) player1.next(die) else player1,
        if (!player1Turn) player2.next(die) else player2,
        player1Turn = !player1Turn
    )

    fun isWinner(scoreNeeded: Int = 1000): Boolean = player1.score >= scoreNeeded || player2.score >= scoreNeeded
}

@JvmRecord
private data class PlayerState(val place: Int, val score: Int = 0) {
    fun next(die: Int): PlayerState {
        val nextPlace = (place + die - 1) % 10 + 1
        return PlayerState(
            place = nextPlace,
            score = score + nextPlace
        )
    }
}