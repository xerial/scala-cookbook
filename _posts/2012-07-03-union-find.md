---
layout: post
title: "Union Find"
description: "データを互いに疎なグループ(disjoint sets)に分ける"
category: recipes
tags: [algorithms]
---
{% include JB/setup %}

## 問題

遺伝子のデータにはゲノム座標中で交差しているアノテーションが含まれている（splicing variant, 転写開始位置の違いなどによる）。
遺伝子情報に基づく解析を行う際、重複を避けるためこのような遺伝子はひとまとまりにして考えたい。

	|----(g1)-------|                 |---(g4)----|     |----(g6)-----|
	  |-----(g2)----------|                         |---(g5)---|
	                   |--(g3)----|

上記の遺伝子g1, g2, ... , g6が与えられたとき、交差、あるいは包含関係にある遺伝子は同じ集合に入るようにする。

	{g1, g2, g3}, {g4}, {g5, g6}

g1, g3は直接交差はしていないが、各々g2と交差しているので同じ集合に属している。

## 考え方

区間の交差判定については、[こちら]({{BASE_PATH}}/recipes/2012/07/03/interval-sweep/)を参考に。
区間を並べ替え、左端からsweepしながら交差しているものを列挙すれば良い。

### グループの作成

n個の要素をグループに分類する問題として考える。

* find(e): 要素eがどのグループに属するかを見つける（グループの代表元を返す）
* union(e1, e1): 要素e1と要素e2が含まれるグループを結合する


## Union-Find 

互いに疎な集合を手軽に構築するデータ構造として、[Union-Find](http://en.wikipedia.org/wiki/Disjoint-set_data_structure)が使える。

### 参考文献

* Introduction to Algorithms 2nd Edition. Chapter21: Data Structures for Disjoint Sets

### コード例
[UnionFindSet.scala](https://github.com/xerial/silk/blob/develop/src/main/scala/xerial/silk/util/UnionFindSet.scala)より抜粋

ノードを順次`+=`で追加できるように設計。

    class UnionFindSet[E] { 
    
      /**
       * Holder of the element with its rank and the parent node
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

`find`でグループの代表元(root)を求める。同じ集合に属するノードは、ツリーで管理されているが、ルートまでのパス中のノードを同時にルートに直結させている(path compression).

      /**
       * Find the representative (root) element of the class to which e belongs
       */
      def find(e: E) : E = {
        val c = containerOf(e)
        if(c.isRoot)
          e
        else {
          // path compression: recursively connect all elements 
		  // in the path from e to the root directly to the root
          c.parent = find(c.parent)
          c.parent
        }
      }

`union(x, y)`ではx, yの代表元を求めてそれを結合することで、2つの集合の結合を行う。

      /**
       * Union the two sets containing x and y
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
    

### Union-Findをさらに使いやすくする

Setを拡張し、iterator、要素数などを取得できるように。

	class UnionFindSet[E] extends collection.mutable.Set[E] {
	   (中略)
       private def containerList = elemToContainerIndex.values
	   override def size = elemToContainerIndex.size
	   def contains(e: E) = elemToContainerIndex.contains(e)
	   /**
	    * Iterator of the elements contained in this set
	    * @return
		*/
	   def iterator = containerList.map(_.elem).toIterator

さらに、代表元のみを探索、あるノードと同じグループに属するノード集合、グループを探索するためのiteratorなどを定義。
		
    /**
     * Iterator of the root nodes of the groups
     */
    def representatives: Iterable[E] =
      for(c <- containerList if c.isRoot) yield c.elem
    /**
     * Return the elements belonging to the same group with e
     */
    def elementsInTheSameClass(e: E) : Iterable[E] = {
      val root = containerOf(find(e))
      for(c <- containerList if find(c.elem) == root.elem) yield c.elem
    }
    /**
     * Iterator of each group
     */
    def groups: Iterable[Iterable[E]] =
      for (r <- representatives) yield elementsInTheSameClass(r)
