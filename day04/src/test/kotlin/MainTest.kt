import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `using secret key abcdef`() {
        assertEquals(609043, partOne("abcdef"))
    }
    @Test
    fun `using secret key pqrstuv`() {
        assertEquals(1048970, partOne("pqrstuv"))
    }
    @Test
    fun `part one`() {
        assertEquals(282749, partOne("yzbqklnj"))
    }
    @Test
    fun `part two`() {
        assertEquals(9962624, partTwo("yzbqklnj"))
    }

}