package xerial.scb.tree

//--------------------------------------
//
// Tree.scala
// Since: 2012/07/11 10:28
//
//--------------------------------------


abstract class Node[+A] {
  def isEmpty: Boolean

  def getOrElse[B >: A](alternative: => Node[B]): Node[B]
}

case object Empty extends Node[Nothing] {
  def isEmpty = true

  def getOrElse[B >: Nothing](alternative: => Node[B]): Node[B] = alternative
}

case class Tree[+A](elem: A, left: Node[A], right: Node[A]) extends Node[A] {
  def isEmpty = false

  def getOrElse[B >: A](alternative: => Node[B]): Node[B] = this
}

object BinaryTree {
  def empty[A] = new BinaryTree[A](Empty)
}

class BinaryTree[+A](val root: Node[A]) {

  private class Finder(target:A, updater: Tree[A] => Node[A]) {
    // (newNode, found flag)
    def find(current: Node[A]): (Node[A], Boolean) = {
      current match {
        case Empty => (current, false)
        case t @ Tree(elem, left, right) =>
          if (elem == target)
            (updater(t), true)
          else {
            // search left tree
            val (l, found) = find(left)
            if (found)
              (Tree(elem, l, right), found)
            else {
              // search right tree
              val (r, found) = find(right)
              if(found)
                (Tree(elem, left, r), found)
              else
                (current, false) // no change
            }
          }
      }
    }
  }


  private def set(target:A, newChild:A, updater:Tree[A] => Node[A]) : this.type = {
    val f = new Finder(target, updater)
    val (newRoot, found) = f.find(root)
    if (found)
      new BinaryTree(newRoot)
    else
      this // no change
  }
  
  // set left node
  def setLeft(target: A, newChild: A): this.type =
    set(target, newChild, { case Tree(e, l, r) => Tree(e, Tree(newChild, Empty, Empty), r)})


  // set right node
  def setRight(target: A, newChild: A): this.type =
    set(target, newChild, { case Tree(e, l, r) => Tree(e, l, Tree(newChild, Empty, Empty))})

}