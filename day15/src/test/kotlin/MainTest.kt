import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTest {

    val sampleData = """
        Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
    """.trimIndent().split("\n")

    @Test
    fun `convert string to an ingredient`() {
        val string = "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8"
        assertEquals("Butterscotch", string.toIngredient().name)
        assertEquals(-1L, string.toIngredient().capacity)
        assertEquals(-2L, string.toIngredient().durability)
        assertEquals(6L, string.toIngredient().flavour)
        assertEquals(3L, string.toIngredient().texture)
        assertEquals(8L, string.toIngredient().calories)
    }
    @Test
    fun `total for 44 butterscotch and 56 cinnamon`() {
        val ingredients = parse(sampleData)
        ingredients[0].teaspoons = 44
        ingredients[1].teaspoons = 56
        assertEquals(62842880, ingredients.totalScore())
    }
    @Test
    fun `creating an array with all possible combinations of 4 values adding to 100`() {
        val combinations = combinationsOfNumbersAddingToATotal(100L,4)
        assertEquals(156849, combinations.size)
        assertTrue(combinations.all { it.sum() == 100L } )
    }
    @Test
    fun `part one using sample data`() {
        assertEquals(62842880L, partOne(sampleData))
    }
    @Test
    fun `part one`() {
        assertEquals(222870L, partOne(puzzleInput))
    }

    @Test
    fun `total calories for 40 butterscotch and 60 cinnamon is 500`() {
        val ingredients = parse(sampleData)
        ingredients[0].teaspoons = 40L
        ingredients[1].teaspoons = 60L
        assertEquals(500L, ingredients.totalCalories())
    }
    @Test
    fun `part two using sample data`() {
        assertEquals(57600000L, partTwo(sampleData))
    }

    @Test
    fun `part two`() {
        assertEquals(117936L, partTwo(puzzleInput))
    }
}