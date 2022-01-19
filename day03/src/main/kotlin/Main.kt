
data class Position(var x:Int = 0, var y:Int=0 ) {
    fun move(direction:Char) = when(direction) {
        '<' -> Position(x - 1, y)
        '>' -> Position(x + 1, y)
        '^' -> Position(x, y + 1)
        'v' -> Position(x, y - 1)
        else -> this
    }
}

typealias Visits = MutableSet<Position>

fun Visits.housesVisited(directions: String): Visits {
    var position = Position()
    add(position)
    directions.forEach { direction ->
        position = position.move(direction)
        add(position)
    }
    return this
}

fun partTwo(directions: String): Int =
   mutableSetOf<Position>()
       .housesVisited(santaData(directions,0))
       .housesVisited(santaData(directions,1))
       .size


private fun santaData(directions: String, operative:Int) =  directions.chunked(2).map { it[operative] }.joinToString("")

