package aoc2022

import utils.createDebug
import utils.readInput
import java.util.*

object Day07 {
    sealed class ElfPath(val name: String, val size: Long, val children: MutableMap<String, ElfPath>) {
        abstract fun recursiveSize(): Long

        abstract fun recursiveDirectories(): List<ElfDirectory>
    }

    class ElfDirectory(name: String, children: MutableMap<String, ElfPath>) : ElfPath(name, 0L, children) {
        override fun recursiveSize(): Long {
            return children.values.sumOf { it.recursiveSize() }
        }

        override fun recursiveDirectories(): List<ElfDirectory> {
            return listOf(this) + children.values.filterIsInstance<ElfDirectory>().flatMap { it.recursiveDirectories() }
        }

        override fun toString(): String {
            return "$name (dir)"
        }

    }

    class ElfFile(name: String, size: Long) : ElfPath(name, size, mutableMapOf()) {

        override fun recursiveSize(): Long {
            return size
        }
        override fun recursiveDirectories(): List<ElfDirectory> {
            return emptyList()
        }

        override fun toString(): String {
            return "$name (file) $size"
        }
    }

    class Parser {
        val rootDir: ElfPath = ElfDirectory("/", mutableMapOf())
        private val stack: Deque<ElfDirectory> = ArrayDeque()

        fun loadInput(list: List<String>): Parser {
            list.forEach { readLine(it) }
            return this
        }

        fun readLine(line: String) {
            if (line.startsWith("$ ")) {
                handleCommand(line.removePrefix("$ "))
                return
            }
            val (sizeOrDir, pathName) = line.split(' ', limit = 2)

            stack.peekLast().children[pathName] = if (sizeOrDir == "dir") {
                ElfDirectory(pathName, mutableMapOf())
            } else {
                ElfFile(pathName, sizeOrDir.toLong())
            }
        }

        private fun handleCommand(command: String) {
            if (command == "ls") {
                return
            }
            val (_, dir) = command.split(' ', limit = 2)
            when (dir) {
                ".." -> {
                    stack.removeLast()
                }
                "/" -> {
                    stack.addLast(rootDir as ElfDirectory)
                }
                else -> {
                    val currentDir = stack.peekLast()
                    stack.addLast(currentDir.children[dir] as? ElfDirectory ?: error("not a directory: $dir"))
                }
            }
        }
    }
}

fun main() {
    val (year, day) = 2022 to "Day07"

    fun part1(input: List<String>, debugActive: Boolean = false): Long {
        val debug = createDebug(debugActive)
        val parser = Day07.Parser().loadInput(input)
        val recursiveDirectories = parser.rootDir.recursiveDirectories()
        recursiveDirectories.forEach {
            debug {
                "$it: ${it.recursiveSize()}"
            }
        }
        return recursiveDirectories.filter { it.recursiveSize() <= 100_000L }.sumOf { it.recursiveSize() }
    }

    fun part2(input: List<String>, debugActive: Boolean = false): Long {
        val debug = createDebug(debugActive)
        val parser = Day07.Parser().loadInput(input)

        val diskSize = 70_000_000L
        val requiredUnusedSpace = 30_000_000L
        val usedSpace = parser.rootDir.recursiveSize()
        val currentlyFree = diskSize - usedSpace

        val sizeToDelete = requiredUnusedSpace - currentlyFree
        val res = parser.rootDir.recursiveDirectories().filter { it.recursiveSize() >= sizeToDelete }.minOf { it.recursiveSize() }

        debug { "Result: $res" }

        return res
    }

    val testInput = readInput(year, "${day}_test")
    val input = readInput(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput, debugActive = true) == 95_437L)
    println(part1(input))

    check(part2(testInput, debugActive = true) == 24_933_642L)
    println(part2(input))
}
