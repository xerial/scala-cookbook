package xerial.scb

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpec, FlatSpec}

//--------------------------------------
//
// Lesson1Test.scala
// Since: 2012/06/13 15:18
// 
//--------------------------------------

/**
 * @author leo
 */
class Lesson1Test extends WordSpec with ShouldMatchers {

  "Lesson1" should {
    "create arrays" in {
      val a = Array(3, 2, 4, 5)

      // accessor
      a(0) should be (3)
      a(1) should be (2)

      // update elements.  a.update(2, 10)
      a(2) should be (4)
      a(2) = 10
      a(2) should be (10)


    }

  }


}