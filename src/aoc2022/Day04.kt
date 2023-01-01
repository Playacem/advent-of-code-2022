package aoc2022

import utils.readInput

object Day04 {
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}

fun main() {
    fun rangeStringAsIntRange(rangeString: String): IntRange {
        val (left, right) = rangeString.split('-')
        return IntRange(left.toInt(10), right.toInt(10))
    }

    fun inputAsRanges(input: String): List<IntRange> {
        return input.split(',').map { rangeStringAsIntRange(it) }
    }

    fun totalOverlap(list: List<IntRange>): Boolean {
        val (a, b) = list
        return (b.first in a && b.last in a) || (a.first in b && a.last in b)
    }

    fun partialOverlap(list: List<IntRange>): Boolean {
        val (a, b) = list
        for (x in a) {
            if (x in b) {
                return true
            }
        }

        for (x in b) {
            if (x in a) {
                return true
            }
        }
        return false
    }

    fun part1(input: List<String>): Int {
        return input.map { inputAsRanges(it) }.count { totalOverlap(it) }
    }

    fun part2(input: List<String>): Int {
        return input.map { inputAsRanges(it) }.count { partialOverlap(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2022, "${Day04}_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput(2022, Day04.toString())
    println(part1(input))
    println(part2(input))
}
