
fun Set<Int>.weight() = sum()

fun quantumEntanglement(set:Set<Int>) = set.fold(1L){ acc, i -> acc * i  }

data class Info(var smallestGroupSize:Int = Int.MAX_VALUE)

fun groupsWithTargetWeight(targetWeight:Int, fullSet:List<Int>, setSoFar:Set<Int>, info:Info = Info()):List<Set<Int>> {
    return if (setSoFar.weight() == targetWeight){
        info.smallestGroupSize = setSoFar.size
        listOf(setSoFar)
    } else fullSet
        .filter{ num -> (setSoFar.size < info.smallestGroupSize) && (num + setSoFar.weight() <= targetWeight)}
        .flatMap { num ->
            groupsWithTargetWeight(targetWeight, fullSet.filter{it < num} , setSoFar + num, info )}
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