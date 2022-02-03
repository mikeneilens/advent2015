import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val sampleData = """
        20
        15
        10
        5
        5
    """.trimIndent().split("\n")

    @Test
    fun `part one using sample data`() {
        assertEquals(4, partOne(sampleData,25).size)
    }
    @Test
    fun `part one using puzzle input`() {
        assertEquals(1304, partOne(puzzleInput,150).size)
    }

    @Test
    fun `part two using sample data`() {
        assertEquals(3, partTwo(sampleData,25).size)
    }
    @Test
    fun `part two using puzzle input`() {
        assertEquals(18, partTwo(puzzleInput,150).size)
    }


}