package aoc2022

import aoc2022.Day08.calculateScore
import aoc2022.Day08.isNotVisible
import utils.createDebug
import utils.readInput

object Day08 {
    data class TreeView(
        val value: Int,
        val left: List<Int>,
        val right: List<Int>,
        val top: List<Int>,
        val bottom: List<Int>,
        val rowIndex: Int,
        val columnIndex: Int
    )

    fun TreeView.isNotVisible(): Boolean {
        return left.any { it >= value } &&
                right.any { it >= value } &&
                top.any { it >= value } &&
                bottom.any { it >= value }
    }

    // like takeWhile with first predicate match
    inline fun <T> Iterable<T>.takeDoWhile(predicate: (T) -> Boolean): List<T> {
        val list = ArrayList<T>()
        for (item in this) {
            list.add(item)
            if (!predicate(item))
                break
        }
        return list
    }

    fun TreeView.calculateScore(): Int {
        val rightView = right.takeDoWhile { it < value }.count()
        val leftView = left.reversed().takeDoWhile { it < value }.count()
        val bottomView = bottom.takeDoWhile { it < value }.count()
        val topView = top.reversed().takeDoWhile { it < value }.count()

        return rightView * leftView * bottomView * topView
    }
}
fun main() {
    val (year, day) = 2022 to "Day08"

    fun List<List<Int>>.getColumnValues(index: Int): List<Int> {
        return this.map { it[index] }
    }

    fun List<List<Int>>.getRowValues(index: Int): List<Int> {
        return this[index]
    }

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.chunked(1) {
                it.toString().toInt(radix = 10)
            }
        }
    }

    fun List<List<Int>>.asTreeView(row: Int, column: Int): Day08.TreeView {
        val value = this[row][column]

        val rowValues = this.getRowValues(row)
        val columnValues = this.getColumnValues(column)

        val leftOfTarget = rowValues.subList(0, column)
        val rightOfTarget = rowValues.subList(column + 1, rowValues.size)
        val topOfTarget = columnValues.subList(0, row)
        val bottomOfTarget = columnValues.subList(row + 1, columnValues.size)

        return Day08.TreeView(
            value = value,
            left = leftOfTarget,
            right = rightOfTarget,
            top = topOfTarget,
            bottom = bottomOfTarget,
            rowIndex = row,
            columnIndex = column
        )
    }



    fun List<List<Int>>.isVisible(row: Int, column: Int): Boolean {
        fun isNotVisible(): Boolean {
            return asTreeView(row, column).isNotVisible()
        }


        return when {
            row == 0 -> true
            column == 0 -> true
            row == this.size - 1 -> true
            column == this[row].size - 1 -> true
            else -> isNotVisible().not()
        }

    }

    fun part1(input: List<String>, debugActive: Boolean = false): Int {
        val debug = createDebug(debugActive)
        val parsed = parseInput(input).also { debug { "$it" } }

        val res = parsed.mapIndexed { rowIndex, rows ->
            List(rows.size) { columnIndex ->
                parsed.isVisible(rowIndex, columnIndex)
            }
        }

        return res.flatten().count { it }
    }

    fun part2(input: List<String>, debugActive: Boolean = false): Int {
        val debug = createDebug(debugActive)
        val parsed = parseInput(input).also { debug { "$it" } }

        val res = parsed.mapIndexed { rowIndex, rows ->
            List(rows.size) { columnIndex ->
                parsed.asTreeView(rowIndex, columnIndex)
            }
        }

        return res.flatten().maxOf { it.calculateScore().also { score -> debug { "$it: $score" } } }
    }

    val testInput = readInput(year, "${day}_test")
    val input = readInput(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput, debugActive = true) == 21)
    println(part1(input))

    check(part2(testInput, debugActive = true) == 8)
    println(part2(input))
}
