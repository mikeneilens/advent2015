
val digits = '0'..'9'

sealed class JElement{
    data class JObject(val members:Map<String,JElement>):JElement()
    data class JArray(val list:List<JElement>):JElement()
    data class StringLiteral(val value:String):JElement()
    data class NumberLiteral(val value:Int):JElement()

    fun sum(exclude:String = ""):Int = when(this) {
        is StringLiteral -> 0
        is NumberLiteral -> this.value
        is JArray -> list.sumOf { it.sum(exclude) }
        is JObject -> {
            if (members.values.contains(StringLiteral(exclude))) 0
            else members.values.sumOf { it.sum(exclude) }
            }
        }
    }


fun String.toText(opening:Char, closing:Char):String{
    var openings = 1
    var result = ""
    drop(1).forEach{char ->
        if (char == closing && --openings == 0) return opening + result + closing
        else if (char == opening) openings++
        result += char
    }
    return result
}
fun String.toNumberText():String{
    var result = ""
    forEach{char ->
        if (char !in digits && char != '-' ) return result
        result += char
    }
    return result
}

fun String.elementText() = when {
    isNumberLiteral() -> toNumberText()
    isStringLiteral() -> toText('"','"')
    isArray() -> toText('[',']')
    isObject() -> toText('{','}')
    else -> ""
}
fun String.isNumberLiteral() = startsWith('-') || first() in digits
fun String.isStringLiteral() = startsWith('"')
fun String.isArray() = startsWith('[')
fun String.isObject() = startsWith('{')

fun String.evaluate():JElement = when {
    isNumberLiteral() -> JElement.NumberLiteral(elementText().toInt())
    isStringLiteral() -> JElement.StringLiteral(elementText().drop(1).dropLast(1))
    isArray() -> evaluateArray()
    isObject() -> evaluateObject()
    else -> JElement.NumberLiteral(0) }

fun String.evaluateArray():JElement.JArray {
    var arrayText = drop(1).dropLast(1)
    val list = mutableListOf<JElement>()
    while (arrayText.isNotEmpty()) {
        val arrayItemText = arrayText.elementText()
        list.add(arrayItemText.evaluate())
        arrayText = arrayText.drop(arrayItemText.length)
        if (arrayText.isNotEmpty() && arrayText.first() == ',') arrayText = arrayText.drop(1)
    }
    return JElement.JArray(list)
}

fun String.evaluateObject():JElement.JObject {
    var objectText = drop(1).dropLast(1)
    val members = mutableMapOf<String,JElement>()
    while (objectText.isNotEmpty()) {
        val key = objectText.elementText().drop(1).dropLast(1)
        objectText = objectText.drop(key.length + 2)
        if (objectText.isNotEmpty() && objectText.first() == ':') objectText = objectText.drop(1)
        val valueText = objectText.elementText()
        members[key] = valueText.evaluate()
        objectText = objectText.drop(valueText.length)
        if (objectText.isNotEmpty() && objectText.first()== ',') objectText = objectText.drop(1)
    }
    return JElement.JObject(members)
}
