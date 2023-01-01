package aoc2022

import utils.readInput
import kotlin.math.abs

private data class Pos(val x: Int, val y: Int)
private data class Day09Command(val direction: String, val times: Int)

private operator fun Pos.plus(pair: Pair<Int, Int>): Pos {
    val (plusX, plusY) = pair
    return this.copy(x = this.x + plusX, y = this.y + plusY)
}

private fun isTouching(head: Pos, tail: Pos): Boolean {
    val xDiff = abs(head.x - tail.x)
    val yDiff = abs(head.y - tail.y)
    return xDiff <= 1 && yDiff <= 1
}

private fun hasSameXLevel(head: Pos, tail: Pos): Boolean {
    return head.x - tail.x == 0
}

private fun hasSameYLevel(head: Pos, tail: Pos): Boolean {
    return head.y - tail.y == 0
}

private fun newTailPos(head: Pos, tail: Pos): Pos {
    if (isTouching(head, tail)) {
        return tail
    }
    if (hasSameXLevel(head, tail)) {
        return when (head.y - tail.y) {
            2 -> tail.copy(y = tail.y + 1)
            -2 -> tail.copy(y = tail.y - 1)
            else -> error("more than two difference y-wise! $head, $tail")
        }
    }
    if (hasSameYLevel(head, tail)) {
        return when (head.x - tail.x) {
            2 -> tail.copy(x = tail.x + 1)
            -2 -> tail.copy(x = tail.x - 1)
            else -> error("more than two difference x-wise! $head, $tail")
        }
    }

    val diffPos = Pos(head.x - tail.x, head.y - tail.y)
    return when {
        diffPos.x > 0 && diffPos.y > 0 -> tail + (1 to 1)
        diffPos.x < 0 && diffPos.y > 0 -> tail + (-1 to 1)
        diffPos.x > 0 && diffPos.y < 0 -> tail + (1 to -1)
        diffPos.x < 0 && diffPos.y < 0 -> tail + (-1 to -1)
        else -> error("weird difference $diffPos - $head, $tail")
    }
}

private fun newHeadPos(head: Pos, dir: String): Pos {
    return when (dir) {
        "U" -> head + (0 to 1)
        "D" -> head + (0 to -1)
        "L" -> head + (-1 to 0)
        "R" -> head + (1 to 0)
        else -> error("Unknown direction $dir")
    }
}

private fun parseInstruction(instruction: String): Day09Command {
    val (dir, times) = instruction.split(' ', limit = 2)
    return Day09Command(dir, times.toInt(radix = 10))
}

private class Snake {
    var head: Pos = Pos(0, 0)
    var tail: Pos = Pos(0, 0)
    private val visitedPositions: MutableSet<Pos> = mutableSetOf(tail)

    fun consume(instruction: Day09Command) {
        repeat(instruction.times) {
            head = newHeadPos(head, instruction.direction)
            tail = newTailPos(head, tail)
            visitedPositions += tail
        }
    }

    fun consume(instructions: List<Day09Command>) {
        instructions.forEach {
            consume(it)
        }
    }

    fun visited(): Int {
        return visitedPositions.size
    }
}

private class SnakeWithList(size: Int) {
    private var knots: List<Pos> = List(size) { Pos(0, 0) }
    private val visitedPositions: MutableSet<Pos> = mutableSetOf(knots.last())

    fun consume(instruction: Day09Command) {
        repeat(instruction.times) {
            val head = knots.first()
            val rest = knots.drop(1)

            val newHead = newHeadPos(head, instruction.direction)
            var left = newHead
            val newRest = rest.map { pos ->
                newTailPos(left, pos).also { newTail -> left = newTail }
            }
            knots = listOf(newHead, *newRest.toTypedArray())
            visitedPositions += knots.last()
        }
    }

    fun consume(instructions: List<Day09Command>) {
        instructions.forEach {
            consume(it)
        }
    }

    fun visited(): Int {
        return visitedPositions.size
    }
}

fun main() {
    val (year, day) = 2022 to "Day09"

    fun part1(input: List<String>): Int {
        return Snake().also { it.consume(input.map(::parseInstruction)) }.visited()
    }

    fun part1Alt(input: List<String>): Int {
        return SnakeWithList(2).also { it.consume(input.map(::parseInstruction)) }.visited()
    }

    fun part2(input: List<String>): Int {
        return SnakeWithList(10).also { it.consume(input.map(::parseInstruction)) }.visited()
    }

    val testInput = readInput(year, "${day}_test")
    val input = readInput(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 13)
    println(part1(input))

    check(part1Alt(testInput) == 13)

    println("====")
    check(part2(testInput) == 1)
    println("checking alternative test input")
    check(part2(readInput(year, "${day}_test_part2")) == 36)
    println("Done with test input")

    println(part2(input))
}
