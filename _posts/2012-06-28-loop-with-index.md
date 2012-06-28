---
layout: post
title: "インデックスを付きのループ"
description: "zipWithIndex"
category: recipes
tags: [collection]
---
{% include JB/setup %}

配列へのアクセス、要素のrankなど、添字（インデックス）を使いたい場合、

C++/Javaなどでの書き方

	for(int i=0; i<10; ++i) {
		printf("%d-th element:%s\n", i, array[i])
	}

Scalaでも添字を使うことはできる。

	val l = Array("A", "B", "C")
	for(i <- 0 until l.length) 
		println("%d-th element:%s".format(i, l(i)))

ここで`zipWithIndex`を使うと、添字の指定や配列の長さの範囲の指定が省けて便利。

	for((elem, i) <- l.zipWithIndex) 
		println("%d-th element:%s".format(i, elem))

`zipWithIndex`の動作

	scala> val l = Array("A", "B", "C")
	l: Array[java.lang.String] = Array(A, B, C)

	scala> l.zipWithIndex
	res0: Array[(java.lang.String, Int)] = Array((A,0), (B,1), (C,2))
	

forループ内では`(elem, i)`と`(A, 0), ...`のパターンマッチが行われている。
	
