import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val sampleData = """
        1
        2
        3
        4
        5
        7
        8
        9
        10
        11
    """.trimIndent().split("\n")

    @Test
    fun `target weight of sample data is 20`() {
        assertEquals(20, targetWeightPartOne(sampleData))
    }
    @Test
    fun `quantum entanglement of 10, 9,1  is 90`() {
        assertEquals(90, setOf(10,9,1).quantumEntanglement())
    }
    @Test
    fun `smalest sets that add up to 20`() {
        val fullSet = parse(sampleData)
        val result = GroupCalculator().groupsWithTargetWeight(20, fullSet, setOf())
        val expectedResult = listOf(setOf(11, 9))
        assertEquals(expectedResult, result)
    }
    @Test
    fun `part one using sample data`() {
        assertEquals(99, partOne(sampleData))
    }
    @Test
    fun `part one using puzzle input`() {
        assertEquals(11266889531L, partOne(puzzleInput))
    }

    @Test
    fun `part two using sample data`() {
        assertEquals(44, partTwo(sampleData))
    }
    @Test
    fun `part two using puzzleInput`() {
        assertEquals(77387711L, partTwo(puzzleInput))
    }

}
