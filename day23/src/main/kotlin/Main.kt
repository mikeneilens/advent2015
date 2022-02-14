
val hlf = { computerStatus:ComputerStatus, param1:String, _:String ->
    ComputerStatus(computerStatus.step + 1,
        if (param1 == "a") computerStatus.a / 2 else computerStatus.a,
        if (param1 == "b") computerStatus.b / 2 else computerStatus.b)}

val  tpl = {computerStatus:ComputerStatus, param1:String, _:String  ->
    ComputerStatus(computerStatus.step + 1,
        if (param1 == "a") computerStatus.a *3 else computerStatus.a,
        if (param1 == "b") computerStatus.b * 3 else computerStatus.b  )}

val inc ={computerStatus:ComputerStatus, param1:String, _:String ->
    ComputerStatus(computerStatus.step + 1,
        if (param1 == "a") computerStatus.a + 1 else computerStatus.a,
        if (param1 == "b") computerStatus.b + 1 else computerStatus.b  )}

val jmp = {computerStatus:ComputerStatus, param1:String, _:String ->
    ComputerStatus(computerStatus.step + param1.toInt(),
        computerStatus.a,
        computerStatus.b )}

val jie = { computerStatus:ComputerStatus, param1:String, param2:String ->
    if (param1 == "a" && (computerStatus.a % 2) == 0 || param1 == "b" && (computerStatus.b % 2) == 0)
        ComputerStatus(computerStatus.step + param2.toInt(), computerStatus.a, computerStatus.b )
    else ComputerStatus(computerStatus.step + 1, computerStatus.a, computerStatus.b )}

val jio = { computerStatus:ComputerStatus, param1:String, param2:String ->
    if (param1 == "a" && computerStatus.a  == 1 || param1 == "b" && computerStatus.b  == 1)
        ComputerStatus(computerStatus.step + param2.toInt(), computerStatus.a, computerStatus.b )
    else ComputerStatus(computerStatus.step + 1, computerStatus.a, computerStatus.b )}

val instructions = mapOf("hlf" to hlf, "tpl" to tpl, "inc" to inc, "jmp" to jmp, "jie" to jie, "jio" to jio)

data class ComputerStatus(val step:Int = 0, val a:Int = 0, val b:Int = 0)

fun String.process(computerStatus: ComputerStatus):ComputerStatus {
    val instruction = instructions.getValue(split(" ").first())
    val param1 = split(" ")[1].removeSuffix(",")
    val param2 = if (split(" ").size > 2) split(" ")[2] else ""
    return instruction(computerStatus, param1, param2  )
}

class Program(private val data:List<String>) {

    fun run(initialStatus:ComputerStatus = ComputerStatus()):ComputerStatus  {
        var computerStatus = initialStatus
        while (computerStatus.step < data.size) {
            computerStatus = data[computerStatus.step].process(computerStatus)
        }
        return computerStatus
    }
}

fun partOne(data:List<String>):Int = Program(data).run().b

fun partTwo(data:List<String>):Int = Program(data).run(ComputerStatus(0,1,0)).b