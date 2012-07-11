package xerial.scb.tree

//--------------------------------------
//
// Tree.scala
// Since: 2012/07/11 10:28
//
//--------------------------------------


abstract class Node[+A] {
  def isEmpty: Boolean

  def foreach[U](f:A => U)
  def getOrElse[B >: A](alternative: => Node[B]): Node[B]
  def updateLeft[A1 >: A](e:A1) : Node[A1]
  def updateRight[A1 >: A](e:A1) : Node[A1]
}

case object Empty extends Node[Nothing] {
  def isEmpty = true
  def foreach[U](f:Nothing => U) {}
  def getOrElse[B >: Nothing](alternative: => Node[B]): Node[B] = alternative
  def updateLeft[A1 >: Nothing](e:A1)= this
  def updateRight[A1 >: Nothing](e:A1) = this
}

case class Tree[+A](elem: A, left: Node[A], right: Node[A]) extends Node[A] {
  def isEmpty = false

  def foreach[U](f:A => U) { f(elem) }
  def getOrElse[B >: A](alternative: => Node[B]): Node[B] = this

  def updateLeft[A1 >: A](e:A1) = Tree(elem, Tree(e, Empty, Empty), right)
  def updateRight[A1 >: A](e:A1) = Tree(elem, left, Tree(e, Empty, Empty))
}

object BinaryTree {
  def empty[A] = new BinaryTree[A](Empty)
  def apply[A](root:A) = new BinaryTree[A](Tree(root, Empty, Empty))
}

class BinaryTree[A](val root: Node[A]) {

  override def toString = root.toString

  private class Finder(target:A, updater: Tree[A] => Node[A]) {
    // (newNode, found flag)
    def find(current: Node[A]): Option[Node[A]] = {
      current match {
        case Empty => None
        case t @ Tree(elem, left, right) =>
          if (elem == target)
            Some(updater(t))
          else {
            // search left tree
            find(left).map(Tree(elem, _, right)).orElse {
              // search right tree
              find(right).map(Tree(elem, left, _))
            }
          }
      }
    }
  }


  private def set(target:A, newChild:A, updater:Tree[A] => Tree[A]) : BinaryTree[A] = {
    val f = new Finder(target, updater)
    val newRoot = f.find(root)
    newRoot.map(r => new BinaryTree(r)).getOrElse(this)
  }
  
  // set left node
  def setLeft(target: A, newChild: A): BinaryTree[A] =
    set(target, newChild, { _.updateLeft(newChild) })


  // set right node
  def setRight(target: A, newChild: A): BinaryTree[A] =
    set(target, newChild, { _.updateRight(newChild) })
  
  
  def dfs[B](f: A => B) {
    def dfs(node:Node[A]) {
      node match {
        case Empty =>
        case Tree(e, l, r) => {
          f(e)
          dfs(l)
          dfs(r)
        }
      }
    }
    dfs(root)
  }

  def bfs[B](f: A=> B) {
    val queue = new collection.mutable.Queue[Node[A]]
    queue += root

    while(!queue.isEmpty) {
      val node = queue.dequeue
      node match {
        case Empty =>
        case Tree(e, l, r) => {
          f(e)
          queue += l
          queue += r
        }
      }
    }
  }

}