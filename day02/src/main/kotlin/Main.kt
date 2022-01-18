data class Box (val l:Int, val w:Int, val h:Int)

fun paperNeeded(data: String): Int {
    val (side1, side2, side3) = getDimensions(data).calcSides()
    return 2 * (side1 + side2 + side3) + listOf(side1, side2, side3).minOf {it}
}

fun Box.calcSides() = Triple(l * w, l * h, w * h)

fun getDimensions(data: String) =
    Box(data.split("x")[0].toInt(), data.split("x")[1].toInt(), data.split("x")[2].toInt())

fun partOne(data:List<String>):Int =
    data.fold(0){totalPaper, boxData -> totalPaper + paperNeeded(boxData) }

fun ribbonNeeded(data: String): Int {
    val sides = getDimensions(data)
    return sides.shortestDistanceAroundItsSides() + sides.volume()
}
fun Box.shortestDistanceAroundItsSides() = listOf(2 * (l + w), 2 * ( l + h), 2 * (w + h) ).minOf {it}
fun Box.volume() = l * w * h

fun partTwo(data:List<String>):Int =
    data.fold(0){totalRibbon, boxData -> totalRibbon + ribbonNeeded(boxData) }

