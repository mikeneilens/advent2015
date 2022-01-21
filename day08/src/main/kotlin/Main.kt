val escapeSequencesLength = mapOf('\\' to 1, '"' to 1, 'x' to 3)

fun calcMemoryUsed(string:String):Int{
    var i = 1
    var memoryUsed = 0
    while (i < string.length - 1) {
        if ( string[i] == '\\' && string[i + 1] in escapeSequencesLength.keys ) {
            i += escapeSequencesLength.getValue(string[i + 1])
        }
        memoryUsed++
        i++
    }
    return memoryUsed
}

fun partOne(data:List<String>):Int = data.sumOf{it.length} - data.sumOf(::calcMemoryUsed)

val escapeSequencesEncodedLength = mapOf('\\' to 3, '"' to 3, 'x' to 4)

fun calcEncodedLength(string:String):Int{
    var i = 1
    var encodedLength = 6 //length of empty string
    while (i < string.length - 1) {
        if ( string[i] == '\\' && string[i + 1] in escapeSequencesLength.keys ) {
            encodedLength += escapeSequencesEncodedLength.getValue(string[i + 1])
            i += escapeSequencesLength.getValue(string[i + 1])
        }
        encodedLength++
        i++
    }
    return encodedLength
}

fun partTwo(data:List<String>):Int = data.sumOf(::calcEncodedLength) - data.sumOf{it.length}
