
fun Set<Int>.weight() = sum()

fun quantumEntanglement(set:Set<Int>) = set.fold(1L){ acc, i -> acc * i  }

data class Info(var smallestGroupSize:Int = Int.MAX_VALUE)

fun groupsWithTargetWeight(targetWeight:Int, fullSet:List<Int>, setSoFar:Set<Int>, info:Info = Info()):List<Set<Int>> {
    if (setSoFar.weight() == targetWeight){
        info.smallestGroupSize = setSoFar.size
        return listOf(setSoFar)
    }

    return if (setSoFar.size < info.smallestGroupSize)
        fullSet.filter{it + setSoFar.weight() <= targetWeight}.flatMap { num ->
            groupsWithTargetWeight(targetWeight, fullSet.filter{it < num} , setSoFar + num, info )
        }
    else listOf()
}

val targetWeightPartOne = { strings:List<String> -> strings.sumOf(String::toInt)/3}

fun parse(data:List<String>) = data.map(String::toInt).reversed()

fun partOne(data:List<String>, weightCalculator:(List<String> )->Int = targetWeightPartOne  ):Long {
    val groupsMatchingTarget =  groupsWithTargetWeight(weightCalculator(data), parse(data), setOf())
    val groupsWithSmallestSize = { group:Set<Int> -> group.size == groupsMatchingTarget.minByOrNull{ it.size }?.size}
    return groupsMatchingTarget.filter(groupsWithSmallestSize)
        .map(::quantumEntanglement)
        .minOf{ it }
}

fun partTwo(data:List<String>): Long {
    val targetWeightPartTwo = { strings:List<String> -> strings.sumOf(String::toInt)/4 }
    return partOne(data, targetWeightPartTwo)
}