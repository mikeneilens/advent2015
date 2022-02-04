import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {

    val sampleData = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent().split("\n")

    @Test
    fun `parse data into a lightMap`() {
        assertEquals(sampleData, parse(sampleData).toStrings(5))
    }
    @Test
    fun `surrounding positions`() {
        assertEquals(listOf(Position(x=1, y=0), Position(x=0, y=1), Position(x=1, y=1)), Position(0,0).surroundingPositions(5),"position surrounding top left")
        assertEquals(listOf(Position(x=4, y=4), Position(x=5, y=4), Position(x=4, y=5)), Position(5,5).surroundingPositions(5),"position bottom right")
        assertEquals(listOf(Position(x=0, y=2), Position(x=1, y=2), Position(x=1, y=3), Position(x=0, y=4), Position(x=1, y=4)), Position(0,3).surroundingPositions(5),"position on left")
        assertEquals(listOf(Position(x=4, y=2), Position(x=5, y=2), Position(x=4, y=3), Position(x=4, y=4), Position(x=5, y=4)), Position(5,3).surroundingPositions(5),"position on right")
        assertEquals(listOf(Position(x=2, y=0), Position(x=4, y=0), Position(x=2, y=1), Position(x=3, y=1), Position(x=4, y=1)), Position(3,0).surroundingPositions(5),"position at top")
        assertEquals(listOf(Position(x=2, y=4), Position(x=3, y=4), Position(x=4, y=4), Position(x=2, y=5), Position(x=4, y=5)), Position(3,5).surroundingPositions(5),"position at bottom")
        assertEquals(listOf(Position(x=2, y=2), Position(x=3, y=2), Position(x=4, y=2), Position(x=2, y=3), Position(x=4, y=3), Position(x=2, y=4), Position(x=3, y=4), Position(x=4, y=4)), Position(3,3).surroundingPositions(5),"position in middle")
    }
    @Test
    fun `after step 1`() {
        val afterStep1 = """
            ..##..
            ..##.#
            ...##.
            ......
            #.....
            #.##..
        """.trimIndent().split("\n")
        assertEquals(afterStep1, parse(sampleData).newLightMap(5, LightMap::statusCalculatorPartOne).toStrings(5))
    }
    @Test
    fun `after step 4`() {
        val afterStep1 = """
            ......
            ......
            ..##..
            ..##..
            ......
            ......
        """.trimIndent().split("\n")
        assertEquals(afterStep1, parse(sampleData).newLightMap(5,LightMap::statusCalculatorPartOne).newLightMap(5,LightMap::statusCalculatorPartOne).newLightMap(5,LightMap::statusCalculatorPartOne).newLightMap(5,LightMap::statusCalculatorPartOne).toStrings(5))
    }
    @Test
    fun `part one using sample data`() {
        assertEquals(4, partOne(sampleData, 4))
    }
    @Test
    fun `part one using puzzle input`() {
        assertEquals(768, partOne(puzzleInput, 100))
    }
    @Test
    fun `part two using puzzle input`() {
        assertEquals(781, partTwo(puzzleInput, 100))
    }

}