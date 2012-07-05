---
layout: post
title: "長い文字列を作成する"
description: "StringBuilder"
category: recipes
tags: [string]
---
{% include JB/setup %}

長い文字列を作成する。

## 解法

[StringBuilder](http://www.scala-lang.org/api/current/index.html#scala.collection.mutable.StringBuilder)を使用する。

<span class="label success">Good</span>

	val s = { 
		val b = new StringBuilder
		b.append("Hello")
		b.append(" ")
		b.append("World!")
		...
		b.result
	} // s = "Hello World!"
	

<span class="label important">Bad</span>

	var s = "Hello"
	s += " "            // "Hello " を生成
	s += "World!"       // "Hello World!"を生成
	s +=  ...           // さらに文字列のコピーを作成

+=で文字列を連結していくと、文字列のコピーが大量に発生してしまい性能が悪い。数個の文字列を連結するくらいなら`+=`で問題ないが、何十以上の文字列を連結するのには不向き。
