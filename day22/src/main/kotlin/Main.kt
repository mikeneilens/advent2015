data class Boss(val damage:Int, val hitPoints:Int) {
    fun hasLost() = hitPoints <= 0
}

data class Player(val armour:Int, val hitPoints:Int, val mana:Int) {
    fun hasLost() = hitPoints <= 0
}

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

fun applyEffectBeforeTurn(player:Player, boss:Boss, spells:List<Spell>, isHard:Boolean):Triple<Player, Boss, List<Spell>> {
    val updatedBoss = boss.applyEffectBeforeTurn(spells)
    val updatePlayer = player.applyEffectBeforeTurn(spells, isHard)
    val updatedSpells = spells.update()
    return Triple(updatePlayer, updatedBoss, updatedSpells)
}

fun applyInstantEffects(player:Player, boss:Boss, spell:Spell):Pair<Player, Boss> = when(spell.name) {
    SpellName.MagicMissile -> Pair(player, Boss(boss.damage,boss.hitPoints- 4 ))
    SpellName.Drain -> Pair(Player(player.armour, player.hitPoints + 2, player.mana), Boss(boss.damage,boss.hitPoints - 2 ))
    else -> Pair(player, boss)
}

fun attackPlayer(player: Player, boss: Boss):Player =
    if (player.armour < boss.damage) Player( player.armour, player.hitPoints + player.armour - boss.damage, player.mana )
    else Player( player.armour, player.hitPoints - 1, player.mana)

data class ScoreCard(var minSpellCost:Int = Int.MAX_VALUE)

fun playTurn(player:Player, boss:Boss, currentSpells:List<Spell> = listOf(), spellsCast:List<Spell> = listOf(), spellText:String = "", scoreCard:ScoreCard = ScoreCard(), isHard:Boolean):List<List<Spell>> {

    val spellCost = spellsCast.sumOf { it.cost }

    val (playerBeforePlayersTurn, bossBeforePlayersTurn, spellsBeforePlayersTurn) = applyEffectBeforeTurn(player, boss, currentSpells, isHard)

    if (bossBeforePlayersTurn.hasLost() ) return playerHasWon(playerBeforePlayersTurn, bossBeforePlayersTurn, spellText, spellCost, scoreCard, spellsCast)
    if (playerBeforePlayersTurn.hasLost()) return listOf() // boss has won

    val possibleSpells = Spell.allTypes.filter{playerBeforePlayersTurn.mana >= it.cost  && it.name !in spellsBeforePlayersTurn.map{spell -> spell.name} && spellCost + it.cost < scoreCard.minSpellCost}

    return possibleSpells.flatMap{ newSpell ->
        val playerAfterCostOfSpellDeducted = Player(playerBeforePlayersTurn.armour, playerBeforePlayersTurn.hitPoints, playerBeforePlayersTurn.mana - newSpell.cost)

        val (playerAfterPlayersTurn, bossAfterPlayersTurn) = applyInstantEffects(playerAfterCostOfSpellDeducted, bossBeforePlayersTurn, newSpell)

        if (bossAfterPlayersTurn.hasLost()) {
            playerHasWon(playerBeforePlayersTurn, bossBeforePlayersTurn, spellText + newSpell.name,  spellCost + newSpell.cost, scoreCard, spellsCast + newSpell)
        } else {
            val (playerBeforeBossesTurn, bossBeforeBossesTurn, spellsBeforeBossesTurn) = applyEffectBeforeTurn(playerAfterPlayersTurn, bossAfterPlayersTurn, spellsBeforePlayersTurn + newSpell , isHard)
            if (bossBeforeBossesTurn.hasLost() ) {
                playerHasWon(playerBeforeBossesTurn, bossBeforeBossesTurn, spellText + newSpell.name,  spellCost + newSpell.cost, scoreCard, spellsCast + newSpell)
            } else if (playerBeforeBossesTurn.hasLost()) {
                listOf() //boss has won
            } else {
                val playerAfterBossesTurn = attackPlayer(playerBeforeBossesTurn, bossBeforeBossesTurn)
                if (playerAfterBossesTurn.hasLost()) {
                    listOf() //boss has won
                } else {
                    playTurn(playerAfterBossesTurn, bossBeforeBossesTurn, spellsBeforeBossesTurn, spellsCast + newSpell, "$spellText ${newSpell.name}", scoreCard, isHard)
                }
            }
        }
    }
}

fun playerHasWon(player: Player, boss: Boss, spellText: String, spellCost: Int, scoreCard: ScoreCard, spellsCast: List<Spell>): List<List<Spell>> {
    println("Player wins hit point:${player.hitPoints} boss hit points:${boss.hitPoints}  spells: $spellText")
    if (spellCost < scoreCard.minSpellCost) scoreCard.minSpellCost = spellCost
    return listOf(spellsCast)
}

fun partOne(player:Player, boss:Boss):Int {
    val result = playTurn(player,boss, isHard = false)
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}

fun partTwo(player:Player, boss:Boss):Int {
    val result = playTurn(player,boss,isHard = true)
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}
