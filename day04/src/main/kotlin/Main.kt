import org.apache.commons.codec.digest.DigestUtils

fun lowestMD5HashWithPrefix(secretKey: String,requiredPrefix:String): Long {
    var number = 0L
    while (!hashedExpression(secretKey, number).startsWith(requiredPrefix)) {
        number++
    }
    return number
}
// Day04 seems to be about knowing how to hash something using MD5.
// Is it cheating to use a library? Otherwise this is a hard task!
fun hashedExpression(secretKey:String, number:Long): String = DigestUtils.md5Hex("$secretKey$number")

fun partOne(secretKey:String) = lowestMD5HashWithPrefix(secretKey, "00000")

fun partTwo(secretKey:String) = lowestMD5HashWithPrefix(secretKey, "000000")