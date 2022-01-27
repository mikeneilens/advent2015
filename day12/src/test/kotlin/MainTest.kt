import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `sample 1 has a total of 6`() {
        assertEquals(6, """[1,2,3]""".evaluate().sum())
        assertEquals(6, """{"a":2,"b":4}""".evaluate().sum())
    }
    @Test
    fun `sample 2 has a total of 3`() {
        assertEquals(3, """[[[3]]]""".evaluate().sum())
        assertEquals(3, """{"a":{"b":4},"c":-1}""".evaluate().sum())
    }
    @Test
    fun `sample 3 has a total of 0`() {
        assertEquals(0, """{"a":[-1,1]}""".evaluate().sum())
        assertEquals(0, """[-1,{"a":1}]""".evaluate().sum())
    }
    @Test
    fun `sample 4 has a total of 0`() {
        assertEquals(0, """[]""".evaluate().sum())
        assertEquals(0, """{}""".evaluate().sum())
    }
    @Test
    fun `part one`(){
        assertEquals(119433, puzzleInput.evaluate().sum())
    }

    @Test
    fun `get text for an array`() {
        assertEquals("""[[[3]]]""", """[[[3]]],""".toText('[',']'))
    }
    @Test
    fun `get text for an object`() {
        assertEquals("""{"a":{"b":4},"c":-1}""", """{"a":{"b":4},"c":-1},""".toText('{','}'))
    }
    @Test
    fun `get text for an string`() {
        assertEquals(""""abc"""",""""abc",""".toText('"','"'))
    }
    @Test
    fun `get text for a number`() {
        assertEquals("-123","-123abc".toNumberText())
        assertEquals("123","123abc".toNumberText())
        assertEquals("","abc".toNumberText())
    }
    @Test
    fun `evaluate a json string containing a number`() {
        assertEquals(JElement.NumberLiteral(-123), "-123".evaluate())
        assertEquals(JElement.NumberLiteral(123), "123".evaluate())
    }
    @Test
    fun `evaluate a json string containing a string`() {
        assertEquals(JElement.StringLiteral("abc"), """"abc"""".evaluate())
    }
    @Test
    fun `evaluate a json string containing an array containing nothing`() {
        assertEquals(JElement.JArray(listOf<JElement>()), "[]".evaluate())
    }
    @Test
    fun `evaluate a json string containing an array containing one number`() {
        assertEquals(JElement.JArray(listOf(JElement.NumberLiteral(123))), "[123]".evaluate())
    }
    @Test
    fun `evaluate a json string containing an array containing a number and a string`() {
        assertEquals(JElement.JArray(listOf(JElement.NumberLiteral(123),JElement.StringLiteral("abc"))), """[123,"abc"]""".evaluate())
    }
    @Test
    fun `evaluate a json string containing an object containing nothing`() {
        assertEquals(JElement.JObject(mapOf()), "{}".evaluate())
    }
    @Test
    fun `evaluate a json string containing an object containing a number`() {
        assertEquals(JElement.JObject(mapOf("a" to JElement.NumberLiteral(123))),  """{"a":123}""".evaluate())
    }
    @Test
    fun `evaluate a json string containing an object containing a number and a string`() {
        assertEquals(JElement.JObject(mapOf("a" to JElement.NumberLiteral(123),"b" to JElement.StringLiteral("xyz"))),  """{"a":123,"b":"xyz"}""".evaluate())
    }
    @Test
    fun `evaluate a json string containing an object containing a number and an array`() {
        assertEquals(JElement.JObject(mapOf("a" to JElement.NumberLiteral(123),"b" to JElement.JArray(listOf(JElement.StringLiteral("xyz"))))),  """{"a":123,"b":["xyz"]}""".evaluate())
    }
    @Test
    fun `evaluate a json string containing an object containing a number and a object`() {
        assertEquals(JElement.JObject(mapOf("a" to JElement.NumberLiteral(123),"b" to JElement.JObject(mapOf("c" to JElement.StringLiteral("xyz"))))),  """{"a":123,"b":{"c":"xyz"}}""".evaluate())
    }
    @Test
    fun `part two`() {
        assertEquals(68466,puzzleInput.evaluate().sum(JElement.StringLiteral("red")) )
    }

}