
abstract class Instruction(val output:String) {
    fun connect(wires:MutableMap<String, Int>) {
        if (wires[output] == null) processInstruction(wires)
    }
    abstract fun processInstruction(wires:MutableMap<String, Int>)
}

fun MutableMap<String, Int>.value(n:String) = if (n.toIntOrNull() == null)  getValue(n) else n.toInt()

class Assign(val input:String, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input)
    }
}

class And(val input1:String, val input2:String, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input1) and wires.value(input2)
    }
}

class Or(val input1:String, val input2:String, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input1) or wires.value(input2)
    }
}

class LShift(val input:String, val shift:Int, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input) shl shift
    }
}

class RShift(val input:String, val shift:Int, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input) shr shift
    }
}

class Not(val input:String, output:String):Instruction(output) {
    override fun processInstruction(wires: MutableMap<String, Int>) {
        wires[output] = wires.value(input).inv()
    }
}

fun parse(data:List<String>) = data.map(String::toInstruction)

fun String.toInstruction():Instruction {
    val leftSide = split(" -> ")[0]
    val targetWire = split(" -> ")[1]
    return when {
        (leftSide.split(" ").size == 1) ->  Assign(leftSide, targetWire)
        (leftSide.split(" ")[1] == "AND") -> And(leftSide.operand1, leftSide.operand2, targetWire)
        (leftSide.split(" ")[1] == "OR") -> Or(leftSide.operand1, leftSide.operand2, targetWire)
        (leftSide.split(" ")[1] == "LSHIFT") -> LShift(leftSide.operand1, leftSide.operand2.toInt(), targetWire)
        (leftSide.split(" ")[1] == "RSHIFT") -> RShift(leftSide.operand1, leftSide.operand2.toInt(), targetWire)
        (leftSide.split(" ")[0] == "NOT") -> Not(leftSide.singleOperand, targetWire)
        else -> Assign("0","")
    }
}

val String.operand1 get() = split(" ")[0]
val String.operand2 get() = split(" ")[2]
val String.singleOperand get() = split(" ")[1]

fun makeCircuit(instructions: List<Instruction>, wires:MutableMap<String, Int> = mutableMapOf()): Map<String, Int> {
    var complete = true
    instructions.forEach {
        try {
            it.connect(wires)
        } catch(e:NoSuchElementException) { //can't make a connection
            complete = false
        }
    }
    if (complete) return wires else return makeCircuit(instructions, wires)
}

fun partOne(data:List<String>):Map<String, Int> {
    val instructions = parse(data)
    return makeCircuit(instructions)
}

fun List<Instruction>.override(wire:String, value:Int) = map{
    if (it.output == "b") Assign("$value", "b") else it
}

fun partTwo(data:List<String>):Map<String, Int> {
    val instructions = parse(data).override("b", 956)
    return makeCircuit(instructions)
}