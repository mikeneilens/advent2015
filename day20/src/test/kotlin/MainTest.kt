import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `calculate factors of 100`() {
        val factorsOfHundred = setOf(1,2,4,5,10,20,25,50,100)
        assertEquals(factorsOfHundred,calcFactors(100))
    }

    @Test
    fun `elf two can deliver 2 times 10 presents to house 100 and also house 102`() {
        assertEquals(1020, partOneRule(1000,2,100))
        assertEquals(1020, partOneRule(1000,2,102))
    }
    @Test
    fun `calculating presents for houses`() {
        assertEquals(10, calcPresents(1, partOneRule))
        assertEquals(70, calcPresents(4, partOneRule))
    }
    @Test
    fun `lowest house number for 120 presents`() {
        assertEquals(6, partOne(120))
    }
    @Test
    fun `part one`() {
        assertEquals(831600, partOne(36000000))
    }

    @Test
    fun `elf two can deliver 2 times 11 presents to house 100 but not house 102`() {
        assertEquals(1022, partTwoRule(1000,2,100))
        assertEquals(1000, partTwoRule(1000,2,102))
    }
    @Test
    fun `part two`() {
        assertEquals(884520, partTwo(36000000))
    }
}