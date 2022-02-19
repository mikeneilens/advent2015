typealias Group = Set<Int>

fun Group.weight() = sum()

fun Group.quantumEntanglement() = fold(1L){ acc, i -> acc * i  }


class GroupCalculator (private var smallestGroupSize: Int = Int.MAX_VALUE)  {

    fun groupsWithTargetWeight(targetWeight:Int, remainingNumbers:List<Int>, groupSoFar:Group = setOf()):List<Group> {
        return if (groupSoFar.weight() == targetWeight){
            smallestGroupSize = groupSoFar.size
            listOf(groupSoFar)
        } else remainingNumbers
            .filter{ num -> (groupSoFar.size < smallestGroupSize) && (num + groupSoFar.weight() <= targetWeight)}
            .flatMap { num ->
            groupsWithTargetWeight(targetWeight, remainingNumbers.filter{it < num} , groupSoFar + num)}
        }

    fun groupsWithSmallestSize(targetWeight:Int, numbers:List<Int>):Long {
        return groupsWithTargetWeight(targetWeight, numbers)
            .sortedWith(compareBy(Group::size).thenBy (Group::quantumEntanglement))
            .first()
            .quantumEntanglement()
    }
}

val targetWeightPartOne = { strings:List<String> -> strings.sumOf(String::toInt)/3}

fun parse(data:List<String>) = data.map(String::toInt).reversed()

fun partOne(data:List<String>, weightCalculator:(List<String> )->Int = targetWeightPartOne) =
    GroupCalculator().groupsWithSmallestSize(weightCalculator(data), parse(data))

val targetWeightPartTwo = { strings:List<String> -> strings.sumOf(String::toInt)/4 }

fun partTwo(data:List<String>): Long = partOne(data, targetWeightPartTwo)
