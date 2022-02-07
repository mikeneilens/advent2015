data class Equipment(val name:String, val cost:Int, val damaga:Int, val armour:Int)

operator fun List<List<Equipment>>.plus( newEquipment:List<Equipment>):List<List<Equipment>>  =
    flatMap { listOfEquipment -> newEquipment.map{equipment -> listOfEquipment + equipment} }

fun List<List<Equipment>>.removeWhenLastTwoTheSame() = filter{ it.size < 2 || it.last().name != it[it.lastIndex - 1].name }

fun List<String>.parse() = map{ Equipment(it.item(0),it.item(1).toInt(),it.item(2).toInt(),it.item(3).toInt()) }

fun String.item(n:Int) = split(" ").filter {it != ""}[n]

fun List<Equipment>.damage() = sumOf{it.damaga}
fun List<Equipment>.armour() = sumOf{it.armour}
fun List<Equipment>.cost() = sumOf{it.cost}

data class Contestant(val damage:Int, val armour:Int, val hitPoints:Int) {
    operator fun minus(other:Contestant) = Contestant(damage, armour, hitPoints - maxOf(other.damage, 1) + armour)
}

enum class ResultOfGame{playerWins, BossWins}

fun playGame(player:Contestant, boss:Contestant,):ResultOfGame{
    val damagedBoss = boss - player
    if (damagedBoss.hitPoints <= 0) return ResultOfGame.playerWins
    val damagedPlayer = player - damagedBoss
    if (damagedPlayer.hitPoints <= 0) return ResultOfGame.BossWins
    return playGame(damagedPlayer, damagedBoss)
}

fun partOne(weaponsData:List<String>,armourData:List<String>,ringsData:List<String>,playerHitPoints:Int = 100, boss: Contestant  ):Int {
    val listsOfEquipment = (weaponsData.parse().map{listOf(it)} + armourData.parse() + ringsData.parse() + ringsData.parse()).removeWhenLastTwoTheSame()
    var lowestCost = Int.MAX_VALUE
    listsOfEquipment.forEach { equipmentList ->
        val player = createPlayer(equipmentList,playerHitPoints)
        if (playGame(player, boss) ==  ResultOfGame.playerWins && equipmentList.cost() < lowestCost) lowestCost =equipmentList.cost()
    }
    return lowestCost
}

fun createPlayer(equipmentList:List<Equipment>,playerHitPoints:Int ) = Contestant(equipmentList.damage(), equipmentList.armour(),playerHitPoints)