class Container(val capacity:Int) {
    override fun toString(): String = "capacity=$capacity"
}

fun String.toContainer() = Container(toInt())

fun combinationsWithTargetCapacity(target: Int, remaining:List<Container>, setOfContainers:Set<Container> = setOf(), processedBefore:MutableSet<Set<Container>> = mutableSetOf()): List<Set<Container>> {
    if (setOfContainers in processedBefore) return listOf()
    processedBefore.add(setOfContainers)
    return when {
        setOfContainers.totalCapacity() > target -> listOf()
        setOfContainers.totalCapacity() == target -> listOf(setOfContainers)
        else-> remaining.flatMap { container -> combinationsWithTargetCapacity(target, remaining.filter{ it != container }, setOfContainers + container, processedBefore) }
    }
}

fun Set<Container>.totalCapacity() = sumOf { it.capacity }

fun partOne(data:List<String>, target:Int) =
   combinationsWithTargetCapacity(target, data.map(String::toContainer))

fun partTwo(data:List<String>, target:Int): List<Set<Container>> {
    val combinations = combinationsWithTargetCapacity(target, data.map(String::toContainer))
    val minNumberOfContainers = combinations.minOf {setOfContainers -> setOfContainers.size }
    return combinations.filter{setOfContainers -> setOfContainers.size == minNumberOfContainers}
}

