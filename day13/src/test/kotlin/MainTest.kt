import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    private val sampleData = """
        Alice would gain 54 happiness units by sitting next to Bob.
        Alice would lose 79 happiness units by sitting next to Carol.
        Alice would lose 2 happiness units by sitting next to David.
        Bob would gain 83 happiness units by sitting next to Alice.
        Bob would lose 7 happiness units by sitting next to Carol.
        Bob would lose 63 happiness units by sitting next to David.
        Carol would lose 62 happiness units by sitting next to Alice.
        Carol would gain 60 happiness units by sitting next to Bob.
        Carol would gain 55 happiness units by sitting next to David.
        David would gain 46 happiness units by sitting next to Alice.
        David would lose 7 happiness units by sitting next to Bob.
        David would gain 41 happiness units by sitting next to Carol.
    """.trimIndent().split("\n")

    @Test
    fun `parse data into Guests`() {
        val guests = sampleData.parse()
        assertEquals("Alice", guests[0].name )
        assertEquals(54, guests[0].neighbours["Bob"] )
        assertEquals(-79, guests[0].neighbours["Carol"] )
        assertEquals(-2, guests[0].neighbours["David"] )
        assertEquals("David", guests[3].name )
        assertEquals(46, guests[3].neighbours["Alice"] )
        assertEquals(-7, guests[3].neighbours["Bob"] )
        assertEquals(41, guests[3].neighbours["Carol"] )
    }

    @Test
    fun `creating a circular combination of size 3`() {
        assertEquals(listOf(listOf(0, 2, 1), listOf(0, 1, 2)), circularCombinations(3))
    }
    @Test
    fun `part one with sample data`() {
        assertEquals(330, partOne(sampleData))
    }
    @Test
    fun `part one with puzzle input`() {
        assertEquals(664, partOne(puzzleInput))
    }

    @Test
    fun `adding a guest to sample data`() {
        assertEquals(5, sampleData.parse().addGuest("Mike").size)
        assertEquals(mutableMapOf("Alice" to 0, "Bob" to 0, "Carol" to 0, "David" to 0), sampleData.parse().addGuest("Mike").last().neighbours)
        assertEquals(mutableMapOf("Bob" to 54, "Carol" to -79, "David" to -2, "Mike" to 0), sampleData.parse().addGuest("Mike").first().neighbours)
    }
    @Test
    fun `part two with puzzle input`() {
        assertEquals(640, partTwo(puzzleInput))
    }

}