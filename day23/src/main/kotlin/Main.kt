
data class ComputerStatus(val step:Int = 0, val a:Int = 0, val b:Int)

fun String.process(computerStatus: ComputerStatus):ComputerStatus {
    val instruction = toInstruction()
    val param = split(" ")[1].removeSuffix(",")
    val offset = if (split(" ").size > 2) split(" ")[2].toInt() else 0
    return instruction(computerStatus, param, offset  )
}

fun String.toInstruction() = instructions.getValue(split(" ").first())

val instructions = mapOf("hlf" to ::hlf, "tpl" to ::tpl, "inc" to ::inc, "jmp" to ::jmp, "jie" to ::jie, "jio" to ::jio)

fun hlf(computerStatus:ComputerStatus, register:String, offset:Int) =
    ComputerStatus(computerStatus.step + 1, if (register == "a") computerStatus.a /2 else computerStatus.a, if (register == "b") computerStatus.b /2 else computerStatus.b  )

fun tpl(computerStatus:ComputerStatus, register:String, offset:Int) =
    ComputerStatus(computerStatus.step + 1, if (register == "a") computerStatus.a *3 else computerStatus.a, if (register == "b") computerStatus.b * 3 else computerStatus.b  )

fun inc(computerStatus:ComputerStatus, register:String, offset:Int) =
    ComputerStatus(computerStatus.step + 1, if (register == "a") computerStatus.a + 1 else computerStatus.a, if (register == "b") computerStatus.b + 1 else computerStatus.b  )

fun jmp(computerStatus:ComputerStatus, register:String, offset:Int) =
    ComputerStatus(computerStatus.step + register.toInt(), computerStatus.a, computerStatus.b )

fun jie(computerStatus:ComputerStatus, register:String, offset:Int) =
    if (register == "a" && (computerStatus.a % 2) == 0 || register == "b" && (computerStatus.b % 2) == 0)
        ComputerStatus(computerStatus.step + offset, computerStatus.a, computerStatus.b )
    else computerStatus

fun jio(computerStatus:ComputerStatus, register:String, offset:Int) =
    if (register == "a" && computerStatus.a  == 1 || register == "b" && computerStatus.b  == 1)
        ComputerStatus(computerStatus.step + offset, computerStatus.a, computerStatus.b )
    else computerStatus