typealias Group = Set<Int>

fun Group.weight() = sum()

fun quantumEntanglement(group:Group) = group.fold(1L){ acc, i -> acc * i  }

data class Info(var smallestGroupSize:Int = Int.MAX_VALUE)

fun groupsWithTargetWeight(targetWeight:Int, fullSet:List<Int>, groupSoFar:Group, info:Info = Info()):List<Group> {
    return if (groupSoFar.weight() == targetWeight){
        info.smallestGroupSize = groupSoFar.size
        listOf(groupSoFar)
    } else fullSet
        .filter{ num -> (groupSoFar.size < info.smallestGroupSize) && (num + groupSoFar.weight() <= targetWeight)}
        .flatMap { num ->
            groupsWithTargetWeight(targetWeight, fullSet.filter{it < num} , groupSoFar + num, info )}
}

val targetWeightPartOne = { strings:List<String> -> strings.sumOf(String::toInt)/3}

fun parse(data:List<String>) = data.map(String::toInt).reversed()

fun partOne(data:List<String>, weightCalculator:(List<String> )->Int = targetWeightPartOne  ):Long {
    val groupsMatchingTarget =  groupsWithTargetWeight(weightCalculator(data), parse(data), setOf())
    val groupsWithSmallestSize = groupsMatchingTarget.minByOrNull{ it.size }?.size
    return groupsMatchingTarget.filter {group -> group.size == groupsWithSmallestSize  }
        .map(::quantumEntanglement)
        .minOf{ it }
}

fun partTwo(data:List<String>): Long {
    val targetWeightPartTwo = { strings:List<String> -> strings.sumOf(String::toInt)/4 }
    return partOne(data, targetWeightPartTwo)
}