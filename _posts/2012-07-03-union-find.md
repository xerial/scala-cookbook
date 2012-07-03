---
layout: post
title: "Union Find"
description: "データを互いに疎なグループ(disjoint sets)に分ける"
category: recipes
tags: [algorithm]
---
{% include JB/setup %}

## 問題

遺伝子のデータにはゲノム座標中で交差しているアノテーションが含まれている（splicing variant, 転写開始位置の違いなどによる）。
遺伝子情報に基づく解析を行う際、重複を避けるため、このような遺伝子はひとまとまりにして考えたい。

	|----(g1)-------|                 |---(g4)----|     |----(g6)-----|
	  |-----(g2)----------|                         |---(g5)---|
	                   |--(g3)----|

上記の遺伝子g1, g2, ... , g6が与えられたとき、交差、あるいは包含関係にある遺伝子は同じ集合に入るようにする。

	{g1, g2, g3}, {g4}, {g5, g6}

g1, g3は直接交差はしていないが、各々g2と交差しているので同じ集合に属している。

## 考え方

### 区間の交差判定

区間を表現するクラスを作成。

	class Interval(val start:Int, val end:Int)

	object Interval {
		// Interval(s, e) でinstanceを作成できるようにするhelper method
		def apply(start:Int, end:Int) = new Interval(start, end)
	}

区間に順序を定義する。[scala.math.Ordering](http://www.scala-lang.org/api/current/scala/math/Ordering.html) を使用。
	
	object IntervalOrdering extends Ordering[Interval] {
		def compare(x:Interval, y:Interval) : Int = {
			// startの小さい順に並べる
			val diff = x.start - y.start
			if(diff == 0)
				x.end - y.end  // startが同じならendの小さい順に並べる
			else
				diff
		}
	}
	
	


### グループの作成

n個の要素をグループに分類する問題として考える。

* find(e): 要素eがどのグループに属するかを見つける（グループの代表元を返す）
* union(e1, e1): 要素e1と要素e2が含まれるグループを結合する

## Union-Find 



[UnionFindSet.scala](https://github.com/xerial/silk/blob/develop/src/main/scala/xerial/silk/util/UnionFindSet.scala)

    /**
     * Union-find based disjoint set implementation.
     *
     * Reference: http://enwikipeida.org/wiki/Disjoint-set_data_structure
     *
     * @author leo
     *
     */
    class UnionFindSet[E] extends collection.mutable.Set[E] {
    
      /**
       * Holder of the element with its rank and the parent node
       * @param elem
       * @param parent
       * @param rank
       */
      private class Container(val elem: E, var parent: E, var rank: Int) {
        def isRoot : Boolean = elem == parent
      }
    
      /**
       * Hold a map from elements to their containers
       */
      private val elemToContainerIndex = collection.mutable.Map[E, Container]()
    
    
      /**
       * Retrieve the container of the element e
       * @param e
       * @return container of e
       */
      private def containerOf(e: E): Container = {
        def newContainer = new Container(e, e, 0) // Set the parent to this element
    
        // If no container for e is found, create a new one
        elemToContainerIndex.getOrElseUpdate(e, newContainer)
      }
    
      /**
       * Add a new element
       * @param e
       */
      def +=(e: E): this.type = {
        containerOf(e) // create a new containerOf for e if it does not exist
        this
      }
    
      def -=(e: E): this.type = {
        throw new UnsupportedOperationException("removal")
      }
    
      /**
       * Find the representative (root) element of the class to which e belongs
       * @param e
       * @return
       */
      def find(e: E) : E = {
        val c = containerOf(e)
        if(c.isRoot)
          e
        else {
          // path compression: the parent of e
          c.parent = find(c.parent)
          c.parent
        }
      }
    
      /**
       * Union the two sets containing x and y
       * @param x
       * @param y
       */
      def union(x: E, y: E) {
    
        val xRoot = containerOf(find(x))
        val yRoot = containerOf(find(y))
    
        // Compare the rank of two root nodes
        if (xRoot.rank > yRoot.rank) {
          // x has a higher rank
          yRoot.parent = xRoot.elem
        }
        else {
          // y has a higher rank
          xRoot.parent = yRoot.elem
          // If the ranks are the same, increase the rank of the other
          if (xRoot.rank == yRoot.rank)
            yRoot.rank += 1
        }
      }
    
      private def containerList = elemToContainerIndex.values
    
      override def size = elemToContainerIndex.size
    
      def contains(e: E) = elemToContainerIndex.contains(e)
    
      /**
       * Iterator of the elements contained in this set
       * @return
       */
      def iterator = containerList.map(_.elem).toIterator
    
      /**
       * Iterator of the root nodes of the groups
       * @return
       */
      def representatives: Iterable[E] =
        for(c <- containerList if c.isRoot) yield c.elem
    
    
      /**
       * Return the elements belonging to the same group with e
       * @param e
       * @return
       */
      def elementsInTheSameClass(e: E) : Iterable[E] = {
        val root = containerOf(find(e))
        for(c <- containerList if find(c.elem) == root.elem) yield c.elem
      }
    
      /**
       * Iterator of each group
       * @return
       */
      def groups: Iterable[Iterable[E]] =
        for (r <- representatives) yield elementsInTheSameClass(r)
    
    }


## 参考文献

* Introduction to Algorithms 2nd Edition. Chapter21: Data Structures for Disjoint Sets

