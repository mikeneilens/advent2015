
fun parse(data:List<String>) =
    allTowns(data).associateWith { destinations(it, data) }

fun allTowns(data:List<String>):Set<String> {
    val startTowns = data.map{ it.split(" ")[0]}.toSet()
    val endTowns = data.map{ it.split(" ")[2]}.toSet()
    return (startTowns + endTowns)
}

data class Destination(val name:String, val distance:Int)

fun destinations(town:String, data:List<String>):List<Destination> =
    data.filter{it.startsWith(town)}.map(String::toDestination) + data.filter{it.drop(1).contains(town)}.map(String::toDestination2)

fun String.toDestination() = Destination(split(" ")[2],split(" ")[4].toInt())
fun String.toDestination2() = Destination(split(" ")[0],split(" ")[4].toInt())

class DistanceRecord(var shortest:Int = Int.MAX_VALUE, var longest:Int = 0  )

typealias DistanceCalculator = (town:String, visited:List<String>, distance:Int, distanceRecord:DistanceRecord, destinationMap:Map<String, List<Destination>>) -> Int

fun findShortestDistance(town: String, visited: List<String>, distance: Int, distanceRecord: DistanceRecord, destinationMap: Map<String, List<Destination>>
):Int {
    if (visited.size == destinationMap.keys.size){
        if (distance < distanceRecord.shortest) distanceRecord.shortest = distance
        return distance
    }
    val destinations = destinationMap.getValue(town).filter{distance + it.distance < distanceRecord.shortest && it.name !in visited}
    if (destinations.isEmpty()) return Int.MAX_VALUE
    return destinations.map { findShortestDistance(
        it.name,
        visited + town,
        distance + it.distance,
        distanceRecord,
        destinationMap
    )}.minOf { it }
}

fun findLongestDistance(town: String, visited: List<String>, distance: Int, distanceRecord: DistanceRecord, destinationMap: Map<String, List<Destination>>):Int {
    if (visited.size == destinationMap.keys.size){
        if (distance > distanceRecord.longest) distanceRecord.longest = distance
        return distance
    }
    val destinations = destinationMap.getValue(town).filter{it.name !in visited}
    if (destinations.isEmpty()) return 0
    return destinations.map { findLongestDistance(
        it.name,
        visited + town,
        distance + it.distance,
        distanceRecord,
        destinationMap
    )}.maxOf { it }
}

fun partOne(data:List<String>, distanceCalculator:DistanceCalculator ):DistanceRecord {
    val destinationMap = parse(data)
    val distanceRecord = DistanceRecord()
    fun findDistance(town:String) {
        distanceCalculator(town, listOf(town),0, distanceRecord, destinationMap )
    }
    destinationMap.forEach { findDistance(it.key)}
    return distanceRecord
}

fun partTwo(data:List<String>):DistanceRecord {
    return partOne(data, ::findLongestDistance)
}

