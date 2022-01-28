data class Guest(val name:String, val neighbours:MutableMap<String, Int>)

fun List<String>.parse():List<Guest> = drop(1).fold(listOf(first().toGuest()),::updateGuests)

fun String.toGuest() = Guest(
    split(" ").first(),
    mutableMapOf(split(" ").last().removeSuffix(".") to if (split(" ")[2] == "gain") split(" ")[3].toInt() else split(" ")[3].toInt() * -1 )
)

fun updateGuests(guests: List<Guest>, string: String) =
    if (string.toGuest().name != guests.last().name) guests + string.toGuest()
    else guests.updateLastGuest(string.toGuest().neighbours)

fun List<Guest>.updateLastGuest(newNeighbours:Map<String, Int> ):List<Guest> {
    last().neighbours.putAll(newNeighbours)
    return this
}

fun partOne(data:List<String>) = data.parse().bestGuestHappiness()

fun List<Guest>.bestGuestHappiness():Int {
    val guestCombinations = circularCombinations(size).map{ indexes -> indexes.map{ index -> this[index]} }
    return guestCombinations.map { it.sumClockwise().sum() + it.sumAntiClockwise().sum() }.maxOf { it }
}
fun List<Guest>.sumClockwise() = mapIndexed{ index, guest ->
    if (index < size - 1) guest.neighbours.getValue(get(index + 1).name) else guest.neighbours.getValue(first().name )
}
fun List<Guest>.sumAntiClockwise() = mapIndexed{ index, guest ->
    if (index > 0) guest.neighbours.getValue(get(index - 1).name) else guest.neighbours.getValue(last().name)
}

//circular creates every none repeating combination of integers but then only retains the combinations that start with a zero.
tailrec fun circularCombinations(size:Int, result:List<List<Int>> = listOf(listOf(0))):List<List<Int>> {
    if (result.isEmpty() && size==0 || result.first().size == size ) return result
    val newItem = result.first().size
    val lists =result.map{ list ->
        (0..newItem ).map { index -> list.insert(newItem, index) }
    }
    val newResult = lists.drop(1).fold(lists.first()){total, l -> total + l}.filter { it.first() == 0 }
    return circularCombinations(size, newResult)
}

//if a combinations is [0,1] then to increase the number of combinations add the new value before and after every digit
// to create a new list of combinations. i.e. [2,0,1], [0,2,1] and [0,1,2]
fun List<Int>.insert(number:Int, before:Int):List<Int> {
    if (before > lastIndex) return this + number
    val result = mutableListOf<Int>()
    forEachIndexed {index, n ->
        if (before == index) result.add(number)
        result.add(n)
    }
    return result
}

fun List<Guest>.addGuest(name:String):List<Guest> {
    forEach{ guest -> guest.neighbours[name] = 0 }
    return this + Guest(name, associate{Pair(it.name, 0)}.toMutableMap() )
}

fun partTwo(data:List<String>) = data.parse().addGuest("Mike").bestGuestHappiness()