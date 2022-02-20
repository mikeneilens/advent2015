data class Boss(val damage:Int, val hitPoints:Int) {
    fun reduceHitPoints(amount:Int) = Boss(damage, hitPoints - amount )

    fun applyEffectBeforeTurn(spells:List<Spell>):Boss = spells.fold(this, Boss::updateBossUsingSpell)
    fun updateBossUsingSpell(spell:Spell) = spell.updateBossBeforeTurn(this)
}

data class Player(val armour:Int, val hitPoints:Int, val mana:Int) {
    val withNoArmour by lazy {Player(0, hitPoints, mana)}
    val withArmour by lazy {Player(7, hitPoints, mana)}

    fun increaseHitPoint(amount:Int) = Player(armour, hitPoints + amount, mana)
    fun reduceHitPoint(amount:Int) = increaseHitPoint(-amount)
    fun increaseMana(amount:Int) = Player(armour, hitPoints, mana + amount)
    fun reduceMana(amount:Int) = increaseMana(-amount)
    fun attackPlayer(damage:Int) = if (armour < damage) reduceHitPoint(damage - armour) else reduceHitPoint(1)

    fun applyEffectBeforeTurn(spells:List<Spell>):Player = spells.fold(this.withNoArmour, Player::updatePlayerUsingSpell)
    fun updatePlayerUsingSpell(spell:Spell) = spell.updatePlayerBeforeTurn(this)
}

abstract class Spell(val name:String, val cost:Int, val lifeLeft:Int) {
    open fun updatePlayerBeforeTurn(player:Player):Player = player
    open fun updateBossBeforeTurn(boss:Boss):Boss = boss
    open fun instantEffectOnPlayer(player:Player) = player
    open fun instantEffectOnBoss(boss:Boss) = boss
    open fun reduceLife():Spell = this
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

fun List<Spell>.reduceLife() = fold(listOf<Spell>()){ newSpells, spell -> if (spell.lifeLeft > 1 ) newSpells + spell.reduceLife() else newSpells}

data class GameStatus(val player:Player, val boss:Boss, val currentSpells:List<Spell> = listOf(), val mana:Int = player.mana ) {

    fun applyEffectBeforeTurn() =GameStatus (
        player.applyEffectBeforeTurn(currentSpells),
        boss.applyEffectBeforeTurn(currentSpells),
        currentSpells.reduceLife()
    )

    fun applyInstantEffects() = GameStatus(
        currentSpells.last().instantEffectOnPlayer(player),
        currentSpells.last().instantEffectOnBoss(boss),
        currentSpells
    )

    fun addSpellAndDeductCost(newSpell: Spell):GameStatus =
        GameStatus(player.reduceMana(newSpell.cost), boss, currentSpells + newSpell)

    fun attackPlayer():GameStatus = GameStatus(player.attackPlayer(boss.damage), boss, currentSpells)

    fun playerHasWon() = boss.hitPoints <= 0
    fun bossHasWon() = player.hitPoints <= 0
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
