import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `first example delivers to two houses`() {
        val visits:Visits = mutableSetOf()
        assertEquals(2, visits.housesVisited(">").size)
    }
    @Test
    fun `second example delivers to 4 houses`() {
        val visits:Visits = mutableSetOf()
        assertEquals(4, visits.housesVisited("^>v<").size)
    }
    @Test
    fun `third example delivers to 2 houses`() {
        val visits:Visits = mutableSetOf()
        assertEquals(2, visits.housesVisited("^v^v^v^v^v").size)
    }
    @Test
    fun `part one`() {
        val visits:Visits = mutableSetOf()
        assertEquals(2081, visits.housesVisited(puzzleInput).size)
    }

    @Test
    fun `part two with first example delivers to 3 houses`(){
        assertEquals(3, partTwo("^v"))
    }
    @Test
    fun `part two with second example delivers to 3 houses`(){
        assertEquals(3, partTwo("^>v<"))
    }
    @Test
    fun `part two with third example delivers to 11 houses`(){
        assertEquals(11, partTwo("^v^v^v^v^v"))
    }
    @Test
    fun `part two`() {
        assertEquals(2341, partTwo(puzzleInput))
    }
}