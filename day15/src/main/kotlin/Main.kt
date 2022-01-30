data class Ingredient(val name:String, val capacity:Long, val durability:Long, val flavour:Long, val texture:Long, val calories:Long, var teaspoons:Long = 0L)

fun parse(data:List<String>) = data.map(String::toIngredient)

fun String.toIngredient() = Ingredient(
    split(" ")[0].dropLast(1),
    split(" ")[2].dropLast(1).toLong(),
    split(" ")[4].dropLast(1).toLong(),
    split(" ")[6].dropLast(1).toLong(),
    split(" ")[8].dropLast(1).toLong(),
    split(" ")[10].toLong(),
)

fun List<Ingredient>.totalScore() = maxOf(sumOf{it.capacity * it.teaspoons},0) *
        maxOf(sumOf{it.durability * it.teaspoons},0) *
        maxOf(sumOf{it.flavour * it.teaspoons},0)  *
        maxOf(sumOf{it.texture * it.teaspoons},0)

fun List<Ingredient>.totalCalories() = sumOf{it.calories * it.teaspoons}

fun findBestCombinationScore(data:List<String>, isBestCombinationRule:(List<Ingredient>, Long) -> Boolean ):Long {
    val ingredients = parse(data)
    val combinations = combinationsOfNumbersAddingToATotal(100, ingredients.size)
    var bestTotalScore = 0L
    combinations.forEach { teaSpoonCombos ->
        teaSpoonCombos.forEachIndexed { index, teaspoons -> ingredients[index].teaspoons = teaspoons  }
        if (isBestCombinationRule(ingredients, bestTotalScore) ) bestTotalScore = ingredients.totalScore()
    }
    return bestTotalScore
}

fun isBestSoFarPartOne(ingredients: List<Ingredient>, bestTotalScore: Long) = ingredients.totalScore() > bestTotalScore

fun isBestSoFarPartTwo(ingredients: List<Ingredient>, bestTotalScore: Long) = ingredients.totalCalories() == 500L && ingredients.totalScore() > bestTotalScore


//e.g. if total is 5 and noOfNumbers is 2 then this returns [[1, 4], [2, 3], [3, 2], [4, 1]]
//     if total is 5 and noOfNumnres is 3 then this returns [[1, 1, 3], [1, 2, 2], [1, 3, 1], [2, 1, 2], [2, 2, 1], [3, 1, 1]]
fun combinationsOfNumbersAddingToATotal(total:Long, noOfNumbers:Int):List<List<Long>> =
    if (noOfNumbers == 1)  listOf(listOf(total))
    else (1L until total).fold(listOf(listOf<Long>())){ listOfCombinations, i ->
        val subCombinations = combinationsOfNumbersAddingToATotal(total - i, noOfNumbers -1  )
        listOfCombinations + subCombinations.map{ listOf(i) + it }
    }.drop(1)



