---
layout: post
title: "長い文字列を作成する"
description: "StringBuilder"
category: recipes
tags: [string]
---
{% include JB/setup %}

長い文字列を作成する。

## 方法

短い文字列の場合。
	
	val s = "Hello" + " World!!"

文字列をたくさん連結していく場合は、[StringBuilder](http://www.scala-lang.org/api/current/index.html#scala.collection.mutable.StringBuilder)を使用する。

<span class="label success">Good</span>

	val b = new StringBuilder
	for(i <- 0 until 10) {
		if(i > 0)
			b.append(", ")
		b.append(i)
	} 
	val s = b.result  // s = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9"

<span class="label important">Bad</span>

	var s = ""
	for(i <- 0 until 10) {
		if(i > 0)
		   s += ", "   // 文字列のコピーを作成
		s += i　// 文字列のコピーを作成
	} // s = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9"


+=でStringに対して文字列を連結していくと、文字列のコピーが大量に発生してしまい性能が悪くなる。数個の文字列を連結するくらいなら`+=`でも問題ないが、何十以上の文字列を連結するのには不向き。
