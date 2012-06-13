package xerial.scb

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpec, FlatSpec}
import xerial.silk.util.Logger

//--------------------------------------
//
// Lesson1Test.scala
// Since: 2012/06/13 15:18
// 
//--------------------------------------

/**
 * @author leo
 */
class Lesson1Test extends WordSpec with ShouldMatchers with Logger {

  "Lesson1" should {
    "create arrays" in {
      val a = Array(3, 2, 4, 5)

      debug(a.mkString(", "))

      // accessor
      a(0) should be (3)   // a(0) becomes a.apply(0)
      a(1) should be (2)

      // update elements.
      a(2) should be (4)
      a(2) = 10    // translates to a.update(2, 10)
      debug(a.mkString(", "))
      a(2) should be (10)

    }

    "build arrays" in {
      val b = Array.newBuilder[Int]

      for(i <- 0 until 10)
        b += i * i

      val array = b.result

      debug(array.mkString(", "))
    }


  }


}