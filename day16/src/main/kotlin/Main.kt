data class Compound(val name:String, val qty:Int)

typealias ValidationRules = Map<String, (Compound) -> Boolean >

fun List<String>.toValidationRules(isPartTwo:Boolean = false):ValidationRules {
    val validationRules = mutableMapOf<String, (Compound) -> Boolean>()
    forEach{ line ->
        val name = line.toCompoundName()
        val qty = line.toCompoundQty()
        validationRules[name] =  if (isPartTwo) rulesForPartTwo(name, qty) else rulesForPartOne(qty)
    }
    return validationRules
}

fun rulesForPartOne( qty: Int ) = { compound: Compound -> compound.qty == qty }

fun rulesForPartTwo(name: String, qty: Int) = when (name) {
        "cats", "trees" ->  { compound: Compound -> compound.qty > qty }
        "pomeranians", "goldfish" ->  { compound: Compound -> compound.qty < qty }
        else -> rulesForPartOne(qty)
    }

fun List<String>.toCompound():List<Compound> = map{Compound(it.toCompoundName(), it.toCompoundQty())}

fun String.toCompoundName() = split(": ")[0]
fun String.toCompoundQty() = split(": ")[1].toInt()

data class Sue(val number:Int, val compounds:List<Compound>) {
    fun isValid(validatorRules:ValidationRules ) = compounds.allValid(validatorRules)
}

fun List<Compound>.allValid(validatorRules:ValidationRules) = all{ compound -> validatorRules.getValue(compound.name)(compound) }

fun parseSue(data:List<String>) = data.map{Sue(it.toSueNumber(), it.toSueCompounds())}

fun String.toSueNumber() = removePrefix("Sue ").split(":")[0].toInt()
fun String.toSueCompounds() = split(" ").drop(2).joinToString(" ").split(", ").toCompound()

fun partOne(tickerTapeData:List<String>, data:List<String>):Int {
    val validatorRules = tickerTapeData.toValidationRules()
    return parseSue(data).first{it.isValid(validatorRules)}.number
}

fun partTwo(tickerTapeData:List<String>, data:List<String>):Int {
    val validatorRules = tickerTapeData.toValidationRules(isPartTwo = true)
    return parseSue(data).first{it.isValid(validatorRules)}.number
}
