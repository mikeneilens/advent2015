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
    operator fun minus(other:Contestant) = if (armour >= other.damage) Contestant(damage, armour, hitPoints - 1) else
        Contestant(damage, armour, hitPoints - other.damage + armour, equipmentCost)
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
    permutations(equipmentList.lastIndex)
        .filter(::permutationContainsAtLeastOneWeapon)
        .map{ permutation -> createPlayer( permutation.map{ equipmentList[it]}, playerHitPoints)}
        .filter{ player -> playGame(player, boss) ==  ResultOfGame.BossWins }
        .maxOf{it.equipmentCost}

fun permutationContainsAtLeastOneWeapon(permutation:Set<Int>, indicesOfWeapons:List<Int> = listOf(0,1,2,3,4)) = indicesOfWeapons.any{it in permutation}

//all non-repeating permutations. e.g. contains (0,1,2) but not (0,2,1) or (1,2,0) or (2,1,0) as they are the same
fun permutations(max:Int, n:Int = 0, set:Set<Set<Int>> = setOf(setOf())):Set<Set<Int>> =
    if (n == max)  set else permutations(max, n + 1, set + set.map{it + n})

