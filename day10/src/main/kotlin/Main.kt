fun lookAndSay(chars: List<Char>): List<Char> {
   var count = 0
   var lastChar = chars[0]
   val result = mutableListOf<Char>()
   chars.forEach { char ->
      if (char == lastChar) count++
      else {
         result.apply { add(count.toString()[0]); result.add(lastChar) }
         count = 1
         lastChar = char
      }
   }
   result.apply { add(count.toString()[0]); result.add(lastChar) }
   return result
}

tailrec fun partOne(string:List<Char>, noOfTimesToProcess:Int = 1, noOfTimes:Int = 0 ):Int =
   if (noOfTimes >= noOfTimesToProcess) string.size
   else partOne(lookAndSay(string), noOfTimesToProcess, noOfTimes + 1)

