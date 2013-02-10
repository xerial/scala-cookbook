---
layout: post
title: "for-comprehensionの展開"
description: "map, flatMapのsyntax sugar"
category: recipes
tags: [collections]
---
{% include JB/setup %}
## for-comprehensionの定義

Scalaのfor文(for-comprehension: for文による網羅) は、C言語のようにループを回しているわけではなく、map, flatMapなどの関数を呼び出すsyntax sugarとなっています。以下にScalaのfor文の置き換えの定義を示します。

### for内のパラメータが１つの場合

    for { p0 <- e0 } yield e 　

は、mapを使って以下に置き換えられます。

    e0 map { p0 => e }


### 複数パラメータがある場合

    for { 
     p0 <- e0
     p1 <- e1
     ... 
     pn <- en } yield e

一番外側のパラメータがflatMapに置き換えられます。これが再帰的に繰り返され、パラメータが残り１つになると、最初のルールを使ってmapが適用されます。

     e0.flatMap { p0 => 
       for { 
	     p1 <- e1
		 p2 <- e2
		 ...
		 pn <- en 
	   } yield e
	 }

例えば、
	
	for(p0 <- e0; p1 <- e1; p2 <- e2) yield (p0, p1, p2)

は、

	e0.flatMap(p0 => e1.flatMap(p1 => e2.map(p2 => (p0, p1, p2))))

と同じになります。
