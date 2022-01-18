import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `first sample results in 58`() {
        assertEquals(58, paperNeeded("2x3x4"))
    }
    @Test
    fun `second sample results in 43`() {
        assertEquals(43, paperNeeded("1x1x10"))
    }
    @Test
    fun `part one`() {
        assertEquals(1588178, partOne(puzzleInput))
    }

    @Test
    fun `ribbon needed in sample 1 is 34 feet`() {
        assertEquals(34, ribbonNeeded("2x3x4"))
    }
    @Test
    fun `ribbon needed in sample 2 is 14 feet`() {
        assertEquals(14, ribbonNeeded("1x1x10"))
    }
    @Test
    fun `part two`() {
        assertEquals(3783758, partTwo(puzzleInput))
    }
}