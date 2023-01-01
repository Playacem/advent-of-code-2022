package aoc2022

import utils.readInput

fun main() {
    class Container {

        private val data: MutableList<Long> = mutableListOf()
        private var counter: Long = 0L

        fun load(input: List<String>): Container {
            input.forEach { addLine(it) }
            return this
        }
        fun addLine(line: String) {
            if (line.isBlank()) {
                data.add(counter)
                counter = 0
                return
            }
            counter += line.trim().toLong()
        }

        fun topValue(): Long {
            return data.toList().max()
        }

        fun topThreeTotal(): Long {
            return data.toList().sortedDescending().take(3).sum()
        }
    }

    fun part1(input: List<String>): Long {
        return Container().load(input).topValue()
    }

    fun part2(input: List<String>): Long {
        return Container().load(input).topThreeTotal()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2022, "Day01_test")
    check(part1(testInput) == 24000L)
    check(part2(testInput) == 45000L)

    val input = readInput(2022, "Day01")
    println(part1(input))
    println(part2(input))
}
