import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val sampleData = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent().split("\n")
    @Test
    fun `finding all towns in sample data`() {
        assertEquals(setOf("Belfast","Dublin","London"),allTowns(sampleData))
    }
    @Test
    fun `finding destinations for each town`() {
        assertEquals(listOf(Destination("Dublin", 464), Destination("Belfast", 518)), destinations("London", sampleData)  )
        assertEquals(listOf(Destination("London", 518), Destination("Dublin", 141)), destinations("Belfast", sampleData)  )
        assertEquals(listOf(Destination("Belfast", 141), Destination("London", 464)), destinations("Dublin", sampleData)  )
    }
    @Test
    fun `parsing data into map of destinations for each town`() {
        val map = parse(sampleData)
        assertEquals(listOf(Destination("Dublin", 464), Destination("Belfast", 518)), map["London"])
        assertEquals(listOf(Destination("London", 518), Destination("Dublin", 141)), map["Belfast"])
        assertEquals(listOf(Destination("Belfast", 141), Destination("London", 464)), map["Dublin"])
    }
    @Test
    fun `find shortest distance using sample data`() {
        assertEquals(605, partOne(sampleData, ::findShortestDistance).shortest)
    }
    @Test
    fun `part one`() {
        assertEquals(251, partOne(puzzleInput, ::findShortestDistance).shortest)
    }

    @Test
    fun `find longest distance using sample data`() {
        assertEquals(982, partTwo(sampleData).longest)
    }
    @Test
    fun `part two`() {
        assertEquals(898, partTwo(puzzleInput).longest)
    }
}