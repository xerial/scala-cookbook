package xerial.scb.tree

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec
import xerial.silk.util.Logger

//--------------------------------------
//
// BinaryTreeTest.scala
// Since: 2012/07/11 11:26
//
//--------------------------------------

/**
 * @author leo
 */
class BinaryTreeTest extends WordSpec with ShouldMatchers with Logger {

  "BinaryTree" should {
    "insert nodes" in {
      var b = BinaryTree[String]("A")
      b = b.setLeft("A", "B")
      b = b.setLeft("B", "C")
      b = b.setRight("A", "E")
      b = b.setRight("B", "D")
      info(b)
    }
  }

}