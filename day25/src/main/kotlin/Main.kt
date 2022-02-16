val SEED = 20151125L
val MULTIPLIER = 252533
val DIVIDER = 33554393

data class Code(val col:Int, val row:Int, val num:Long) {
    fun next() =
        if (row == 1)
            Code( 1, col + 1, nextNumber() )
        else
            Code( col + 1, row - 1, nextNumber() )

    fun nextNumber() = num * MULTIPLIER % DIVIDER
}

fun parOne(targetCol:Int, targetRow:Int):Code =
    generateSequence(Code(1,1,SEED), Code::next)
        .first { code -> code.row == targetRow && code.col == targetCol }
