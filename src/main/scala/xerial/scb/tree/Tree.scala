package xerial.scb.tree

//--------------------------------------
//
// Tree.scala
// Since: 2012/07/11 10:28
//
//--------------------------------------


abstract class Node[+A] {
  def isEmpty : Boolean
  def getOrElse[B >: A](alternative: => Node[B]) : Node[B]
}
case object Empty extends Node[Nothing] {
  def isEmpty = true
  def getOrElse[B >: Nothing](alternative: => Node[B]) : Node[B] = alternative
}

case class Fork[+A](elem:A, left:Node[A], right:Node[A]) extends Node[A] {
  def isEmpty = false
  def getOrElse[B >: A](alternative: => Node[B]) : Node[B] = this
}

object BinaryTree {
  def empty[A] = new BinaryTree[A](Empty)
}

class BinaryTree[+A](val root:Node[A]) {
  
  type Tree = this.type

  // set left node
  def setLeft(target:A, newChild:A) : Tree = {

    var found = false

    def find(current:Node[A]) : Node[A] = {
      current match {
        case Empty => Empty
        case Fork(elem, left, right) => 
          if(elem == target) {
            found = true
            Fork(elem, Fork(newChild, Empty, Empty), right)
          }
          else {
            val l = find(left)
            if(!l.isEmpty)
              Fork(elem, l, right)
            else
              Fork(elem, left, find(right))
          }
      }
    }
    val newRoot = find(root)
    if(found)
      new BinaryTree(newRoot)
    else
      this  // no change
  }

  // TODO: set right node


  
}