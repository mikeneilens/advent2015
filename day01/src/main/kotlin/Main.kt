fun partOne(data: String):Int = findFloor(data)

fun findFloor(data: String): Int = data.count{it == '('} - data.count{it == ')'}

fun partTwo(data: String):Int = findPositionForFloor(data, -1)

fun findPositionForFloor(data: String, target:Int, floor:Int = 0, moves:Int = 0):Int =
    if (floor == target) moves
    else {
        val newFloor = if (data.first() == '(') floor + 1 else floor - 1
        findPositionForFloor(data.drop(1), target, newFloor, moves + 1 )
    }

