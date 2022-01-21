import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `exmpale for part one`() {
        val sampleData = """
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> i
        """.trimIndent().split("\n")

        val result:Map<String, Int> = partOne(sampleData)
        assertEquals(72, result["d"])
        assertEquals(507, result["e"])
        assertEquals(492, result["f"])
        assertEquals(114, result["g"])
        assertEquals( -124, result["h"])
        assertEquals(-457, result["i"])
        assertEquals(123, result["x"])
        assertEquals(456, result["y"])
    }
    @Test
    fun `part one `() {
        val result = partOne(puzzleInput)
        assertEquals(956, result["a"])
    }
    @Test
    fun `part two `() {
        val result = partTwo(puzzleInput)
        assertEquals(40149, result["a"])
    }
}