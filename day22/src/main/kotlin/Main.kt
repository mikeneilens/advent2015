data class Boss(val damage:Int, val hitPoints:Int) {
    fun reduceHitPoints(amount:Int) = copy(hitPoints = hitPoints - amount )
}

data class Player(val armour:Int, val hitPoints:Int, val mana:Int) {
    val withNoArmour by lazy {copy(armour = 0)}
    val withArmour by lazy {copy(armour = 7)}

    fun increaseHitPoint(amount:Int) = copy(hitPoints = hitPoints + amount)
    fun reduceHitPoint(amount:Int) = increaseHitPoint(-amount)
    fun increaseMana(amount:Int) = copy(mana = mana + amount)
    fun reduceMana(amount:Int) = increaseMana(-amount)

    fun attackPlayer(damage:Int) = if (armour < damage) reduceHitPoint(damage - armour) else reduceHitPoint(1)
}

abstract class Spell(val name:String, val cost:Int, val lifeLeft:Int) {
    open fun updatePlayerBeforeTurn(player:Player):Player = player
    open fun updateBossBeforeTurn(boss:Boss):Boss = boss
    open fun instantEffectOnPlayer(player:Player) = player
    open fun instantEffectOnBoss(boss:Boss) = boss
    open fun reduceLife():Spell = this
    fun hasLifeLeft() = lifeLeft > 0
}

class MagicMissile:Spell("Magic Missile", 53,0) {
    override fun instantEffectOnBoss(boss: Boss) = boss.reduceHitPoints(4)
}
class Drain:Spell("Drain", 73, 0) {
    override fun instantEffectOnPlayer(player: Player) = player.increaseHitPoint(2)
    override fun instantEffectOnBoss(boss: Boss) = boss.reduceHitPoints(2)
}
class Shield(lifeLeft:Int = 6):Spell("Shield",113,lifeLeft) {
    override fun updatePlayerBeforeTurn(player: Player) = player.withArmour
    override fun reduceLife() = Shield(lifeLeft - 1)
}
class Poison(lifeLeft:Int = 6):Spell("Poison", 173,lifeLeft) {
    override fun updateBossBeforeTurn(boss: Boss) = boss.reduceHitPoints(3)
    override fun reduceLife() = Poison(lifeLeft - 1)
}
class Recharge(lifeLeft:Int = 5):Spell("Recharge", 229, lifeLeft) {
    override fun updatePlayerBeforeTurn(player: Player) = player.increaseMana(101)
    override fun reduceLife() = Recharge(lifeLeft - 1)
}
class PartTwoSpell:Spell("PartTwo Spell", 99999, 99999) {
    override fun updatePlayerBeforeTurn(player: Player) = player.reduceHitPoint(1)
}

fun List<Spell>.reduceLife() =  map(Spell::reduceLife).filter(Spell::hasLifeLeft)

data class GameStatus(val player:Player, val boss:Boss, val currentSpells:List<Spell> = listOf(), val mana:Int = player.mana ) {

    fun applyEffectBeforeTurn() =GameStatus (
        applyEffectBeforeTurn(player),
        applyEffectBeforeTurn(boss),
        currentSpells.reduceLife()
    )

    fun applyInstantEffects() = GameStatus(
        currentSpells.last().instantEffectOnPlayer(player),
        currentSpells.last().instantEffectOnBoss(boss),
        currentSpells
    )

    fun addSpellAndDeductCost(newSpell: Spell):GameStatus =
        GameStatus(player.reduceMana(newSpell.cost), boss, currentSpells + newSpell)

    fun attackPlayer():GameStatus = copy(player = player.attackPlayer(boss.damage))

    fun playerHasWon() = boss.hitPoints <= 0
    fun bossHasWon() = player.hitPoints <= 0

    fun applyEffectBeforeTurn(boss: Boss):Boss = currentSpells.fold(boss, this::updateUsingSpell)
    fun updateUsingSpell(boss:Boss, spell: Spell) = spell.updateBossBeforeTurn(boss)

    fun applyEffectBeforeTurn(player: Player):Player = currentSpells.fold(player.withNoArmour, this::updateUsingSpell)
    fun updateUsingSpell(player: Player, spell: Spell) = spell.updatePlayerBeforeTurn(player)

}


data class GameInfo(var minSpellCost:Int = Int.MAX_VALUE)

val allSpells = listOf(MagicMissile(), Drain(), Shield(), Poison(), Recharge(), PartTwoSpell())

fun playTurn(gameStatus:GameStatus, spellsCast:List<Spell> = listOf(), spellText:String = "", gameInfo:GameInfo = GameInfo()):List<List<Spell>> {

    val spellCost = spellsCast.sumOf { it.cost }
    val statusBeforePlayersTurn = gameStatus.applyEffectBeforeTurn()

    if (statusBeforePlayersTurn.playerHasWon() ) return playerHasWon( gameInfo, spellsCast)
    if (statusBeforePlayersTurn.bossHasWon()) return bossHasWon()

    val possibleSpells = allSpells.filter{statusBeforePlayersTurn.mana >= it.cost  && it.name !in statusBeforePlayersTurn.currentSpells.map{ spell -> spell.name} && spellCost + it.cost < gameInfo.minSpellCost}

    return possibleSpells.flatMap{ newSpell ->
        val statusAfterPlayersTurn = statusBeforePlayersTurn
            .addSpellAndDeductCost(newSpell)
            .applyInstantEffects()

        val statusBeforeBossesTurn = statusAfterPlayersTurn.applyEffectBeforeTurn()
        val statusAfterBossesTurn = statusBeforeBossesTurn.attackPlayer()

        when {
            statusAfterPlayersTurn.playerHasWon() || statusBeforeBossesTurn.playerHasWon() -> playerHasWon( gameInfo, spellsCast + newSpell)
            statusBeforeBossesTurn.bossHasWon() || statusAfterBossesTurn.bossHasWon() -> bossHasWon()
            else ->
                playTurn( statusAfterBossesTurn, spellsCast + newSpell, "$spellText ${newSpell.name}", gameInfo)
        }
    }
}

fun playerHasWon(gameInfo: GameInfo, spellsCast: List<Spell>): List<List<Spell>> {
    val spellCost = spellsCast.sumOf { it.cost }
    if (spellCost < gameInfo.minSpellCost) gameInfo.minSpellCost = spellCost
    return listOf(spellsCast)
}
fun bossHasWon() = listOf<List<Spell>>()

fun partOne(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss))
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}

//include a never ending spell which always deducts one from hit points
fun partTwo(player:Player, boss:Boss):Int {
    val result = playTurn(GameStatus(player,boss, listOf(PartTwoSpell())))
    return  result.minOf{ it.sumOf {spell -> spell.cost } }
}
