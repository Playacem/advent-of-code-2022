object DayX {
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("${DayX}_test")
    val input = readInput(DayX.toString())

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}
