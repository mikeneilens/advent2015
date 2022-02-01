import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainTest {
    val tickerTapeData = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1
    """.trimIndent().split("\n")

    @Test
    fun `parsing list of strings to compounds`() {
        val compounds = tickerTapeData.toCompound()
        assertEquals(10, compounds.size)
        assertEquals(Compound("children",3), compounds[0])
        assertEquals(Compound("perfumes",1), compounds[9])
    }
    @Test
    fun `parsing list of strings to list of sue`() {
        val sampleData = """
            Sue 1: goldfish: 9, cars: 0, samoyeds: 9
            Sue 2: perfumes: 5, trees: 8, goldfish: 8
            Sue 3: pomeranians: 2, akitas: 1, trees: 5
            Sue 4: goldfish: 10, akitas: 2, perfumes: 9
        """.trimIndent().split("\n")
        val sues = parseSue(sampleData)
        assertEquals(4, sues.size)
        assertEquals(Sue(1,listOf(Compound("goldfish",9),Compound("cars",0),Compound("samoyeds",9))), sues[0])
        assertEquals(Sue(4,listOf(Compound("goldfish",10),Compound("akitas",2),Compound("perfumes",9))), sues[3])
    }
    @Test
    fun `if a sue does not match a ticker tape`() {
        val validationMap = tickerTapeData.toValidationMap()
        val sue1 = Sue(1, listOf(Compound("children",3),Compound("perfumes",1),Compound("goldfish",5)))
        assertTrue(sue1.isOK(validationMap))
        val sue2 = Sue(1, listOf(Compound("children",3),Compound("perfumes",1),Compound("goldfish",10)))
        assertFalse(sue2.isOK(validationMap))
    }
    @Test
    fun `part one`() {
        assertEquals(40, partOne(tickerTapeData, puzzleInput))
    }

    @Test
    fun `part two`() {
        assertEquals(241, partTwo(tickerTapeData, puzzleInput))
    }

}