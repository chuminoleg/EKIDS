package Kotlin.Gleb

object WhileDoWhile {
    @JvmStatic
    fun main(args: Array<String>) {
        var i = 1
        do {
            print("$i, ")
            i += 5
        } while (i <= 100)
    }
}