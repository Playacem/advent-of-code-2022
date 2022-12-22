typealias WorryLevel = Long
fun String.toWorryLevelOrNull(): WorryLevel? = toLongOrNull()
infix fun WorryLevel.divisibleBy(divisor: Int) = this % divisor == 0L

private class Monkey(
    val name: String,
    val items: MutableList<WorryLevel>,
    val operation: (WorryLevel) -> WorryLevel,
    val testDivisor: Int,
    val targetMonkeyIfTrue: Int,
    val targetMonkeyIfFalse: Int,
    var itemsInspected: Long = 0
)

private fun Monkey.test(a: WorryLevel): Boolean {
    return a divisibleBy testDivisor
}

private class MonkeyBuilder {
    var name: String = ""
    val items: MutableList<WorryLevel> = mutableListOf()
    var operation: (WorryLevel) -> WorryLevel = { it }
    var testDivisor: Int = -1
    var targetMonkeyIfTrue: Int = -1
    var targetMonkeyIfFalse: Int = -1
}

private fun createOperation(s: String): (WorryLevel) -> WorryLevel {
    val (_, operator, op2) = s.split(" ")
    return when(operator) {
        "+" -> { old -> old + (op2.toWorryLevelOrNull() ?: old) }
        "*" -> { old -> old * (op2.toWorryLevelOrNull() ?: old) }
        else -> error("Unknown operator: $operator")
    }
}

private fun createMonkey(input: String): Monkey {
    val lines = input.split("\n")
    val builder = MonkeyBuilder()

    lines.map { it.trim() }.forEach { line ->
        when {
            line.startsWith("Monkey") -> builder.name = line.removeSuffix(":")
            line.startsWith("Starting items:") -> builder.items.addAll(
                line.split(':')[1].split(", ").map { it.trim().toWorryLevelOrNull() ?: error("") }
            )

            line.startsWith("Operation:") -> builder.operation = createOperation(line.substringAfter("new = "))

            line.startsWith("Test:") -> Regex("""^Test: divisible by (\d+)$""").find(line)?.let {
                builder.testDivisor = it.groupValues[1].toInt()
            } ?: error("no regex match for test!")

            line.startsWith("If ") -> Regex("""^If (\w+): throw to monkey (\d+)$""").find(line)?.let {
                val trueFalse = it.groupValues[1]
                val target = it.groupValues[2].toInt()
                when (trueFalse) {
                    "true" -> builder.targetMonkeyIfTrue = target
                    "false" -> builder.targetMonkeyIfFalse = target
                    else -> error("Unknown if value: $trueFalse")
                }
            } ?: error("no regex match for if!")

            else -> error("Unhandled line: $line")
        }
    }

    return Monkey(
        name = builder.name,
        items = builder.items.toMutableList(),
        operation = builder.operation,
        testDivisor = builder.testDivisor,
        targetMonkeyIfTrue = builder.targetMonkeyIfTrue,
        targetMonkeyIfFalse = builder.targetMonkeyIfFalse
    )
}

private fun monkeys(input: List<String>): List<Monkey> {
    return input.map { createMonkey(it) }
}

private fun playRound(monkeys: List<Monkey>, worryReductionActive: Boolean, commonModulus: Int) {
    monkeys.forEach { monkey ->
        while (monkey.items.isNotEmpty()) {
            val item = monkey.items.removeFirst()
            monkey.itemsInspected += 1
            // worry increase due to inspection
            val worriedItem = monkey.operation(item)
            // monkey is done / divide by 3
            val boredItem = if (worryReductionActive) {
                worriedItem / 3
            } else {
                worriedItem % commonModulus
            }
            val testResult = monkey.test(boredItem)
            val targetIndex = if (testResult) {
                monkey.targetMonkeyIfTrue
            } else {
                monkey.targetMonkeyIfFalse
            }
            monkeys[targetIndex].items.add(boredItem)
        }
    }
}

private fun printMonkeyStatus(monkeys: List<Monkey>) {
    monkeys.forEach {
        println("${it.name}: ${it.items} [inspected: ${it.itemsInspected}]")
    }
}

fun main() {
    val day = "Day11"

    fun part1(input: List<String>): Long {
        val monkeys = monkeys(input)
        repeat(20) {
            playRound(monkeys, true, 1)
        }
        printMonkeyStatus(monkeys)

        return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val monkeys = monkeys(input)
        val commonModulus = monkeys.map { it.testDivisor }.lcm().toInt()
        repeat(10000) {
            playRound(monkeys, false, commonModulus)
            println("Round ${it + 1} over")
        }
        printMonkeyStatus(monkeys)

        return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }

    val testInput = readInputText("${day}_test").split("\n\n")
    val input = readInputText(day).split("\n\n")

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 10605L)
    println(part1(input))

    println("====")
    check(part2(testInput) == 2_713_310_158L)
    println(part2(input))
}
