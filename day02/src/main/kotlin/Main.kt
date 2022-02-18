data class Box (val l:Int, val w:Int, val h:Int) {

    val areaOfEachSide = listOf(l * w, l * h, w * h)
    val surfaceArea = 2 * areaOfEachSide.sum()

    fun smallestSide() = areaOfEachSide.minOf { it }
    fun shortestDistanceAroundItsSides() = listOf(2 * (l + w), 2 * ( l + h), 2 * (w + h) ).minOf {it}

    fun volume() = l * w * h
}

fun paperNeeded(data: String): Int {
    val box = createBox(data)
    return box.surfaceArea + box.smallestSide()
}

fun createBox(data: String) =
    Box(data.split("x")[0].toInt(), data.split("x")[1].toInt(), data.split("x")[2].toInt())

fun partOne(data:List<String>):Int =
    data.fold(0){totalPaper, boxData -> totalPaper + paperNeeded(boxData) }

fun ribbonNeeded(data: String): Int {
    val box = createBox(data)
    return box.shortestDistanceAroundItsSides() + box.volume()
}

fun partTwo(data:List<String>):Int =
    data.fold(0){totalRibbon, boxData -> totalRibbon + ribbonNeeded(boxData) }

