import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines(charset = Charsets.UTF_8)

/**
 * Reads the given input txt file as a single string.
 */
fun readInputText(name: String) = File("src", "$name.txt").readText(charset = Charsets.UTF_8)

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


/**
 * [greatest common denominator](https://en.wikipedia.org/wiki/Greatest_common_divisor) using
 * [Euclidean algorithm](https://en.wikipedia.org/wiki/Euclidean_algorithm)
 */
fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) {
        a
    } else {
        gcd(b, a % b)
    }
}

@JvmName("lcmForInts")
fun Iterable<Int>.lcm(): Long = map { it.toLong() }.lcm()

fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

/**
 * [least common multiple](https://en.wikipedia.org/wiki/Least_common_multiple) for a list of numbers
 */
fun Iterable<Long>.lcm(): Long = reduce(::lcm)



fun createDebug(debug: Boolean): (() -> String) -> Unit {
    return { provider ->
        if (debug) {
            println(provider())
        }
    }
}