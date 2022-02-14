import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `casting magic missile`() {
        val boss = Boss(8,50)
        val player = Player(0,150,20)
        val spell = Spell(SpellName.MagicMissile, 0)
        val (_, updatedBoss) = GameStatus(player, boss, listOf(spell)).applyInstantEffects()
        assertEquals( 46 , updatedBoss.hitPoints )
    }

    @Test
    fun `casting drain`() {
        val boss = Boss(8,50)
        val player = Player(0,150,20)
        val spell = Spell(SpellName.Drain, 0)
        val (updatedPlayer, updatedBoss) = GameStatus(player, boss, listOf(spell)).applyInstantEffects()
        assertEquals( 48 , updatedBoss.hitPoints)
        assertEquals( 152 , updatedPlayer.hitPoints)
    }

    @Test
    fun `casting shield`() {
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val spell = Spell(SpellName.Shield, 6)
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn(
            isHard = false
        )
        assertEquals(boss, updatedBoss)
        assertEquals(7, updatedPlayer.armour)
        assertEquals(5, updatedSpells.first().life)
    }

    @Test
    fun `casting poison`() {
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val spell = Spell(SpellName.Poison, 6)
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn(
            isHard = false
        )
        assertEquals(player, updatedPlayer)
        assertEquals(boss.hitPoints - 3, updatedBoss.hitPoints)
        assertEquals(5, updatedSpells.first().life)
    }

    @Test
    fun `casting recharge`() {
        val spell = Spell(SpellName.Recharge, 6)
        val player = Player(0,150,20)
        val boss = Boss(8,50)
        val (updatedPlayer, updatedBoss, updatedSpells) = GameStatus(player, boss, listOf(spell)).applyEffectBeforeTurn(
            isHard = false
        )
        assertEquals(boss, updatedBoss)
        assertEquals(player.mana + 101, updatedPlayer.mana)
        assertEquals(5, updatedSpells.first().life)
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