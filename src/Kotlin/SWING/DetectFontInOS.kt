package Kotlin.SWING

import java.awt.GraphicsEnvironment

fun main() {
    val fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (element in fonts) {
        println(element)
    }
}