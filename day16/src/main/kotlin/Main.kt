data class Compound(val name:String, val qty:Int)

fun List<String>.toValidationMap(isPartTwo:Boolean = false):Map<String, (Compound) -> Boolean> {
    val compoundMap = mutableMapOf<String, (Compound) -> Boolean>()
    forEach{ line ->
        val name = line.toCompoundName()
        val qty = line.toCompoundQty()
        if (isPartTwo) {
            when (name) {
                "cats","trees" -> compoundMap[name] = {compound:Compound -> compound.qty > qty  }
                "pomeranians","goldfish" -> compoundMap[name] = {compound:Compound -> compound.qty < qty  }
                else ->  compoundMap[name] = {compound:Compound -> compound.qty == qty  }
            }
        }
        else compoundMap[name] = {compound:Compound -> compound.qty == qty  }
    }
    return compoundMap
}

fun List<String>.toCompound():List<Compound> = map{Compound(it.toCompoundName(), it.toCompoundQty())}

fun String.toCompoundName() = split(": ")[0]
fun String.toCompoundQty() = split(": ")[1].toInt()

data class Sue(val number:Int, val compounds:List<Compound>) {
    fun isOK(validatorMap:Map<String, (Compound) -> Boolean> ) =
        compounds.all{ compound -> validatorMap.getValue(compound.name)(compound) }
}

fun parseSue(data:List<String>):List<Sue> = data.map{Sue(it.toSueNumber(), it.toSueCompounds())}

fun String.toSueNumber() = removePrefix("Sue ").split(":")[0].toInt()
fun String.toSueCompounds() = split(" ").drop(2).joinToString(" ").split(", ").toCompound()

fun partOne(tickerTapeData:List<String>, data:List<String>):Int {
    val validatorMap = tickerTapeData.toValidationMap()
    return parseSue(data).filter{it.isOK(validatorMap)}.first().number
}

fun partTwo(tickerTapeData:List<String>, data:List<String>):Int {
    val validatorMap = tickerTapeData.toValidationMap(isPartTwo = true)
    return parseSue(data).filter{it.isOK(validatorMap)}.first().number

}
