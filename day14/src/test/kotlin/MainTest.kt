import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val sampleData = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
    """.trimIndent().split("\n")
    @Test
    fun `parsing sample data`() {
        val reindeers = sampleData.parse()
        assertEquals("Comet", reindeers[0].name )
        assertEquals(10, reindeers[0].flyTime )
        assertEquals(14, reindeers[0].flySpeed )
        assertEquals(127, reindeers[0].restTime )
        assertEquals("Dancer", reindeers[1].name )
        assertEquals(11, reindeers[1].flyTime )
        assertEquals(16, reindeers[1].flySpeed )
        assertEquals(162, reindeers[1].restTime )
    }
    @Test
    fun `distance travelled by comet in 1000s is 1120`(){
       assertEquals(1120,  Reindeer("Commet",14,10,127).distanceTravelled(1000))
    }
    @Test
    fun `distance travelled by dancer in 1000s is 1056`(){
       assertEquals(1056,  Reindeer("Dancer",16,11,162).distanceTravelled(1000))
    }
    @Test
    fun `part one using sample data`() {
        assertEquals(1120, partOne(sampleData, 1000))
    }
    @Test
    fun `part one using puzzle input`() {
        assertEquals(2660, partOne(puzzleInput, 2503))
    }

    @Test
    fun `winner after 1 second using sample data`() {
        assertEquals(listOf(ReindeerTravel("Dancer",16)), sampleData.parse().winnersAfterTime(1))
    }
    @Test
    fun `winner after 140 second using sample data`() {
        assertEquals(listOf(ReindeerTravel("Comet",182)), sampleData.parse().winnersAfterTime(140))
    }
    @Test
    fun `accumulated points after 1000 seconds using sample data`() {
        assertEquals(689, sampleData.parse().accumulatePoints(1000)["Dancer"])
        assertEquals(312, sampleData.parse().accumulatePoints(1000)["Comet"])
    }
    @Test
    fun `part two using sample data`() {
        assertEquals(689, partTwo(sampleData, 1000))
    }
    @Test
    fun `part two using puzzle input`() {
        assertEquals(1256, partTwo(puzzleInput, 2503))
    }

}