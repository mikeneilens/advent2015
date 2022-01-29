data class Guest(val name:String, val neighbours:MutableMap<String, Int>)

fun List<String>.parse():List<Guest> {
    val guests = mutableListOf<Guest>()
    forEach{line ->
        val (name, neighbourName, happiness) = line.guestElements()
        if (guests.isNotEmpty() && guests.last().name == name) guests.last().neighbours[neighbourName] = happiness
        else guests.add(Guest(name, mutableMapOf(neighbourName to happiness)))
    }
    return guests
}

fun String.guestElements() = Triple(
    split(" ").first(),
    split(" ").last().removeSuffix("."),
    if (split(" ")[2] == "gain") split(" ")[3].toInt() else split(" ")[3].toInt() * -1 )

fun partOne(data:List<String>) = data.parse().bestGuestHappiness()

fun List<Guest>.bestGuestHappiness():Int {
    val guestCombinations = circularCombinations(size).map{ indexes -> indexes.map{ index -> this[index]} }
    return guestCombinations.map { it.sumClockwise().sum() + it.reversed().sumClockwise().sum() }.maxOf { it }
}

fun List<Guest>.sumClockwise() = mapIndexed{ index, guest ->
    if (index < size - 1) guest.neighbours.getValue(get(index + 1).name) else guest.neighbours.getValue(first().name )
}

//part two
fun List<Guest>.addGuest(name:String):List<Guest> {
    forEach{ guest -> guest.neighbours[name] = 0 }
    return this + Guest(name, associate{Pair(it.name, 0)}.toMutableMap() )
}

fun partTwo(data:List<String>) = data.parse().addGuest("Mike").bestGuestHappiness()

//circularCombinations creates every none repeating combination of integers but then only retains the combinations that start with a zero.
//This because [0,1,2,3] is the same as [1,2,3,0], [2,3,0,1] and [3,0,1,2] if numbers are arranged in a circle
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
