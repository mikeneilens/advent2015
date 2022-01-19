import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `string contains at least three vowels`() {
        assertTrue("ugknbfddgicrmopn".containsThreeVowels())
        assertTrue("aaa".containsThreeVowels())
        assertFalse("dvszwmarrgswjxmb".containsThreeVowels())
    }
    @Test
    fun `string contains at repeating Char`() {
        assertTrue("ugknbfddgicrmopn".containsRepeatingChar())
        assertTrue("aaa".containsRepeatingChar())
        assertFalse("jchzalrnumimnmhp".containsRepeatingChar())
    }
    @Test
    fun `string does not contains banned strings`() {
        assertTrue("ugknbfddgicrmopn".doesNotContainBannedStrings())
        assertTrue("aaa".doesNotContainBannedStrings())
        assertFalse("haegwjzuvuyypxyu".doesNotContainBannedStrings())
    }
    @Test
    fun `nice string`() {
        assertTrue("ugknbfddgicrmopn".isNiceStringPartOne())
        assertTrue("aaa".isNiceStringPartOne())
        assertFalse("jchzalrnumimnmhp".isNiceStringPartOne())
        assertFalse("haegwjzuvuyypxyu".isNiceStringPartOne())
        assertFalse("dvszwmarrgswjxmb".isNiceStringPartOne())
    }
    @Test
    fun `part one`() {
        assertEquals(255, niceStrings(puzzleInput, String::isNiceStringPartOne))
    }

    @Test
    fun `contains pair that appears twice without overlapping`() {
        assertTrue("qjhvhtzxzqqjkmpb".containsPairThatAppearsTwiceWithoutOverlapping())
        assertTrue("xxyxx".containsPairThatAppearsTwiceWithoutOverlapping())
        assertFalse("aaa".containsPairThatAppearsTwiceWithoutOverlapping())
        assertFalse("ieodomkazucvgmuy".containsPairThatAppearsTwiceWithoutOverlapping())
    }
    @Test
    fun `contains a repeating letter with a letter between them`() {
        assertTrue("qjhvhtzxzqqjkmpb".containsRepeatingCharWithACharBetweenThem())
        assertTrue("xxyxx".containsRepeatingCharWithACharBetweenThem())
        assertTrue("aaa".containsRepeatingCharWithACharBetweenThem())
        assertFalse("uurcxstgmygtbstg".containsRepeatingCharWithACharBetweenThem())
    }

    @Test
    fun `part two`() {
        assertEquals(55, niceStrings(puzzleInput, String::isNiceStringPartTwo))
    }

}
