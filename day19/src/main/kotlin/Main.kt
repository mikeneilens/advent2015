data class Replacement(val old:List<Char>, val new:String) {
    override fun toString() = old.joinToString("") + " => " + new

    fun reverse():Replacement = Replacement(new.toList(), old.joinToString(""))
}

fun parse(data:List<String>) = data.map{ Replacement(it.split(" => ")[0].toList(),it.split(" => ")[1] ) }

fun newMolecules(molecule:String, replacements:List<Replacement>):Set<String> {
    val newMolecules = mutableSetOf<String>()
    replacements.forEach { replacement ->
        (0..molecule.lastIndex).forEach { index ->
            if (molecule.replacementFound(replacement.old, index)) newMolecules.add(molecule.newMolecule(index, replacement.old.size, replacement.new))
        }
    }
    return newMolecules
}

fun String.replacementFound(replacementChars:List<Char>, position:Int):Boolean {
    replacementChars.forEachIndexed { index, char ->
        if (position + index > lastIndex || this[position + index] != char ) return false
    }
    return true
}

fun String.newMolecule(position:Int, size:Int, newText:String):String {
    val part1 = substring(0, position)
    val part2 = substring(position + size, length)
    return "$part1$newText$part2"
}

fun partOne(data:List<String>, molecule:String):Int = newMolecules(molecule,parse(data)).size

data class BestResult(var fewestSteps:Int = Int.MAX_VALUE)

fun partTwo(data:List<String>, molecule:String):Int {
    val replacements = parse(data).map{it.reverse()}
    return findMolecules("e",molecule, replacements,0)
}

fun findMolecules(target:String, string:String, replacements:List<Replacement>, steps:Int, bestResult: BestResult = BestResult()):Int = when {
    (bestResult.fewestSteps < Int.MAX_VALUE || steps > bestResult.fewestSteps) -> bestResult.fewestSteps //for some reason the first result found is the fewest number of steps?!
    (string == target) -> {
        bestResult.fewestSteps = steps
        steps
    }
    else ->
        newMolecules(string, replacements).map { s -> findMolecules(target, s, replacements,steps + 1, bestResult) }.minOfOrNull { it } ?: bestResult.fewestSteps
    }


