enum class Command(val text:String) {TurnOn("turn on "), TurnOff("turn off "), Toggle("toggle ")}
data class Instruction(val command:Command, val xRange:LongRange, val yRange:LongRange)
data class Position(val x:Long, val y:Long)

typealias Rule = (MutableMap<Position, Int>,Position)-> Unit

fun processInstructions(data:List<String>, rules:Map<Command, Rule>):Int{
    val map = mutableMapOf<Position, Int>()
    data.forEach { instructionString ->
        val instruction = instructionString.toInstruction()
        map.applyRule(instruction, rules)
    }
    return map.values.sum()
}

fun String.toInstruction():Instruction {
    val command = if (startsWith(Command.TurnOn.text)) Command.TurnOn else if (startsWith(Command.TurnOff.text)) Command.TurnOff else Command.Toggle
    val (x0,y0) = removePrefix(command.text).split(" through ")[0].toPairOfNumbers()
    val (x1,y1) = removePrefix(command.text).split(" through ")[1].toPairOfNumbers()
    return Instruction(command, x0..x1, y0..y1)
}

fun String.toPairOfNumbers() = Pair(split(",")[0].toLong(), split(",")[1].toLong())

val partOneRules = mapOf(
    Command.TurnOn to {map:MutableMap<Position, Int>, position:Position -> map[position] = 1  },
    Command.TurnOff to {map:MutableMap<Position, Int>, position:Position -> map[position] = 0  },
    Command.Toggle to {map:MutableMap<Position, Int>, position:Position -> if (position in map && map.getValue(position) > 0) map[position] = 0 else map[position] = 1 }
)

fun MutableMap<Position, Int>.applyRule(instruction:Instruction, rules:Map<Command, Rule>) {
    (instruction.xRange).forEach{ x ->
        (instruction.yRange).forEach{ y ->
            rules.getValue(instruction.command)(this, Position(x,y))
        }
    }
}

fun partOne(data:List<String>) = processInstructions(data, partOneRules)

val partTwoRules = mapOf(
    Command.TurnOn to {map:MutableMap<Position, Int>, position:Position -> map[position] = (map[position] ?: 0) + 1  },
    Command.TurnOff to {map:MutableMap<Position, Int>, position:Position ->  if (position in map && map.getValue(position) > 0) map[position] = (map[position] ?: 0) - 1  },
    Command.Toggle to {map:MutableMap<Position, Int>, position:Position -> map[position] = (map[position] ?: 0) + 2 }
)

fun partTwo(data:List<String>) = processInstructions(data, partTwoRules)

