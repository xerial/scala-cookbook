---
layout: post
title: "交差している区間の列挙"
description: "ソート済みの区間をsweep"
category: recipes
tags: [algorithms]
---
{% include JB/setup %}

## 問題

以下のリード（ゲノム配列の断片）で交差しているものを列挙せよ(pileup)。

	|----(r1)-------|                 |---(r4)----|     |----(r6)-----|
	  |-----(r2)----------|                         |---(r5)---|
	                   |--(r3)----|

上記のリードセットr1, r2, ... , r6が与えられたとき、交差しているリードは以下の３組。

	{r1, r2}, {r2, r3}, {r5, r6}

この問題は、リードのcoverage計算, SNPコールのための前処理にも使われておりゲノム情報処理では頻出。

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
	
区間を上の順序を使って並べ替える。

      // 1  2      5  6 7  8      10   12 13 14 15 16
      // |---------|              |----|  |-----|
      //    |-----------|         |----|      |-----|
      //              |---|
	  val in = List(Interval(1, 5), Interval(2, 7), 
		  Interval(6,8), Interval(10, 12), 
		  Interval(10, 12), Interval(13, 15), 
		  Interval(14, 16))
	  val sorted = in.sorted(IntervalOrdering)
  
	
その後、左端(startの小さい順)からsweepする。
sweepする際にはstartだけでなく、endの情報もpriority queue(優先度付きキュー)に入れて管理する。


## コード例

    import collection.{mutable, SortedSet}
    import annotation.tailrec

    class OverlapSweeper[A <: Interval](list:TraversableOnce[A]) extends Iterator[Seq[A]] {
    
      private val it = list.toIterator
      private var nextOverlappedSet : Option[Seq[A]] = None
      private var sweepLine = 0

	  // endの値の小さい順にqueueから取り出せるように順序を定義
      private val endValueQueue = new mutable.PriorityQueue[A]()(new Ordering[A] {
        def compare(x: A, y: A) = {
          val diff = y.end - x.end // lower end value has high priority
          if(diff == 0)
            y.start - x.start
          else
            diff
        }
      })
    
      def hasNext = {
        @tailrec
        def findNextOverlap : Option[Seq[A]] = {
          if(it.hasNext) {
            val r = it.next
            endValueQueue += r  // enqueue
            sweepLine = r.start
    
            // sweep intervals whose end value is less than sweepLine
            while(!endValueQueue.isEmpty && endValueQueue.head.end < sweepLine) {
              endValueQueue.dequeue
            }
            if(endValueQueue.size > 1)
               Some(endValueQueue.clone.toSeq) // queueの中身はmutableなので敢えてコピーを作成
            else
              findNextOverlap
          }
          else
            None
        }
        nextOverlappedSet = nextOverlappedSet.orElse(findNextOverlap)
        nextOverlappedSet.isDefined
      }
      def next() = {
        if(hasNext) {
          val e = nextOverlappedSet.get
          nextOverlappedSet = None
          e
        }
        else
          throw new NoSuchElementException("no more elements")
      }
    }

## 動作をテストする

    val in = List(Interval(1, 5), Interval(2, 7), Interval(6,8), 
	  Interval(10, 12), Interval(10, 12), Interval(13, 15), Interval(14, 16))
    val sorted = in.sorted(IntervalOrdering)
    val overlapped = new OverlapSweeper(sorted)
    for(s <- overlapped) {
      // overlapしていると報告された区間のすべての組み合わせをチェック(combination)
      for(c <- s.combinations(2)) {
         val a = c(0)
         val b = c(1)
         a.intersectWith(b) should be (true)
      }
    }


## 拡張

[GInterval](https://github.com/xerial/genome-weaver/blob/fba37256f6d372993989cc8e77bfab02a6700ae7/lens/src/main/scala/utgenome/weaver/lens/GenomeRange.scala#L258)
のように、染色体名、strandの情報が含まれる場合、上記のアルゴリズムのままでは上手くsweepできない。

	class GInterval(val chr:String, val start:Int, val end:Int, val strand:Strand) 

どう拡張すれば良いか？

### リードセットがメモリに収まりきる場合

染色体ごとにデータをグループ分けして、それぞれをsweep。簡単。

    val l : List[GInterval] = ...
    val groups = l.groupBy(_.chr) 
	// parllel collectionで染色体ごとに並列処理
	for((chr, lst) <- groups.par; overlappedReadSet <- lst) {
		...
	}
  
### リードセットがメモリに収まりきらない場合

染色体名を覚えておき、異なる染色体のリードが入力されたら、queueにたまっているものをすべてsweepする。





