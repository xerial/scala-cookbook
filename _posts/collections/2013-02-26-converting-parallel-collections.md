---
layout: post
title: "Parallel/Sequentialコレクションへの変換"
description: ""
category: recipes
tags: [collections]
---
{% include JB/setup %}

マルチコア（スレッド）で並列処理可能なコレクションへの変換には`par`を使います。

    scala> val s = for(i <- 0 until 10) yield i
    s: scala.collection.immutable.IndexedSeq[Int] = Vector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    
    scala> val double = s.par.map(_*2)　// 並列処理される
    double: scala.collection.parallel.immutable.ParSeq[Int] = ParVector(0, 2, 4, 6, 8, 10, 12, 14, 16, 18)

	
一方、sortingなど2013年現在のScalaではsequentialコレクションにしか提供されてない操作を使いたい場合は、`seq`メソッドを使ってsequentialコレクションに戻す必要があります。

    scala> double.sorted
    <console>:10: error: value sorted is not a member of scala.collection.parallel.immutable.ParSeq[Int]
                  double.sorted
                         ^

`seq`を経由するとsortedが使えるようになります。以下は逆順に並べ替える例。

    scala> double.seq.sorted(Ordering[Int].reverse)
    res2: scala.collection.immutable.Seq[Int] = Vector(18, 16, 14, 12, 10, 8, 6, 4, 2, 0)

### 参考

* [Parallel collection conversions - Scala Documentation](http://docs.scala-lang.org/overviews/parallel-collections/conversions.html)
