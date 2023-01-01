package aoc2022

import utils.readInputText

object Day13 {

    fun parseInput(text: String): List<List<String>> = text.split("\n\n").map { it.trim().split("\n") }

    sealed class Structure : Comparable<Structure> {
        data class AList(val list: List<Structure>) : Structure() {
            override fun compareTo(other: Structure): Int {
                if (other is I) {
                    return this.compareTo(other.toList())
                } else {
                    val otherAList = other as AList
                    var i = 0
                    while(i < this.list.size && i < otherAList.list.size) {
                        val cmp = this.list[i].compareTo(otherAList.list[i])
                        if (cmp != 0) {
                            return cmp
                        }
                        i++
                    }
                    return otherAList.list.size - this.list.size
                }
            }

            override fun toString(): String = list.toString()
        }

        data class I(val value: Int) : Structure() {
            fun toList() = AList(list = listOf(this))
            override fun compareTo(other: Structure): Int {
                return if (other is I) {
                    other.value - this.value
                } else {
                    this.toList().compareTo(other)
                }
            }

            override fun toString(): String = value.toString()
        }

    }

    fun stringAsStructure(line: String): Structure {
        val stack = ArrayDeque<Structure>()
        stack.addLast(Structure.AList(listOf()))

        var numberString = ""
        fun addIntToStack() {
            val number = numberString.toIntOrNull()
            if (number != null) {
                val top = stack.removeLast() as Structure.AList
                stack.addLast(Structure.AList(top.list + Structure.I(number)))
            }
            numberString = ""
        }

        line.forEach { c ->
            when(c) {
                '[' -> {
                    addIntToStack()
                    stack.addLast(Structure.AList(listOf()))
                }
                ']' -> {
                    addIntToStack()
                    val top = stack.removeLast()
                    val next = stack.removeLast() as Structure.AList
                    stack.addLast(Structure.AList(list = next.list + top))
                }
                ',' -> {
                    addIntToStack()
                }
                else -> {
                    numberString += c
                }
            }
        }

        return stack.removeLast()
    }
}

fun main() {
    val (year, day) = 2022 to "Day13"

    fun part1(input: String): Int {
        val parsed = Day13.parseInput(input)

        var sum = 0
        parsed.forEachIndexed { index, strings ->
            val (left, right) = strings.map(Day13::stringAsStructure)
            val compResult = left.compareTo(right)
            if (compResult > 0) {
                sum += (index + 1)
            }
        }

        return sum
    }

    fun part2(input: String): Int {
        val decoderKey1 = Day13.stringAsStructure("[[2]]")
        val decoderKey2 = Day13.stringAsStructure("[[6]]")
        val parsed = Day13.parseInput(input)
        val structures = parsed.flatMap { it.map(Day13::stringAsStructure) } + decoderKey1 + decoderKey2

        val sorted = structures.sortedWith { left, right ->
            val res = left.compareTo(right)
            res * -1
        }

        val left = sorted.indexOf(decoderKey1) + 1
        val right = sorted.indexOf(decoderKey2) + 1
        return left * right
    }

    val testInput = readInputText(year, "${day}_test")
    val input = readInputText(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 140)
    println(part2(input))
}
