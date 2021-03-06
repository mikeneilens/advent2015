import kotlin.math.sqrt

fun calcPresents(houseNumber:Int, presentRule:(Int, Int, Int)->Int ):Int =
    calcFactors(houseNumber).fold(0){total:Int, factor:Int -> presentRule(total, factor, houseNumber)}

val partOneRule = {total:Int, elf:Int, _:Int -> total + elf * 10}

fun partOne(presentsNeeded:Int, presentRule:(Int,Int,Int)->Int = partOneRule ):Int {
    var houseNumber = 1
    while (calcPresents(houseNumber, presentRule) < presentsNeeded) {houseNumber++}
    return houseNumber
}

val partTwoRule = {total:Int, elf:Int, houseNumber: Int -> if (houseNumber > elf * 50) total else total + 11 * elf}

fun partTwo(presentsNeeded:Int) = partOne(presentsNeeded, partTwoRule)

//Not sure if this is the most efficient way to determine all factors of an integer!
fun calcFactors(n: Int):Set<Int> {
    val factors = mutableSetOf<Int>()
    val max = sqrt(n.toDouble())
    var i = 0
    while (++i <= max) {
        if (n % i == 0) {
            factors.add(i)
            if (n / i != i) factors.add(n/i)
        }
    }
    return factors
}
