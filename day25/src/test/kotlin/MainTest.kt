import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `code after 20151125 is 31916031`(){
        val code = Code(1,1, 20151125L)
        assertEquals(31916031L, code.next().num)
    }
    @Test
    fun `part one using 1,2 as the target`() {
        assertEquals(31916031L, parOne(1,2).num)
    }
    @Test
    fun `part one`() {
        assertEquals(2650453L, parOne(3083,2978).num)
    }

}