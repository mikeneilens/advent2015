val digits = '0'..'9'

data class KeyValue(val key:String, val value:JElement)

sealed class JElement{
    data class StringLiteral(val value:String):JElement()
    data class NumberLiteral(val value:Int):JElement()
    data class JObject(val members:List<KeyValue>):JElement()
    data class JArray(val list:List<JElement>):JElement()

    fun sum(exclude:StringLiteral = StringLiteral("") ):Int = when(this) {
        is StringLiteral -> 0
        is NumberLiteral -> value
        is JArray -> list.sumOf { it.sum(exclude) }
        is JObject -> {
            members.map{}
            if (exclude in members.map{it.value}) 0 else members.sumOf { it.value.sum(exclude) }
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
    throw InvalidJason("Invalid json $this")
}

fun String.toNumberText():String{
    var result = ""
    forEach{char ->  if (char in digits || char == '-' ) result += char else return result }
    return result
}

class InvalidJason(text:String):Error(text)

fun String.elementText() = when {
    isNumberLiteral() -> toNumberText()
    isStringLiteral() -> toText('"','"')
    isArray() -> toText('[',']')
    isObject() -> toText('{','}')
    else -> throw InvalidJason("Invalid json $this")
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
    else -> throw InvalidJason("Invalid json $this") }

fun String.evaluateArray() = JElement.JArray(evaluateComposite(::evaluateEachArrayItem))
fun String.evaluateObject() = JElement.JObject(evaluateComposite(::evaluateObjectItem))

fun <T>String.evaluateComposite(itemEvaluator:(String, MutableList<T>)-> String):List<T> {
    var arrayText = removeParentheses()
    val listOfElements = mutableListOf<T>()
    while (arrayText.isNotEmpty()) {
        arrayText = itemEvaluator(arrayText, listOfElements)
    }
    return listOfElements
}

private fun evaluateEachArrayItem(arrayText: String, list: MutableList<JElement>): String {
    val arrayItemText = arrayText.elementText()
    list.add(arrayItemText.evaluate())
    return if (arrayText == arrayItemText) "" else arrayText.drop(arrayItemText.length +1)
}

private fun evaluateObjectItem(objectText: String, members: MutableList<KeyValue>): String {
    val key = objectText.elementText()
    val remainingText = objectText.drop(key.length + 1)
    val valueText = remainingText.elementText()
    members.add(KeyValue(key.removeParentheses(), valueText.evaluate()))
    return if (remainingText == valueText) "" else remainingText.drop(valueText.length + 1)
}

fun String.removeParentheses() = drop(1).dropLast(1)
