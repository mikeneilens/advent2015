fun lookAndSay(chars: List<Char>): List<Char> {
   var count = 0
   var lastChar = chars[0]
   val result = mutableListOf<Char>()
   chars.forEach { char ->
      if (char == lastChar) count++
      else {
         result.apply { add(count.toSingleDigitChar()); result.add(lastChar) }
         count = 1
         lastChar = char
      }
   }
   result.apply { add(count.toSingleDigitChar()); result.add(lastChar) }
   return result
}

tailrec fun partOne(chars:List<Char>, noOfTimesToProcess:Int = 1, noOfTimes:Int = 0 ):Int =
   if (noOfTimes >= noOfTimesToProcess) chars.size
   else partOne(lookAndSay(chars), noOfTimesToProcess, noOfTimes + 1)

fun Int.toSingleDigitChar() = toString()[0]