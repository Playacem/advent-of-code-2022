object Day06 {
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}

fun main() {
    fun findIndexWithPrecedingNUniqueValues(input: String, n: Int): Int {
        val res = input.windowed(size = n + 1, step = 1).find {
            it.toCharArray().dropLast(1).toSet().size == n
        } ?: error("not found")

        val index = input.indexOf(res)
        println("DEBUG: $res - $index + $n = ${index + n} ")
        return index + n
    }

    fun part1(list: List<String>): Int {
        val input = list.first()
        return findIndexWithPrecedingNUniqueValues(input, 4)
    }

    fun part2(list: List<String>): Int {
        val input = list.first()
        return findIndexWithPrecedingNUniqueValues(input, 14)
    }

    val testInput01 = readInput("${Day06}_test01")
    val testInput02 = readInput("${Day06}_test02")
    val testInput03 = readInput("${Day06}_test03")
    val testInput04 = readInput("${Day06}_test04")
    val testInput05 = readInput("${Day06}_test05")
    val input = readInput(Day06.toString())

    // test if implementation meets criteria from the description, like:
    check(part1(testInput01) == 7) { "check 1 part 1" }
    check(part1(testInput02) == 5) { "check 2 part 1" }
    check(part1(testInput03) == 6) { "check 3 part 1" }
    check(part1(testInput04) == 10) { "check 4 part 1" }
    check(part1(testInput05) == 11) { "check 5 part 1" }
    println(part1(input))

    check(part2(testInput01) == 19) { "check 1 part 2" }
    check(part2(testInput02) == 23) { "check 2 part 2" }
    check(part2(testInput03) == 23) { "check 3 part 2" }
    check(part2(testInput04) == 29) { "check 4 part 2" }
    check(part2(testInput05) == 26) { "check 5 part 2" }
    println(part2(input))
}
