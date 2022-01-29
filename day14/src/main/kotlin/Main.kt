data class Reindeer(val name:String, val flySpeed:Int, val flyTime:Int, val restTime:Int) {

    fun distanceTravelled(time:Int):Int {
        val blocksOfFlyAndRest = flyTime + restTime
        val numberOfBlocksFlown = time / blocksOfFlyAndRest
        val incompleteTime = time - numberOfBlocksFlown * blocksOfFlyAndRest
        return flySpeed * flyTime * numberOfBlocksFlown + minOf( flySpeed * incompleteTime , flySpeed * flyTime)
    }
}

fun List<String>.parse():List<Reindeer> =
    map{line -> Reindeer(line.components[0], line.components[1].toInt(), line.components[2].toInt(), line.components[3].toInt()) }

val String.components get() = listOf(split(" ")[0], split(" ")[3], split(" ")[6], split(" ")[13])

fun partOne(data:List<String>, time:Int):Int = data.parse().maxOf { it.distanceTravelled(time) }

data class ReindeerTravel(val name:String, val distance:Int)

fun List<Reindeer>.winnersAfterTime(time:Int):List<ReindeerTravel> {
    val distanceTravelledByEachReindeer = map{ReindeerTravel(it.name, it.distanceTravelled(time))}
    val maxDistance = distanceTravelledByEachReindeer.maxOf { it.distance }
    return distanceTravelledByEachReindeer.filter { it.distance == maxDistance }
}

fun List<Reindeer>.accumulatePoints(time:Int):Map<String, Int> {
    val scoresMap = associate { Pair(it.name, 0) }.toMutableMap()
    (1..time).forEach { t -> updateScoreOfWinners(t, scoresMap) }
    return scoresMap
}

private fun List<Reindeer>.updateScoreOfWinners(time: Int, scores: MutableMap<String, Int>) {
    winnersAfterTime(time).forEach { winner -> scores[winner.name] = scores.getValue(winner.name) + 1 }
}

fun partTwo(data:List<String>, time:Int):Int = data.parse().accumulatePoints(time).maxOf { it.value }
