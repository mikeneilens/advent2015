import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    private val sampleData = """
        inc a
        jio a, +2
        tpl a
        inc a
    """.trimIndent().split("\n")

    @Test
    fun `hlf r sets register r to half its current value, then continues with the next instruction`() {
        val computerStatus = ComputerStatus(0,4,7)
        assertEquals(ComputerStatus(1, 2,7), "hlf a".process(computerStatus) )
        assertEquals(ComputerStatus(1, 4,3), "hlf b".process(computerStatus) )
    }
    @Test
    fun `tpl r sets register r to triple its current value, then continues with the next instruction`() {
        val computerStatus = ComputerStatus(0,4,7)
        assertEquals(ComputerStatus(1, 12,7), "tpl a".process(computerStatus) )
        assertEquals(ComputerStatus(1, 4,21), "tpl b".process(computerStatus) )
    }
    @Test
    fun `inc r increments register r, adding 1 to it, then continues with the next instruction`() {
        val computerStatus = ComputerStatus(0,4,7)
        assertEquals(ComputerStatus(1, 5,7), "inc a".process(computerStatus) )
        assertEquals(ComputerStatus(1, 4,8), "inc b".process(computerStatus) )
    }
    @Test
    fun `jmp offset is a jump, it continues with the instruction offset away relative to itself`() {
        val computerStatus = ComputerStatus(0,4,7)
        assertEquals(ComputerStatus(1, 4,7), "jmp +1".process(computerStatus) )
        assertEquals(ComputerStatus(0, 4,7), "jmp 0".process(computerStatus) )
    }
    @Test
    fun `jie r, offset is like jmp, but only jumps if register r is even`() {
        val computerStatus = ComputerStatus(0,4,7)
        assertEquals(ComputerStatus(2, 4,7), "jie a, +2".process(computerStatus) )
        assertEquals(ComputerStatus(1, 4,7), "jie b, +2".process(computerStatus) )
    }
    @Test
    fun `jio r, offset is like jmp, but only jumps if register r is 1`() {
        val computerStatus = ComputerStatus(0,1,7)
        assertEquals(ComputerStatus(2, 1,7), "jio a, +2".process(computerStatus) )
        assertEquals(ComputerStatus(1, 1,7), "jio b, +2".process(computerStatus) )
    }
    @Test
    fun `program using sample data`() {
        val computerStatus = Program(sampleData).run()
        assertEquals(2, computerStatus.a)
        assertEquals(0, computerStatus.b)
    }

    @Test
    fun `part one`() {
        assertEquals(307, partOne(puzzleInput))
    }

    @Test
    fun `part two`() {
        assertEquals(160, partTwo(puzzleInput))
    }

}
