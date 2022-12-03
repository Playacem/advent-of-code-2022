object Day03 {
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
fun main() {
    fun CharRange.asLongString(): String = this.toList().joinToString(separator = "", prefix = "", postfix = "")

    val alphabet: String = ('a'..'z').asLongString() + ('A'..'Z').asLongString()

    fun splitInParts(string: String): List<String> {
        return listOf(string.substring(0, string.length / 2), string.substring(string.length / 2))
    }

    fun findCommonCharacter(list: List<String>): Char {
        val commonChars = list.map { it.toCharArray().toSet() }.reduce { acc, chars -> acc.intersect(chars) }
        check(commonChars.size == 1) { "Unexpected size: ${commonChars.size} - $commonChars" }
        return commonChars.first()
    }

    fun getPriorityOfItem(item: Char): Int {
        return alphabet.indexOf(item) + 1
    }

    fun part1(input: List<String>): Int {
        return input.map { splitInParts(it) }.map { findCommonCharacter(it) }.sumOf { getPriorityOfItem(it) }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).map { findCommonCharacter(it) }.sumOf { getPriorityOfItem(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${Day03}_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput(Day03.toString())
    println(part1(input))
    println(part2(input))
}
