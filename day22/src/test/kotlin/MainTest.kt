import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `casting magic missile`() {
        val boss = Boss(8,50)
        val player = Player(0,150,20)
        val spell = MagicMissle()
        val (_, updatedBoss) = GameStatus(player, boss, listOf(spell)).applyInstantEffects()
        assertEquals( 46 , updatedBoss.hitPoints )
    }

    @Test
    fun `casting drain`() {
        val boss = Boss(8,50)
        val player = Player(0,150,20)
        val spell = Drain()
        val (updatedPlayer, updatedBoss) = GameStatus(player, boss, listOf(spell)).applyInstantEffects()
        assertEquals( 48 , updatedBoss.hitPoints)
        assertEquals( 152 , updatedPlayer.hitPoints)
    }

    @Test
    fun `casting shield`() {
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val spell = Shield()
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn()
        assertEquals(boss, updatedBoss)
        assertEquals(7, updatedPlayer.armour)
        assertEquals(5, updatedSpells.first().lifeLeft)
    }

    @Test
    fun `casting poison`() {
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val spell = Poison()
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn()
        assertEquals(player, updatedPlayer)
        assertEquals(boss.hitPoints - 3, updatedBoss.hitPoints)
        assertEquals(5, updatedSpells.first().lifeLeft)
    }

    @Test
    fun `casting recharge`() {
        val spell = Recharge()
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn()
        assertEquals(boss, updatedBoss)
        assertEquals(player.mana + 101, updatedPlayer.mana)
        assertEquals(4, updatedSpells.first().lifeLeft)
    }

    @Test
    fun `playing a game from first example`() {
        val player = Player(0,10,250)
        val boss = Boss(8,13)
        assertEquals(226,partOne(player, boss))
    }

    @Test
    fun `playing a game from second example`() {
        val player = Player(0,10,250)
        val boss = Boss(8,14)
        assertEquals(641,partOne(player, boss))
    }

    @Test
    fun `part one`() {
        val player = Player(0,50, 500)
        val boss = Boss(9,58)
        assertEquals(1269, partOne(player, boss))
    }

    @Test
    fun `part Two`() {
        val player = Player(0,50, 500)
        val boss = Boss(9,58)
        assertEquals(1309, partTwo(player, boss))
    }

}