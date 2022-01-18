fun paperNeeded(data: String): Int {
    val (side1, side2, side3) = getDimensions(data).calcSides()
    return 2 * (side1 + side2 + side3) + listOf(side1, side2, side3).minOf {it}
}

fun Triple<Int, Int, Int>.calcSides() = Triple(first * second, first * third, second * third)

fun getDimensions(data: String) =
    Triple(data.split("x")[0].toInt(), data.split("x")[1].toInt(), data.split("x")[2].toInt())

fun partOne(data:List<String>):Int =
    data.fold(0){acc, value -> acc + paperNeeded(value) }

fun ribbonNeeded(data: String): Int {
    val sides = getDimensions(data)
    return sides.shortestDistanceAroundItsSides() + sides.volume()
}
fun Triple<Int, Int, Int>.shortestDistanceAroundItsSides() = listOf(2 * (first + second), 2 * ( first + third), 2 * (second +  third) ).minOf {it}
fun Triple<Int, Int, Int>.volume() = first * second * third

fun partTwo(data:List<String>):Int =
    data.fold(0){acc, value -> acc + ribbonNeeded(value) }

