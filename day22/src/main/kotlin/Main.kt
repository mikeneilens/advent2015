data class Boss(val damage:Int, val hitPoints:Int)

data class Player(val armour:Int, val hitPoints:Int, val mana:Int)

enum class SpellName{MagicMissile, Drain, Shield, Poison, Recharge}

val lifeOfSpells = mapOf(SpellName.MagicMissile to 0, SpellName.Drain to 0, SpellName.Shield to 6, SpellName.Poison to 6, SpellName.Recharge to 5)
val costForSpells = mapOf(SpellName.MagicMissile to 53, SpellName.Drain to 73, SpellName.Shield to 113, SpellName.Poison to 173, SpellName.Recharge to 229)

data class Spell(val name:SpellName, val life:Int = lifeOfSpells[name] ?: 0, val cost:Int  = costForSpells[name] ?: 0) {
    companion object {
        val allTypes = listOf(Spell(SpellName.Poison), Spell(SpellName.MagicMissile),  Spell(SpellName.Drain), Spell(SpellName.Shield), Spell(SpellName.Recharge))
    }
}

fun Boss.applyEffectBeforeTurn(spells:List<Spell>):Boss =
    if (spells.any{it.name == SpellName.Poison}) Boss(damage, hitPoints - 3) else Boss(damage, hitPoints)

fun Player.applyEffectBeforeTurn(spells:List<Spell>, isHard:Boolean = false):Player {
    var armour = 0
    var mana = this.mana
    if (spells.any{it.name == SpellName.Shield}) armour = 7
    if (spells.any{it.name == SpellName.Recharge}) mana += 101
    return Player(armour, hitPoints - (if (isHard) 1 else 0), mana)
}

fun List<Spell>.update() = fold(listOf<Spell>()){ newSpells, spell ->
    if (spell.life > 1 ) newSpells + Spell(spell.name, spell.life - 1) else newSpells
}

data class GameStatus(val player:Player, val boss:Boss, val currentSpells:List<Spell> = listOf(), val mana:Int = player.mana ) {
    fun applyEffectBeforeTurn(isHard: Boolean):GameStatus {
        val updatedBoss = boss.applyEffectBeforeTurn(currentSpells)
        val updatePlayer = player.applyEffectBeforeTurn(currentSpells, isHard)
        val updatedSpells = currentSpells.update()
        return GameStatus(updatePlayer, updatedBoss, updatedSpells)
    }

    fun applyInstantEffects():GameStatus = when(currentSpells.last().name) {
        SpellName.MagicMissile -> GameStatus(player, Boss(boss.damage, boss.hitPoints- 4 ), currentSpells)
        SpellName.Drain -> GameStatus(Player(player.armour, player.hitPoints + 2, player.mana)
            , Boss(boss.damage, boss.hitPoints - 2 ), currentSpells
        )
        else -> this
    }

    fun addSpellAndDeductCost(newSpell: Spell):GameStatus {
        val player = Player(player.armour, player.hitPoints, mana - newSpell.cost)
        return GameStatus(player, boss, currentSpells + newSpell)
    }

    fun attackPlayer():GameStatus {
        val attackedPlayer =  if (player.armour < boss.damage) Player( player.armour, player.hitPoints + player.armour - boss.damage, player.mana )
        else Player( player.armour, player.hitPoints - 1, player.mana)
        return GameStatus(attackedPlayer, boss, currentSpells)
    }

    fun bossHasLost() = boss.hitPoints <= 0
    fun playerHasLost() = player.hitPoints <= 0

}
data class ScoreCard(var minSpellCost:Int = Int.MAX_VALUE)

fun playTurn(gameStatus:GameStatus, spellsCast:List<Spell> = listOf(), spellText:String = "", scoreCard:ScoreCard = ScoreCard(), isHard:Boolean):List<List<Spell>> {

    val spellCost = spellsCast.sumOf { it.cost }
    val gameStatusBeforePlayersTurn = gameStatus.applyEffectBeforeTurn(isHard)

    if (gameStatusBeforePlayersTurn.bossHasLost() ) return playerHasWon( spellCost, scoreCard, spellsCast)
    if (gameStatusBeforePlayersTurn.playerHasLost()) return listOf() // boss has won

    val possibleSpells = Spell.allTypes.filter{gameStatusBeforePlayersTurn.mana >= it.cost  && it.name !in gameStatusBeforePlayersTurn.currentSpells.map{spell -> spell.name} && spellCost + it.cost < scoreCard.minSpellCost}

    return possibleSpells.flatMap{ newSpell ->
        val gameStatusAfterPlayersTurn = gameStatusBeforePlayersTurn
            .addSpellAndDeductCost(newSpell)
            .applyInstantEffects()

        val gameStatusBeforeBossesTurn = gameStatusAfterPlayersTurn.applyEffectBeforeTurn(isHard)
        val gameStatusAfterBossesTurn = gameStatusBeforeBossesTurn.attackPlayer()

        when {
            gameStatusAfterPlayersTurn.bossHasLost() || gameStatusBeforeBossesTurn.bossHasLost() ->
                playerHasWon( spellCost + newSpell.cost, scoreCard, spellsCast + newSpell)
            gameStatusBeforeBossesTurn.playerHasLost() || gameStatusAfterBossesTurn.playerHasLost() ->
                listOf()
            else ->
                playTurn( gameStatusAfterBossesTurn, spellsCast + newSpell, "$spellText ${newSpell.name}", scoreCard, isHard)
        }
    }
}

fun playerHasWon(spellCost: Int, scoreCard: ScoreCard, spellsCast: List<Spell>): List<List<Spell>> {
    if (spellCost < scoreCard.minSpellCost) scoreCard.minSpellCost = spellCost
    return listOf(spellsCast)
}

fun partOne(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss), isHard = false)
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}

fun partTwo(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss),isHard = true)
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}
