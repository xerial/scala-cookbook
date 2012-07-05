---
layout: post
title: "コンストラクタを複数定義する"
description: "代替コンストラクタとfactory"
category: recipes
tags: [class]
---
{% include JB/setup %}

クラスには、デフォルトのもの以外に、複数のコンストラクタを定義できる。

	class Interval(val start:Int, val end:Int) {
		// 代替コンストラクタの定義
		def this(point:Int) = this(point, point)
	}

代替コンストラクタ(alternative consturctor)内では、必ずデフォルトコンストラクタを呼び出さなくてはならない。この制約はクラスの初期化の間違いなどのバグを減らすのに効く。

**使用例**

	val v = new Interval(1, 3)   // デフォルトコンストラクタ
	val p = new Interval(1)      // 代替コンストラクタ new Interval(1, 1)が生成される
	

## コンストラクタの代わりにfactory methodを使う

コンストラクタにはクラス名以外の名前が付いていないので、コードの意味を十分に語れない。そこで、object内に新しいクラスを生成するfactory methodを提供すると良い。

	object Interval {
		// Interval(start, end)で新しいインスタンスを生成できるようにする
		def apply(start:Int, end:Int) = new Interval(start, end)
	
		// Interval.point(start, end)で、点を表す区間を生成するfactory method
		def point(start:Int) = new Interval(point, point)
	}

**使用例**

	val v = Interval(1, 3)
	val p = Interval.point(1)

factoryを作ることで、どのようなインスタンスを生成しているのかというコードの意図が伝えやすくなる。
