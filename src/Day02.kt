enum class RPS(val opponentLabel: String, val ourLabel: String, val selectionValue: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSOR("C", "Z", 3);

    fun match(s: String): Boolean {
        return s == opponentLabel || s == ourLabel
    }
}

fun main() {
    data class Match(val opponent: RPS, val self: RPS)

    val rpsValues = RPS.values().toList()

    fun parseDecision(s: String): RPS {
        return RPS.values().find { it.match(s) } ?: error("Invalid string: '$s'")
    }

    fun String.asRPS() = parseDecision(this)
    fun Char.asRPS() = parseDecision(this.toString())

    fun calculateMatchValue(match: Match): Int {
        val (opponent, self) = match
        if (opponent == self) {
            return 3
        }
        val posDifference = self.ordinal - opponent.ordinal
        if (posDifference in listOf(1, -2)) {
            return 6
        }
        return 0
    }

    fun String.asMatch(): Match {
        val (o, s) = this.split(" ", limit = 2).map { parseDecision(it) }
        return Match(o, s)
    }

    fun Match.asTotalValue(): Int {
        return calculateMatchValue(this) + self.selectionValue
    }

    fun String.outcomeAsMatchString(): String {
        val outcome = this.last()
        val o = this.first()
        return when(outcome) {
            'X' -> {
                val losingRps = rpsValues[(o.asRPS().ordinal - 1 + rpsValues.size) % rpsValues.size]
                "$o ${losingRps.ourLabel}"
            }
            'Y' -> "$o $o"
            'Z' -> {
                val winningRps = rpsValues[(o.asRPS().ordinal + 1 + rpsValues.size) % rpsValues.size]
                "$o ${winningRps.ourLabel}"
            }
            else -> error("Invalid outcome: '$outcome'")
        }
    }
    fun part1(input: List<String>): Int {
        return input.map { it.asMatch() }.sumOf { it.asTotalValue() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.outcomeAsMatchString() }.map { it.asMatch() }.sumOf { it.asTotalValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
