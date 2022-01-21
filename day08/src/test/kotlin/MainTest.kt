import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun `memory for empty string`() {
        val string = """
            ""
        """.trimIndent()
        assertEquals(0, calcMemoryUsed(string) )
    }
    @Test
    fun `memory for string containing 3 characters`() {
        val string = """
            "abc"
        """.trimIndent()
        assertEquals(3, calcMemoryUsed(string))
    }
    @Test
    fun `memory for string containing an escaped quote`() {
        val string = """
            "aaa\"aaa"
        """.trimIndent()
        assertEquals(7, calcMemoryUsed(string))
    }
    @Test
    fun `memory for string containing an escaped char code`() {
        val string = """
            "\x27"
        """.trimIndent()
        assertEquals(1, calcMemoryUsed(string))
    }
    @Test
    fun `memory for string containing some plain text plus an escaped char code and an escaped slash`() {
        val string = """
            "abc\x27def\\"
        """.trimIndent()
        assertEquals(8, calcMemoryUsed(string))
    }
    @Test
    fun `part one using sample data`() {
        val sampleData = """
            ""
            "abc"
            "aaa\"aaa"
            "\x27"
        """.trimIndent().split("\n")
        assertEquals(12, partOne(sampleData))
    }
    @Test
    fun `part one`() {
        assertEquals(1342, partOne(puzzleInput))
    }

    @Test
    fun `encoded length for empty string`() {
        val string = """
            ""
        """.trimIndent()
        assertEquals(6, calcEncodedLength(string) )
    }
    @Test
    fun `encoded length for string containing 3 characters`() {
        val string = """
            "abc"
        """.trimIndent()
        assertEquals(9, calcEncodedLength(string))
    }
    @Test
    fun `encoded length for string containing an escaped quote`() {
        val string = """
            "aaa\"aaa"
        """.trimIndent()
        assertEquals(16, calcEncodedLength(string))
    }
    @Test
    fun `encoded length for string containing an escaped char code`() {
        val string = """
            "\x27"
        """.trimIndent()
        assertEquals(11, calcEncodedLength(string))
    }
    @Test
    fun `encoded length for string containing some plain text plus an escaped char code and an escaped slash`() {
        val string = """
            "abc\x27def\\"
        """.trimIndent()
        assertEquals(21, calcEncodedLength(string))
    }
    @Test
    fun `part two using sample data`() {
        val sampleData = """
            ""
            "abc"
            "aaa\"aaa"
            "\x27"
        """.trimIndent().split("\n")
        assertEquals(19, partTwo(sampleData))
    }
    @Test
    fun `part two`() {
        assertEquals(2074, partTwo(puzzleInput))
    }
}