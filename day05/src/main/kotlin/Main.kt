fun String.containsThreeVowels(): Boolean {
    var vowelsCount = 0
    forEach{ char ->
        if (char in "aeiou" && ++ vowelsCount == 3) return true
    }
    return false
}
fun String.containsRepeatingChar(): Boolean {
    forEachIndexed{index, char ->
        if (index > 0 && get(index - 1) == char ) return true
    }
    return false
}
fun String.doesNotContainBannedStrings(bannedStrings:List<String> = listOf("ab", "cd", "pq", "xy")):Boolean {
    forEachIndexed{index, char ->
        if (index > 0) {
            val pair = "${get(index - 1)}$char"
            if (pair in bannedStrings) return false
        }
    }
    return true
}

fun String.isNiceStringPartOne() = containsThreeVowels() && containsRepeatingChar() && doesNotContainBannedStrings()

fun niceStrings(data:List<String>, niceStringRule:String.()->Boolean) = data.filter(niceStringRule).size

fun String.containsPairThatAppearsTwiceWithoutOverlapping():Boolean {
    forEachIndexed{index, char ->
        if (index > 0) {
            val pair = "${get(index - 1)}$char"
            if (pair in drop(index + 1)) return true
        }
    }
    return false
}

fun String.containsRepeatingCharWithACharBetweenThem(): Boolean {
    forEachIndexed{index, char ->
        if (index > 1 && get(index - 2) == char ) return true
    }
    return false
}

fun String.isNiceStringPartTwo() = containsPairThatAppearsTwiceWithoutOverlapping() && containsRepeatingCharWithACharBetweenThem()




