import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    val weaponsData = """
        Dagger        8     4       0
        Shortsword   10     5       0
        Warhammer    25     6       0
        Longsword    40     7       0
        Greataxe     74     8       0
    """.trimIndent().split("\n")
    val armorData = """
        Leather      13     0       1
        Chainmail    31     0       2
        Splintmail   53     0       3
        Bandedmail   75     0       4
        Platemail   102     0       5
    """.trimIndent().split("\n")
    val ringsData = """
        Damage+1    25     1       0
        Damage+2    50     2       0
        Damage+3   100     3       0
        Defense+1   20     0       1
        Defense+2   40     0       2
        Defense+3   80     0       3
        Damage+0     0     0       0
        Defense+0    0     0       0
    """.trimIndent().split("\n")

    @Test
    fun `adding a list of equipment to another list of equipment`() {
        val weapons = listOf(
            listOf(Equipment("Dagger", 8, 4, 0)),
            listOf(Equipment("Shortsword", 10, 5, 0)),
            listOf(Equipment("Warhammer", 25, 6, 0))
        )
        val armor = listOf(
            Equipment("Leather", 13, 0, 1),
            Equipment("Chainmail", 31, 0, 2)
        )
        val expectedResult = listOf(
            listOf(Equipment("Dagger", 8, 4, 0),Equipment("Leather", 13, 0, 1)),
            listOf(Equipment("Dagger", 8, 4, 0),Equipment("Chainmail", 31, 0, 2)),
            listOf(Equipment("Shortsword", 10, 5, 0),Equipment("Leather", 13, 0, 1)),
            listOf(Equipment("Shortsword", 10, 5, 0),Equipment("Chainmail", 31, 0, 2)),
            listOf(Equipment("Warhammer", 25, 6, 0),Equipment("Leather", 13, 0, 1)),
            listOf(Equipment("Warhammer", 25, 6, 0),Equipment("Chainmail", 31, 0, 2))
        )
        assertEquals(expectedResult, weapons + armor)
    }
    @Test
    fun `remove lists of equipment where last two items are the same`() {
        val weapons = listOf(
            listOf(Equipment("Dagger", 8, 4, 0),Equipment("Dagger", 8, 4, 0)),
            listOf(Equipment("Shortsword", 10, 5, 0),Equipment("Leather", 13, 0, 1)),
            listOf(Equipment("Warhammer", 25, 6, 0),Equipment("Chainmail", 31, 0, 2))
        )
        val expectedResult = listOf(
            listOf(Equipment("Shortsword", 10, 5, 0),Equipment("Leather", 13, 0, 1)),
            listOf(Equipment("Warhammer", 25, 6, 0),Equipment("Chainmail", 31, 0, 2))
        )
        assertEquals(expectedResult, weapons.removeWhenLastTwoTheSame())
    }
    @Test
    fun `parsing equipment data`() {
        val expectedEquipment  = listOf(
            Equipment("Dagger", 8, 4, 0),
            Equipment("Shortsword", 10, 5, 0),
            Equipment("Warhammer", 25, 6, 0),
            Equipment("Longsword", 40, 7, 0),
            Equipment("Greataxe", 74, 8, 0),
        )
        assertEquals(expectedEquipment, weaponsData.parse())
    }
    @Test
    fun `playing a game where player has 5 damage and 5 armour and boss has 7 damage and 2 armour `() {
        val boss = Contestant(7,2,12, )
        val player = Contestant(5,5,8)
        assertEquals(ResultOfGame.PlayerWins, playGame(player, boss))
    }
    @Test
    fun `part one`() {
        val boss = Contestant(8,2,109 )
        assertEquals(111, partOne(weaponsData,armorData,ringsData,100, boss))
    }

    @Test
    fun `permuations that contain unique digits in any sequence`() {
        assertEquals(setOf(setOf(0)), permutations(0))
        assertEquals(setOf(setOf(0),setOf(0,1)), permutations(1))
        assertEquals(setOf(setOf(0),setOf(0,1),setOf(0,2),setOf(0,1,2)), permutations(2))
        assertEquals(setOf(setOf(0),setOf(0,1),setOf(0,2),setOf(0,1,2),setOf(0,3),setOf(0,1,3),setOf(0,2,3),setOf(0,1,2,3)), permutations(3))
        assertEquals(32768, permutations(15).size)
    }
    @Test
    fun `part two`() {
        val boss = Contestant(8,2,109 )
        assertEquals(188, partTwo(weaponsData, armorData, ringsData.drop(2), 100,  boss ))
    }
}