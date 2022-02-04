enum class Light(val text:Char){Off('.'), On('#') }

fun Char.toLight() = if (equals(Light.On.text)) Light.On else Light.Off

data class Position(val x:Int, val y:Int) {
    fun surroundingPositions(max:Int) =
        (maxOf(y-1,0)..minOf(y+1,max)).flatMap{y ->
            (maxOf(x-1,0)..minOf(x+1,max)).mapNotNull{x ->
                if (Position(x,y)!= this ) Position(x,y) else null
            }
        }

    fun isCornerPosition(max: Int) =
        (x == 0 && y == 0) || (x == max && y == max) || (x == 0 && y == max) || (x == max && y == 0)

}

typealias LightMap = Map<Position, Light>

fun LightMap.toStrings(max:Int) = (0..max).map {y ->
    (0..max).map{x -> getValue(Position(x,y)).text }.joinToString("")
}

fun parse(data:List<String>):LightMap =
    data.flatMapIndexed {y, line ->
        line.mapIndexed { x, char -> Pair(Position(x,y), char.toLight()) }
    }.toMap()

fun LightMap.statusCalculatorPartOne(position:Position, max:Int) = when(getValue(position)) {
    Light.On -> if ( position.surroundingPositions(max).count{ getValue(it) == Light.On} in 2..3 ) Pair(position, Light.On) else Pair(position, Light.Off)
    Light.Off -> if ( position.surroundingPositions(max).count{ getValue(it) == Light.On} ==  3 ) Pair(position, Light.On) else Pair(position, Light.Off)
}

typealias StatusCalculator = LightMap.(Position, Int) -> Pair<Position,Light>

fun LightMap.newLightMap(max:Int, statusUpdater:StatusCalculator) = map{ statusUpdater(it.key, max) }.toMap()

fun partOne(data:List<String>, steps:Int, statusUpdater:StatusCalculator = LightMap::statusCalculatorPartOne ):Int {
    val max = data.first().lastIndex
    var lightMap = parse(data)
    (1..steps).forEach { _ -> lightMap = lightMap.newLightMap(max, statusUpdater) }
    return lightMap.count { it.value == Light.On }
}

fun LightMap.statusCalculatorPartTwo(position:Position, max:Int) =
    if (position.isCornerPosition(max)) Pair(position, Light.On) else statusCalculatorPartOne(position, max)

fun partTwo(data:List<String>, steps:Int):Int = partOne(data, steps, LightMap::statusCalculatorPartTwo)


