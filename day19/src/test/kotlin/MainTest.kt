import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTest {
    val sampleData = """
        H => HO
        H => OH
        O => HH
    """.trimIndent().split("\n")

    @Test
    fun `parseing sample data into replacements`(){
        val replacements = parse(sampleData)
        assertEquals( "H => HO", replacements[0].toString())
        assertEquals( "H => OH", replacements[1].toString())
        assertEquals( "O => HH", replacements[2].toString())
    }
    @Test
    fun `inserting text into molecule to create a new molecule`() {
        assertEquals("HOOH", "HOH".newMolecule(0, 1,"HO"))
        assertEquals("HHHH", "HOH".newMolecule(1,1,"HH"))
        assertEquals("HOHO", "HOH".newMolecule(2,1,"HO"))
    }

    @Test
    fun `finding new molecules after 1 replacement of HOH`() {
        val newMolecules = newMolecules("HOH",parse(sampleData))
        assertTrue("HOOH" in newMolecules)
        assertTrue("HOHO" in newMolecules)
        assertTrue("OHOH" in newMolecules)
        assertTrue("HHHH" in newMolecules)
    }
    @Test
    fun `part one using sample data`() {
        assertEquals(4, partOne(sampleData, "HOH"))
        assertEquals(7, partOne(sampleData, "HOHOHO"))
    }
    @Test
    fun `part one using puzzle input`() {
        assertEquals(518, partOne(puzzleInput, puzzleMolecule))
    }

    val sampleDataPartTwo = """
        e => H
        e => O
        H => HO
        H => OH
        O => HH
    """.trimIndent().split("\n")
    @Test
    fun `part two using sample data`() {
        assertEquals(3, partTwo(sampleDataPartTwo, "HOH"))
        assertEquals(6, partTwo(sampleDataPartTwo, "HOHOHO"))
    }
    @Test
    fun `part two using puzzle input`() {
        assertEquals(200, partTwo(puzzleInput, puzzleMolecule))
    }

}

