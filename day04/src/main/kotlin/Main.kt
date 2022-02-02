import org.apache.commons.codec.digest.DigestUtils

fun lowestMD5HashWithPrefix(secretKey: String,requiredPrefix:String): Long {
    var number = 0L
    while (!hashedExpression(secretKey, number).startsWith(requiredPrefix)) {
        number++
    }
    return number
}

fun hashedExpression(secretKey:String, number:Long): String = DigestUtils.md5Hex("$secretKey${number}")

fun partOne(secretKey:String) = lowestMD5HashWithPrefix(secretKey, "00000")

fun partTwo(secretKey:String) = lowestMD5HashWithPrefix(secretKey, "000000")