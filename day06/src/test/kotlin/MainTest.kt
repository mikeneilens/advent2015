import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `string to Instruction`() {
        assertEquals(Instruction(Command.TurnOn, 0..999L,0..999L), "turn on 0,0 through 999,999".toInstruction()   )
        assertEquals(Instruction(Command.Toggle, 0..999L,0..0L), "toggle 0,0 through 999,0".toInstruction()   )
        assertEquals(Instruction(Command.TurnOff, 499..500L,499..500L), "turn off 499,499 through 500,500".toInstruction()   )
    }

    @Test
    fun `part one`() {
        assertEquals(569999, processInstructions(puzzleInput, partOneRules))
    }

    @Test
    fun `incresasing brightness`() {
        val data = listOf("turn on 0,0 through 0,0")
        assertEquals(1, processInstructions(data, partTwoRules))
    }
    @Test
    fun `toggle brightness`() {
        val data = listOf("toggle 0,0 through 999,999")
        assertEquals(2000000, processInstructions(data, partTwoRules))
    }
    @Test
    fun `decreasing brightness`() {
        val data = listOf("toggle 0,0 through 999,999","turn off 0,0 through 0,0")
        assertEquals(1999999, processInstructions(data, partTwoRules))
    }
    @Test
    fun `part two`() {
        assertEquals(17836115, processInstructions(puzzleInput, partTwoRules))
    }
}