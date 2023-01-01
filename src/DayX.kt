import utils.readInput

fun main() {
    val (year, day) = 2022 to "DayX"

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput(year = year, name = "${day}_test")
    val input = readInput(year = year,name = "${day}_test")

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == -1)
    println(part1(input))

    check(part2(testInput) == -1)
    println(part2(input))
}
