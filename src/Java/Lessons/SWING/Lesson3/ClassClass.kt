package Java.Lessons.SWING.Lesson3

class ClassClass : Movable, Rotate {
    override fun move() {
        println("Машина может ехать")
    }

    override fun rotate() {
        TODO("Not yet implemented")
    }

    val object1 = Object() // public final PI = 3,14
    var object2 = Object()

    fun main()  {
//        object1 = object2
        object2 = object1
    }


}

