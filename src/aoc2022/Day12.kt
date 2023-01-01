package aoc2022

import utils.readInput
import java.util.*
import kotlin.math.abs

private object Day12 {

    data class Pos(val x: Int, val y: Int)

    data class ElevationMap(val elevation: Map<Pos, Int>, val startPos: Pos, val goalPos: Pos, val maxX: Int, val maxY: Int)

    fun reconstructPath(cameFrom: Map<Pos, Pos>, current: Pos): List<Pos> {
        val totalPath = mutableListOf(current)
        var tmp = current
        while (tmp in cameFrom.keys) {
            tmp = cameFrom[tmp]!!
            totalPath.add(0, tmp)
        }

        return totalPath
    }

    /**
     * Based on [the Wikipedia pseudo code for A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode)
     */
    fun aStar(
        start: Pos,
        goal: Pos,
        estimate: (Pos) -> Double,
        provideNeighbors: (Pos) -> List<Pos>,
        distance: (Pos, Pos) -> Double
    ): List<Pos> {
        val openSet: PriorityQueue<Pos> = PriorityQueue { left, right ->
            (estimate(left) - estimate(right)).toInt()
        }
        openSet.add(start)

        val cameFrom: MutableMap<Pos, Pos> = mutableMapOf()
        val gScore: MutableMap<Pos, Double> = mutableMapOf()
        gScore[start] = 0.0

        val fScore: MutableMap<Pos, Double> = mutableMapOf()
        fScore[start] = estimate(start)


        while (openSet.isNotEmpty()) {
            val current = openSet.remove()

            if (current == goal) {
                return reconstructPath(cameFrom, current)
            }

            val neighbors = provideNeighbors(current)
            for (neighbor in neighbors) {
                val tentativeGScore =
                    gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + distance(current, neighbor)
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + estimate(neighbor)
                    if (neighbor !in openSet) {
                        openSet.add(neighbor)
                    }
                }
            }
        }
        return listOf()
    }

    fun elevationSymbolToInt(symbol: Char): Int = when(symbol) {
        'E' -> 26
        'S' -> 1
        in 'a'..'z' -> symbol.code - 'a'.code + 1
        else -> error("Unknown symbol: $symbol")
    }

    fun parseInput(input: List<String>): ElevationMap {
        val elevation = mutableMapOf<Pos, Int>()
        var startPos: Pos? = null
        var goalPos: Pos? = null
        input.forEachIndexed { y, s ->
            s.forEachIndexed { x, char ->
                val pos = Pos(x, y)
                elevation[pos] = elevationSymbolToInt(char)
                when (char) {
                    'S' -> {
                        startPos = pos
                    }

                    'E' -> {
                        goalPos = pos
                    }
                }
            }
        }

        if (startPos == null || goalPos == null) {
            error("no start or goal found")
        }

        val maxX = input.first().length - 1
        val maxY = input.size - 1
        return ElevationMap(elevation, startPos!!, goalPos!!, maxX, maxY)
    }

    fun createNeighborProvider(elevationMap: ElevationMap): (Pos) -> List<Pos> {
        return { pos: Pos ->
            sequenceOf(
                Pos(pos.x - 1, pos.y),
                Pos(pos.x + 1, pos.y),
                Pos(pos.x, pos.y - 1),
                Pos(pos.x, pos.y + 1)
            )
                .filter { it.x >= 0 }
                .filter { it.x <= elevationMap.maxX }
                .filter { it.y >= 0 }
                .filter { it.y <= elevationMap.maxY }
                .toList()
        }
    }

    fun createDistanceFunction(elevationMap: ElevationMap, goalPos: Pos): (Pos, Pos) -> Double {
        return { from, to ->
            val fromValue = elevationMap.elevation[from]
            val toValue = elevationMap.elevation[to]
            when {
                fromValue == null -> Double.POSITIVE_INFINITY
                toValue == null -> Double.POSITIVE_INFINITY
                toValue - fromValue > 1 -> Double.POSITIVE_INFINITY
                else -> abs(to.x - goalPos.x).toDouble() + abs(to.y - goalPos.y).toDouble()
            }
        }
    }

    fun createEstimate(goalPos: Pos): (Pos) -> Double {
        return {
            abs(it.x - goalPos.x).toDouble() + abs(it.y - goalPos.y).toDouble()
        }
    }
}


fun main() {
    val (year, day) = 2022 to "Day12"

    fun part1(input: List<String>): Int {
        val elevationMap = Day12.parseInput(input)
        val neighborProvider = Day12.createNeighborProvider(elevationMap)
        val distanceFunction = Day12.createDistanceFunction(elevationMap, elevationMap.goalPos)
        val estimate = Day12.createEstimate(elevationMap.goalPos)

        val res = Day12.aStar(elevationMap.startPos, elevationMap.goalPos, estimate, neighborProvider, distanceFunction)
        return res.size - 1
    }

    fun part2(input: List<String>): Int {
        val elevationMap = Day12.parseInput(input)
        val neighborProvider = Day12.createNeighborProvider(elevationMap)
        val distanceFunction = Day12.createDistanceFunction(elevationMap, elevationMap.goalPos)
        val estimate = Day12.createEstimate(elevationMap.goalPos)

        val aLocations = elevationMap.elevation.filter { e -> e.value == 1 }.keys.toList()

        val minList = aLocations
            .map { Day12.aStar(it, elevationMap.goalPos, estimate, neighborProvider, distanceFunction) }
            .filter { it.isNotEmpty() }
            .minBy { it.size }
        return minList.size - 1
    }

    val testInput = readInput(year, "${day}_test")
    val input = readInput(year, day)

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}
