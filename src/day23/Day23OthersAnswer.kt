package day23

import util.readInput
import java.util.*
import kotlin.math.abs

/**
 * Day23 第一、二部分的解法来自 Kotlin Slack 频道 advent-of-code 中 [dfings](https://github.com/dfings/advent-of-code/blob/main/src/2021/problem_23.main.kts)
 * 的解答。
 */
fun main() {
    fun part1(input: List<String>): Int {
        val amphipods = input.drop(2).dropLast(1).flatMapIndexed { index: Int, value: String ->
            val (a, b, c, d) = regex.find(value)!!.destructured
            listOf(
                Amphipod(a.toType(), Point(2, 1 + index)), Amphipod(b.toType(), Point(4, 1 + index)),
                Amphipod(c.toType(), Point(6, 1 + index)), Amphipod(d.toType(), Point(8, 1 + index))
            )
        }
        val solution = solve(State(amphipods, 0))
        return solution.totalEnergyCost
    }

    fun part2(input: List<String>): Int = part1(input)

    val testInput1 = readInput("day23_test1")
    check(part1(testInput1) == 12521)
    val testInput2 = readInput("day23_test2")
    check(part2(testInput2) == 44169)

    val input1 = readInput("day23_1")
    check(part1(input1) == 15516)
    val input2 = readInput("day23_2")
    check(part2(input2) == 45272)
}

private val regex = Regex(".*([ABCD]).*([ABCD]).*([ABCD]).*([ABCD])")

private val hallway = listOf(Point(0, 0), Point(1, 0), Point(3, 0), Point(5, 0), Point(7, 0), Point(9, 0), Point(10, 0))

private fun solve(initialState: State): Solution {
    val slotsPerRoom = initialState.amphipods.size / 4
    val frontier = PriorityQueue<State> { a, b ->
        a.totalEnergyCost.compareTo(b.totalEnergyCost)
    }
    frontier += initialState
    val seen = mutableSetOf<List<Amphipod>>()
    var maxFrontierSize = 0
    while (!frontier.isEmpty()) {
        if (frontier.size > maxFrontierSize) maxFrontierSize = frontier.size
        val state = frontier.poll()
        when {
            state.amphipods in seen -> {}
            state.done() -> return Solution(state.totalEnergyCost, seen.size, maxFrontierSize)
            else -> {
                seen += state.amphipods
                state.successor(slotsPerRoom).forEach { frontier += it }
            }
        }
    }
    error("no solution!")
}

private fun Point.manhattanDistance(p: Point) = abs(x - p.x) + abs(y - p.y)

private fun State.move(amphipod: Amphipod, to: Point) = State(
    amphipods.toMutableList().apply {
        this[indexOf(amphipod)] = amphipod.copy(p = to)
        sortBy { it.hashCode() } // Need some consistent order for dedupe purposes.
    },
    totalEnergyCost + amphipod.type.cost * amphipod.p.manhattanDistance(to)
)

private fun State.done() = amphipods.none { it.p.x != it.type.roomX }

private fun String.toType() = Type.values().single { this == it.code }

private fun State.successor(slotsPerRoom: Int) = sequence {
    fun Amphipod.roomOnlyHasCorrectTypes() = amphipods.none { it.p.x == type.roomX && it.type != type }
    fun Amphipod.shouldStayPut() = p.x == type.roomX && roomOnlyHasCorrectTypes()
    fun Amphipod.canMoveThroughHall(x: Int) =
        p.x < x && amphipods.none { it.p.y == 0 && it.p.x > p.x && it.p.x <= x } ||
                (p.x > x && amphipods.none { it.p.y == 0 && it.p.x < p.x && it.p.x >= x })

    fun Amphipod.canMoveToHall() =
        p.y > 0 && amphipods.none { p.x == it.p.x && p.y > it.p.y }

    fun Amphipod.canMoveToRoom() =
        p.y == 0 && canMoveThroughHall(type.roomX) && roomOnlyHasCorrectTypes()

    fun Amphipod.firstOpenSlotInRoom(): Point {
        var minOccupiedY = slotsPerRoom + 1
        amphipods.forEach { if (it.p.x == type.roomX && it.p.y < minOccupiedY) minOccupiedY = it.p.y }
        return Point(type.roomX, minOccupiedY - 1)
    }

    amphipods.forEach { amphipod ->
        when {
            amphipod.shouldStayPut() -> return@forEach
            amphipod.canMoveToHall() -> hallway.forEach {
                if (amphipod.canMoveThroughHall(it.x)) yield(
                    move(
                        amphipod,
                        it
                    )
                )
            }
            amphipod.canMoveToRoom() -> yield(move(amphipod, amphipod.firstOpenSlotInRoom()))
            else -> return@forEach
        }
    }
}

private enum class Type(val code: String, val roomX: Int, val cost: Int) {
    AMBER("A", 2, 1),
    BRONZE("B", 4, 10),
    COPPER("C", 6, 100),
    DESERT("D", 8, 1000),
}

@JvmRecord
private data class Point(val x: Int, val y: Int)

@JvmRecord
private data class Amphipod(val type: Type, val p: Point)

@JvmRecord
private data class State(val amphipods: List<Amphipod>, val totalEnergyCost: Int)

@JvmRecord
private data class Solution(val totalEnergyCost: Int, val statesExplored: Int, val maxFrontierSize: Int)