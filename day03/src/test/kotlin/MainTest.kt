import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `first example delivers to two houses`() {
        assertEquals(2, Visits().visitHouses(">").size)
    }
    @Test
    fun `second example delivers to 4 houses`() {
        assertEquals(4, Visits().visitHouses("^>v<").size)
    }
    @Test
    fun `third example delivers to 2 houses`() {
        assertEquals(2, Visits().visitHouses("^v^v^v^v^v").size)
    }
    @Test
    fun `part one`() {
        assertEquals(2081, Visits().visitHouses(puzzleInput).size)
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