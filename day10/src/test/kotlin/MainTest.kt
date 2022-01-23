import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val puzzleInput = "1321131112".toList()
    @Test
    fun `look and say all the samples`() {
        data class TestData (val testValue:List<Char>, val expectedResult:List<Char>)
        val testData = listOf(
            TestData( testValue = "1".toList(), expectedResult = "11".toList()),
            TestData( testValue = "11".toList(), expectedResult = "21".toList()),
            TestData( testValue = "21".toList(), expectedResult = "1211".toList()),
            TestData( testValue = "1211".toList(), expectedResult = "111221".toList()),
            TestData( testValue = "111221".toList(), expectedResult = "312211".toList()),
        )
        testData.forEach {test ->
            assertEquals(test.expectedResult, lookAndSay(test.testValue))
        }
    }
    @Test
    fun `part one using sample data`() {
        data class TestData (val testValue:List<Char>, val expectedResult:Int)
        val testData = listOf(
            TestData( testValue = "1".toList(), expectedResult = "11".length),
            TestData( testValue = "11".toList(), expectedResult = "21".length),
            TestData( testValue = "21".toList(), expectedResult = "1211".length),
            TestData( testValue = "1211".toList(), expectedResult = "111221".length),
            TestData( testValue = "111221".toList(), expectedResult = "312211".length),
        )
        testData.forEach { test ->
            assertEquals(test.expectedResult, partOne(test.testValue))
        }
    }
    @Test
    fun `part one`() {
        assertEquals(492982, partOne(puzzleInput, 40))
    }
    @Test
    fun `part two`() {
        assertEquals(6989950, partOne(puzzleInput, 50))
    }
}