package aoc2022

import utils.readInput
import kotlin.math.abs

private sealed class Instruction {
    object Noop : Instruction()
    class Add(val modifier: Int): Instruction()
}

private fun createInstruction(input: String): Instruction {
    return if (input == "noop") {
        Instruction.Noop
    } else {
        Instruction.Add(input.split(' ')[1].toInt(10))
    }
}

private fun List<Int>.signalStrengthAt(index: Int): Int {
    return index * this[index]
}

private fun List<Int>.signalStrengthAt(indexes: List<Int>): Int {
    return indexes.sumOf { this.signalStrengthAt(it) }
}

private fun List<Int>.renderAsSingleString(): String {
    return this.mapIndexed { cycle, spritePos ->
        if (abs(spritePos - (cycle % 40)) <= 1) {
            "#"
        } else {
            "."
        }
    }.joinToString(separator = "")
}

fun main() {
    val (year, day) = 2022 to "Day10"

    fun cycleData(input: List<String>): List<Int> {
        var current = 1
        val data = mutableListOf(current)

        input.map(::createInstruction).forEach {
            when(it) {
                is Instruction.Noop -> {
                    data.add(current)
                }
                is Instruction.Add -> {
                    data.add(current)
                    data.add(current)
                    current += it.modifier
                }
            }
        }
        data.add(current)

        return data
    }

    fun part1(input: List<String>): Int {
        return cycleData(input).signalStrengthAt(listOf(20, 60, 100, 140, 180, 220))
    }

    fun part2(input: List<String>): List<String> {
        val renderedString = cycleData(input).drop(1).renderAsSingleString()
        return renderedString.chunked(40).dropLast(1)
    }

    val testInput = readInput(year, "${day}_test")
    val input = readInput(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 13140)
    println(part1(input))

    val testPart2Result = part2(testInput)

    check(testPart2Result.size == 6) { "not exactly 6 rows, instead we got ${testPart2Result.size} rows" }
    check(testPart2Result.all { it.length == 40 }) { "not every row has 40 pixels" }
    testPart2Result.forEach { println(it) }
    println("====")
    part2(input).forEach { println(it) }
}
