---
layout: post
title: "複数行に渡る文字列を作成する"
description: ""
category: recipes
tags: [string]
---
{% include JB/setup %}

改行を含む文字列をソースコード中に埋め込むには、triple quote(`"""`)を使う。


	val s = """Hello World! 
	Hello Scala!"""

コード中で読みやすいようにインデントを揃えたい場合には、stripMarginを使うと良い。

	val s2 = """|HelloWorld!
	            |Hello Scala!""".stripMargin

上記のsとs2は同じ内容の文字列になる。
