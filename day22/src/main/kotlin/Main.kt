data class Boss(val damage:Int, val hitPoints:Int)

data class Player(val armour:Int, val hitPoints:Int, val mana:Int)

enum class SpellName(val cost:Int, val life:Int){
    MagicMissile(53,0),
    Drain(73,0),
    Shield(113,6),
    Poison(173,6),
    Recharge(229,5),
    PartTwoSpell(99999,99999) //too expensive to buy and it never runs out.
}

data class Spell(val name:SpellName, val lifeLeft:Int = name.life, val cost:Int  = name.cost) {
    companion object {
        val allTypes = SpellName.values().map(::Spell)
    }
}

fun Boss.applyEffectBeforeTurn(spells:List<Spell>):Boss =
    if (spells.any{it.name == SpellName.Poison}) Boss(damage, hitPoints - 3) else Boss(damage, hitPoints)

fun Player.applyEffectBeforeTurn(spells:List<Spell>):Player {
    val updatedArmour = if (spells.any{it.name == SpellName.Shield}) 7 else 0
    val updatedMana = if (spells.any{it.name == SpellName.Recharge}) this.mana + 101 else this.mana
    val updatedHitPoints =  if (spells.any{it.name == SpellName.PartTwoSpell}) this.hitPoints - 1 else this.hitPoints
    return Player(updatedArmour, updatedHitPoints, updatedMana)
}

fun List<Spell>.update() = fold(listOf<Spell>()){ newSpells, spell ->
    if (spell.lifeLeft > 1 ) newSpells + Spell(spell.name, spell.lifeLeft - 1) else newSpells
}

data class GameStatus(val player:Player, val boss:Boss, val currentSpells:List<Spell> = listOf(), val mana:Int = player.mana ) {

    fun applyEffectBeforeTurn():GameStatus {
        val updatedBoss = boss.applyEffectBeforeTurn(currentSpells)
        val updatePlayer = player.applyEffectBeforeTurn(currentSpells)
        val updatedSpells = currentSpells.update()
        return GameStatus(updatePlayer, updatedBoss, updatedSpells)
    }

    fun applyInstantEffects() = when(currentSpells.last().name) {
        SpellName.MagicMissile -> GameStatus(
            player,
            Boss(boss.damage, boss.hitPoints- 4 ),
            currentSpells)
        SpellName.Drain -> GameStatus(
            Player(player.armour, player.hitPoints + 2, player.mana),
            Boss(boss.damage, boss.hitPoints - 2 ),
            currentSpells
        )
        else -> this
    }

    fun addSpellAndDeductCost(newSpell: Spell):GameStatus {
        val player = Player(player.armour, player.hitPoints, mana - newSpell.cost)
        return GameStatus(player, boss, currentSpells + newSpell)
    }

    fun attackPlayer():GameStatus {
        val attackedPlayer =
            if (player.armour < boss.damage) Player( player.armour, player.hitPoints + player.armour - boss.damage, player.mana )
            else Player( player.armour, player.hitPoints - 1, player.mana)
        return GameStatus(attackedPlayer, boss, currentSpells)
    }

    fun bossHasLost() = boss.hitPoints <= 0
    fun playerHasLost() = player.hitPoints <= 0

}
data class GameInfo(var minSpellCost:Int = Int.MAX_VALUE)

fun playTurn(gameStatus:GameStatus, spellsCast:List<Spell> = listOf(), spellText:String = "", gameInfo:GameInfo = GameInfo()):List<List<Spell>> {

    val spellCost = spellsCast.sumOf { it.cost }
    val statusBeforePlayersTurn = gameStatus.applyEffectBeforeTurn()

    if (statusBeforePlayersTurn.bossHasLost() ) return playerHasWon( spellCost, gameInfo, spellsCast)
    if (statusBeforePlayersTurn.playerHasLost()) return listOf() // boss has won

    val possibleSpells = Spell.allTypes.filter{statusBeforePlayersTurn.mana >= it.cost  && it.name !in statusBeforePlayersTurn.currentSpells.map{ spell -> spell.name} && spellCost + it.cost < gameInfo.minSpellCost}

    return possibleSpells.flatMap{ newSpell ->
        val statusAfterPlayersTurn = statusBeforePlayersTurn
            .addSpellAndDeductCost(newSpell)
            .applyInstantEffects()

        val statusBeforeBossesTurn = statusAfterPlayersTurn.applyEffectBeforeTurn()
        val statusAfterBossesTurn = statusBeforeBossesTurn.attackPlayer()

        when {
            statusAfterPlayersTurn.bossHasLost() || statusBeforeBossesTurn.bossHasLost() ->
                playerHasWon( spellCost + newSpell.cost, gameInfo, spellsCast + newSpell)
            statusBeforeBossesTurn.playerHasLost() || statusAfterBossesTurn.playerHasLost() ->
                listOf()
            else ->
                playTurn( statusAfterBossesTurn, spellsCast + newSpell, "$spellText ${newSpell.name}", gameInfo)
        }
    }
}

fun playerHasWon(spellCost: Int, scoreCard: GameInfo, spellsCast: List<Spell>): List<List<Spell>> {
    if (spellCost < scoreCard.minSpellCost) scoreCard.minSpellCost = spellCost
    return listOf(spellsCast)
}

fun partOne(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss))
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}

//include a never ending spell which always deducts one from hit points
fun partTwo(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss, listOf(Spell(SpellName.PartTwoSpell))))
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}
