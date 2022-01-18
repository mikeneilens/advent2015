import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `first samples result in floor 0`() {
        val sample1 = "(())"
        assertEquals(0, findFloor(sample1))
        val sample2 = "()()"
        assertEquals(0, findFloor(sample2))
    }
    @Test
    fun `second samples result in floor 3`() {
        val sample1 = "((("
        assertEquals(3, findFloor(sample1))
        val sample2 = "(()(()("
        assertEquals(3, findFloor(sample2))
    }
    @Test
    fun `third samples result in floor 3`() {
        val sample1 = "))((((("
        assertEquals(3, findFloor(sample1))
    }
    @Test
    fun `fourth samples result in floor -1`() {
        val sample1 = "())"
        assertEquals(-1, findFloor(sample1))
        val sample2 = "))("
        assertEquals(-1, findFloor(sample2))
    }
    @Test
    fun `fifth samples result in floor -3`() {
        val sample1 = ")))"
        assertEquals(-3, findFloor(sample1))
        val sample2 = ")())())"
        assertEquals(-3, findFloor(sample2))
    }
    @Test
    fun `part one`() {
        assertEquals(232, findFloor(puzzleInput))
    }

    @Test
    fun `part two first sample results in position 1`() {
        assertEquals(1, findPositionForFloor(")",-1))
    }
    @Test
    fun `part two second sample results in position 5`() {
        assertEquals(5, findPositionForFloor("()())",-1))
    }
    @Test
    fun `part two`() {
        assertEquals(1783, findPositionForFloor(puzzleInput,-1))
    }
}