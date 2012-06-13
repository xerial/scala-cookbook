package xerial.scb

object ScalaCookbook {
    def main(args:Array[String]) {

      //def f(x:Float)  = x*x

      def g(x:Int) : Unit = { x * x }

//      val r1  = f(10)
//      val r2 : Unit = g(10)
//      println(r1)
//      println(r2)

      val array : Array[Int] = Array(0, 2, 3, 4, 5, 10)
      println(array.mkString(", "))
      for(each <- array) {
        println("element " + each)
      }

      array.foreach(x => println("foreach: " + x))
      array.foreach(println(_))

      val mixedArray : Array[Float] = Array(10f, 2.43f, 34.34f, 234f, 34f, 0.3f)

      val twice = mixedArray.map(x => x * x)
      println(twice.mkString(", "))

      val filtered = twice.filter(x => x > 100)
      println(filtered.mkString(", "))

      println(filtered.sum)


      def print[A](arr:Seq[A]) = println(arr.mkString(", "))

      val a = Array(1, 3, 4)
      print(a)
      a(0) = 10
      print(a)

      val l = List(1, 2, 3, 4, 5)
      print(l)

      for(each <- l) {
        println("each " + each)
      }

      print(l.map(x => x*2).filter(_ > 3).map(_ * 10))


      val m = Map(1 -> "leo", 2 -> "yuki", 3 -> "suzuki")
      println(m(2))
      println(m(1))

      println(m.values)

      val names = List("leo", "yuki", "suzuki", "hiraoka")
      val grade = List("teacher", "student", "B4", "B4")



      for((name, count) <- names.zipWithIndex) {
        println("id:%d, name:%s".format(count+1, name))
      }
      println(names.zipWithIndex)




      val tuple = ("leo", 1)
      println(tuple)


      val t2 = (10, 2.3f, 3.5, "leo")
      println(t2)

      println(t2._1)
      println(t2._3)


      case class Person(name:String, grade:String)
      val name_grade = names.zip(grade)
      val p = name_grade.map(t => Person(t._1, t._2))
      println(p)

      println(p.sortBy(each => (each.grade,  each.name)))

      println("start computation")
      val intList = (1 until 100000)
      val result = intList.par.map{x =>
        var v = x
        for(i <- 0 until 100000)
          v = v * v
        v
      }
      println(result)

    }
}
