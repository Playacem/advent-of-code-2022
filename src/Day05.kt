object Day05 {
    override fun toString(): String {
        return this.javaClass.simpleName
    }

    data class Instruction(val numOfItems: Int, val fromRaw: Int, val toRaw: Int) {
        val fromIndex: Int get() = fromRaw - 1
        val toIndex: Int get() = toRaw - 1
    }

    fun String.toInstruction(): Instruction {
        val parts = this.split(' ', limit = 6)
        return Instruction(
            numOfItems = parts[1].toInt(radix = 10),
            fromRaw = parts[3].toInt(radix = 10),
            toRaw = parts[5].toInt(radix = 10)
        )
    }

    class StorageStack(val is9001: Boolean = false) {
        private val lines = ArrayDeque<String>()
        private val stacks: MutableList<ArrayDeque<String>> = mutableListOf()
        private var layoutDone = false

        fun loadData(lines: List<String>): StorageStack {
            lines.forEach(::readInputLine)
            return this
        }

        private fun readInputLine(line: String) {
            if (line.isBlank() && !layoutDone) {
                val numberOfElements = lines.removeLast().split(' ').last { it.isNotBlank() }.toInt()
                repeat(numberOfElements) {
                    stacks.add(ArrayDeque())
                }
                lines.reversed().forEach {
                    it.chunked(4) { chars ->
                        chars.trim().removePrefix("[").removeSuffix("]")
                    }.forEachIndexed { index, charSequence ->
                        if (charSequence.isNotBlank()) {
                            stacks[index].addFirst(charSequence.toString())
                        }
                    }
                }
                layoutDone = true
                return
            }
            if (!layoutDone) {
                lines.add(line)
                return
            }
            line.toInstruction().apply {
                if (is9001) {
                    applyInstruction9001(this)
                } else {
                    applyInstruction9000(this)
                }
            }
        }

        private fun applyInstruction9000(instruction: Instruction) {
            repeat(instruction.numOfItems) {
                val tmp = stacks[instruction.fromIndex].removeFirst()
                stacks[instruction.toIndex].addFirst(tmp)
            }
        }

        private fun applyInstruction9001(instruction: Instruction) {
            val tmp: ArrayDeque<String> = ArrayDeque()
            repeat(instruction.numOfItems) {
                stacks[instruction.fromIndex].removeFirst().apply { tmp.addLast(this) }
            }
            stacks[instruction.toIndex].addAll(0, tmp)
        }

        fun displayStacks(): StorageStack {
            println("-".repeat(15))
            stacks.forEachIndexed { index, strings ->
                println("${index + 1}: $strings")
            }
            println("-".repeat(15))
            return this
        }

        fun calculateMessage(): String {
            return stacks.joinToString(prefix = "", postfix = "", separator = "") { it.first() }
        }
    }
}

fun main() {


    fun part1(input: List<String>): String {
        return Day05.StorageStack().loadData(input).displayStacks().calculateMessage()
    }

    fun part2(input: List<String>): String {
        return Day05.StorageStack(is9001 = true).loadData(input).displayStacks().calculateMessage()
    }

    val testInput = readInput("${Day05}_test")
    val input = readInput(Day05.toString())

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}
