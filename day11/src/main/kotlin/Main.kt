val letters = ('a'..'z').filter{ letter -> letter !in listOf('i','l','o') }

fun Char.nextLetter() = if (this == 'z') 'a' else letters[letters.indexOf(this) + 1]

fun List<Char>.increment(tail:List<Char> = listOf()):List<Char> =
    if (isNotEmpty() && last() != 'z') dropLast(1) + last().nextLetter() + tail
    else {
        if ( isNotEmpty() ) dropLast(1).increment(listOf('a') + tail)
        else tail.map{'a'} // has overflowed so return a list of all 'a'
    }

fun List<Char>.containsAscendingLetters():Boolean {
    (0 until size - 2).forEach{ index ->
        val char = this[index]
        if (char != 'y' && char != 'z' && this[index + 1] == char.nextLetter() && this[index + 2] == char.nextLetter().nextLetter()) return true
    }
    return false
}

fun List<Char>.containsTwoDifferentPairs():Boolean {
    var index = 0
    var noOfpairs = 0
    while (index < size - 1) {
        if (this[index] == this[index + 1]) {
            if (++noOfpairs == 2) return true
            index++
        }
        index++
    }
    return false
}

fun String.nextPasswrd():String {
    var chars = toList().increment()
    while (!(chars.containsAscendingLetters() && chars.containsTwoDifferentPairs())) {
       chars = chars.increment()
    }
    return chars.joinToString("")
}

fun partOne(password:String):String = password.nextPasswrd()

fun partTwo(password:String):String = password.nextPasswrd().nextPasswrd()