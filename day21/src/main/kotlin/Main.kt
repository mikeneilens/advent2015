data class Equipment(val name:String, val cost:Int, val damage:Int, val armour:Int)

operator fun List<List<Equipment>>.plus( newEquipment:List<Equipment>):List<List<Equipment>>  =
    flatMap { listOfEquipment -> newEquipment.map{equipment -> listOfEquipment + equipment} }

fun List<List<Equipment>>.removeWhenLastTwoTheSame() = filter{ it.size < 2 || it.last().name != it[it.lastIndex - 1].name }

fun List<String>.parse() = map{ Equipment(it.item(0),it.item(1).toInt(),it.item(2).toInt(),it.item(3).toInt()) }

fun String.item(n:Int) = split(" ").filter {it != ""}[n]

fun List<Equipment>.damage() = sumOf{it.damage}
fun List<Equipment>.armour() = sumOf{it.armour}
fun List<Equipment>.cost() = sumOf{it.cost}

data class Contestant(val damage:Int, val armour:Int, val hitPoints:Int, val equipmentCost:Int = 0) {
    operator fun minus(other:Contestant) = Contestant(damage, armour, hitPoints - maxOf(other.damage, 1) + armour, equipmentCost)
}

enum class ResultOfGame{PlayerWins, BossWins, Draw}

fun playGame(player:Contestant, boss:Contestant):ResultOfGame = when {
    (boss - player).hitPoints <= 0 -> ResultOfGame.PlayerWins
    (player - boss).hitPoints <= 0 -> ResultOfGame.BossWins
    (boss - player).hitPoints >= boss.hitPoints && (player - boss).hitPoints >= player.hitPoints -> ResultOfGame.Draw
    else -> playGame(player - boss, boss - player)
}

fun partOne(weaponsData:List<String>, armourData:List<String>, ringsData:List<String>, playerHitPoints:Int, boss:Contestant ) =
    (weaponsData.parse().map{listOf(it)} + armourData.parse() + ringsData.parse() + ringsData.parse())
        .removeWhenLastTwoTheSame()
        .map{createPlayer(it, playerHitPoints)}
        .filter{ player -> playGame(player, boss) ==  ResultOfGame.PlayerWins }
        .minOf { it.equipmentCost }

fun createPlayer(equipmentList:List<Equipment>,playerHitPoints:Int ) = Contestant(equipmentList.damage(), equipmentList.armour(), playerHitPoints, equipmentList.cost())

fun partTwo(weaponsData: List<String>, armourData:List<String>, ringsData:List<String>, playerHitPoints:Int, boss:Contestant):Int =
    mostExpensiveWayToLose((weaponsData +  armourData + ringsData).parse(),playerHitPoints, boss)

fun mostExpensiveWayToLose(equipmentList:List<Equipment>, playerHitPoints: Int, boss: Contestant) =
    permutations(equipmentList.lastIndex).map{ permutation -> createPlayer( permutation.map{ equipmentList[it]}, playerHitPoints)}
        .filter{ player -> playGame(player, boss) ==  ResultOfGame.BossWins }
        .maxOf{it.equipmentCost}

fun permutations(max:Int, n:Int = 1, l:Set<Set<Int>> = setOf(setOf(0))):Set<Set<Int>> =
    if (n > max)  l else permutations(max, n + 1, l + l.map{it + n})

