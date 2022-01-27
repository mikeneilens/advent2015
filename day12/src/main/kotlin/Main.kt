
val digits = '0'..'9'

sealed class JElement{
    data class JObject(val members:Map<String,JElement>):JElement()
    data class JArray(val list:List<JElement>):JElement()
    data class StringLiteral(val value:String):JElement()
    data class NumberLiteral(val value:Int):JElement()

    fun sum(exclude:StringLiteral = StringLiteral("") ):Int = when(this) {
        is StringLiteral -> 0
        is NumberLiteral -> value
        is JArray -> list.sumOf { it.sum(exclude) }
        is JObject -> {
            if (exclude in members.values) 0 else members.values.sumOf { it.sum(exclude) }
            }
        }
    }

fun String.toText(opening:Char, closing:Char):String{
    var openings = 1
    var result = ""
    drop(1).forEach{char ->
        if (char == closing && --openings == 0) return opening + result + closing
        if (char == opening) openings++
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
    var arrayText = removeParentheses()
    val listOfElements = mutableListOf<JElement>()
    while (arrayText.isNotEmpty()) {
        arrayText = evaluateEachArrayElement(arrayText, listOfElements)
    }
    return JElement.JArray(listOfElements)
}

private fun evaluateEachArrayElement(arrayText: String, list: MutableList<JElement>): String {
    val arrayItemText = arrayText.elementText()
    list.add(arrayItemText.evaluate())
    return if (arrayText == arrayItemText) "" else arrayText.drop(arrayItemText.length +1)
}

fun String.evaluateObject():JElement.JObject {
    var objectText = removeParentheses()
    val members = mutableMapOf<String,JElement>()
    while (objectText.isNotEmpty()) {
        objectText = evaluateObjectMember(objectText, members)
    }
    return JElement.JObject(members)
}

private fun evaluateObjectMember(objectText: String, members: MutableMap<String, JElement>): String {
    val key = objectText.elementText()
    val remainingText = objectText.drop(key.length + 1)
    val valueText = remainingText.elementText()
    members[key.removeParentheses()] = valueText.evaluate()
    return if (remainingText == valueText) "" else remainingText.drop(valueText.length + 1)
}

fun String.removeParentheses() = drop(1).dropLast(1)
