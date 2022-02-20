data class Boss(val damage:Int, val hitPoints:Int)

data class Player(val armour:Int, val hitPoints:Int, val mana:Int)

abstract class Spell(val name:String, val cost:Int, val lifeLeft:Int) {
    open fun updatePlayerBeforeTurn(player:Player):Player = player
    open fun updateBossBeforeTurn(boss:Boss):Boss = boss
    open fun instantEffectOnPlayer(player:Player) = player
    open fun instantEffectOnBoss(boss:Boss) = boss
    open fun reduceLife():Spell = this
}
class MagicMissle():Spell("Magic Missile", 53,0) {
    override fun instantEffectOnBoss(boss: Boss) = Boss(boss.damage, boss.hitPoints- 4 )
}
class Drain():Spell("Drain", 73, 0) {
    override fun instantEffectOnPlayer(player: Player) = Player(player.armour, player.hitPoints + 2, player.mana)
    override fun instantEffectOnBoss(boss: Boss) = Boss(boss.damage, boss.hitPoints - 2 )
}
class Shield(lifeLeft:Int = 6):Spell("Shield",113,lifeLeft) {
    override fun updatePlayerBeforeTurn(player: Player) = Player(7, player.hitPoints, player.mana)
    override fun reduceLife() = Shield(lifeLeft -1)
}
class Poison(lifeLeft:Int = 6):Spell("Poison", 173,lifeLeft) {
    override fun updateBossBeforeTurn(boss: Boss) = Boss(boss.damage, boss.hitPoints - 3)
    override fun reduceLife() = Poison(lifeLeft -1)
}
class Recharge(lifeLeft:Int = 5):Spell("Recharge", 229, lifeLeft) {
    override fun updatePlayerBeforeTurn(player: Player) = Player(player.armour, player.hitPoints,player.mana + 101)
    override fun reduceLife() = Recharge(lifeLeft -1)
}
class PartTwoSpell():Spell("PartTwo Spell", 99999, 99999) {
    override fun updatePlayerBeforeTurn(player: Player) = Player(player.armour, player.hitPoints -1 , player.mana)
}

fun Boss.applyEffectBeforeTurn(spells:List<Spell>):Boss = spells.fold(this, ::updateBossUsingSpell)

fun Player.applyEffectBeforeTurn(spells:List<Spell>):Player = spells.fold(Player(0, this.hitPoints, this.mana), ::updatePlayerUsingSpell)

fun updateBossUsingSpell(boss:Boss, spell:Spell) = spell.updateBossBeforeTurn(boss)
fun updatePlayerUsingSpell(player:Player, spell:Spell) = spell.updatePlayerBeforeTurn(player)

fun List<Spell>.update() = fold(listOf<Spell>()){ newSpells, spell -> if (spell.lifeLeft > 1 ) newSpells + spell.reduceLife() else newSpells}

data class GameStatus(val player:Player, val boss:Boss, val currentSpells:List<Spell> = listOf(), val mana:Int = player.mana ) {

    fun applyEffectBeforeTurn():GameStatus {
        val updatedBoss = boss.applyEffectBeforeTurn(currentSpells)
        val updatePlayer = player.applyEffectBeforeTurn(currentSpells)
        val updatedSpells = currentSpells.update()
        return GameStatus(updatePlayer, updatedBoss, updatedSpells)
    }

    fun applyInstantEffects() = GameStatus(
        currentSpells.last().instantEffectOnPlayer(player),
        currentSpells.last().instantEffectOnBoss(boss),
        currentSpells
    )

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
    fun playerHasWon() = boss.hitPoints <= 0
    fun bossHasWon() = player.hitPoints <= 0
}

data class GameInfo(var minSpellCost:Int = Int.MAX_VALUE)

val allSpells = listOf(MagicMissle(), Drain(), Shield(), Poison(), Recharge(), PartTwoSpell())

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
