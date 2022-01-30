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
    val combinations = variants(100, ingredients.size)
    var bestTotalScore = 0L
    combinations.forEach { teaSpoonCombos ->
        teaSpoonCombos.forEachIndexed { index, teaspoons -> ingredients[index].teaspoons = teaspoons  }
        if (isBestCombinationRule(ingredients, bestTotalScore) ) bestTotalScore = ingredients.totalScore()
    }
    return bestTotalScore
}

fun isBestSoFarPartOne(ingredients: List<Ingredient>, bestTotalScore: Long) = ingredients.totalScore() > bestTotalScore

fun isBestSoFarPartTwo(ingredients: List<Ingredient>, bestTotalScore: Long) = ingredients.totalCalories() == 500L && ingredients.totalScore() > bestTotalScore

//This is too big!
fun variants(total:Long, size:Int = 4  ):List<List<Long>> {
    if (size == 1) return listOf(listOf(total))
    val result = mutableListOf<List<Long>>()
    (1L..total).forEach { i ->
        if (size == 2) {if (i  < total) result.add(listOf(i, total - i))}
        else {
            (1L..(total - i )).forEach { j->
                if (size == 3) {if (i + j < total) result.add(listOf(i, j, total - i, j))}
                else {
                    (1L..(total - i - j)).forEach { k->
                        if (i + j + k  < total) result.add(listOf(i, j, k, total - i - j - k))
                    }
                }
            }
        }
    }
    return result
}


