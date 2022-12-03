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

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${DayX}_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput(DayX.toString())
    println(part1(input))
    println(part2(input))
}
