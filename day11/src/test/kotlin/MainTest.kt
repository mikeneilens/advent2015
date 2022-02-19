import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `letter after a is b`(){
        assertEquals('b','a'.nextLetter())
    }
    @Test
    fun `letter after z is a`(){
        assertEquals('a','z'.nextLetter())
    }
    @Test
    fun `incrememnting xx 5 times`() {
        assertEquals("xy".toList(), "xx".toList().increment() )
        assertEquals("xz".toList(), "xx".toList().increment().increment() )
        assertEquals("ya".toList(), "xx".toList().increment().increment().increment() )
        assertEquals("yb".toList(), "xx".toList().increment().increment().increment().increment() )
    }
    @Test
    fun `incrememnting zz 2 times`() {
        assertEquals("aa".toList(),"zz".toList().increment())
        assertEquals("ab".toList(),"zz".toList().increment().increment())
    }
    @Test
    fun `string contains three ascending letters`() {
        assertTrue("abc".toList().containsAscendingLetters())
        assertTrue("aabc".toList().containsAscendingLetters())
        assertTrue("abca".toList().containsAscendingLetters())
        assertTrue("abcdffaa".toList().containsAscendingLetters())
        assertTrue("ghjaabcc".toList().containsAscendingLetters())
        assertFalse("ayza".toList().containsAscendingLetters())
    }
    @Test
    fun `string contains two different pairs`() {
        assertTrue("aabcc".toList().containsTwoDifferentPairs())
        assertTrue("aacc".toList().containsTwoDifferentPairs())
        assertTrue("abcdffaa".toList().containsTwoDifferentPairs())
        assertTrue("ghjaabcc".toList().containsTwoDifferentPairs())
        assertFalse("aaa".toList().containsTwoDifferentPairs())
    }
    @Test
    fun `next password with sample data`() {
        assertEquals("abcdffaa", "abcdefgh".nextPasswrd())
    }
    @Test
    fun `part one`() {
        assertEquals("hepxxyzz", partOne("hepxcrrq"))
    }
    @Test
    fun `part two`() {
        assertEquals("heqaabcc", partTwo("hepxcrrq"))
    }

}