---
layout: post
title: "Scalaを学ぶ"
description: "Scalaを学ぶのにおすすめの資料"
category: recipes
tags: [references]
---
{% include JB/setup %}

## Scala 

* [Programming in Scala (2nd Ed)](http://www.artima.com/shop/programming_in_scala)
Scalaの開発者(Martin Odersky)らによる参考書。おそらくこれが一番良い教科書。CSの素養があればScalaのデザインについてより理解が深まる本。

* [Scala Documentation](http://docs.scala-lang.org/)
  * [Scala Collections](http://docs.scala-lang.org/overviews/collections/introduction.html)
  * [The Architecture of Scala Collections](http://docs.scala-lang.org/overviews/core/architecture-of-scala-collections.html)
  * [Parallel collections](http://docs.scala-lang.org/overviews/parallel-collections/overview.html)
* [Scala API](http://www.scala-lang.org/api/current/index.html)
* [Scala School by Twitter inc.](http://twitter.github.com/scala_school/)

## Computer Sciences

* [Purely Functional Data Structures](http://www.cs.cmu.edu/~rwh/theses/okasaki.pdf) by Chris Okasaki
  * [Amazon.com](http://www.amazon.co.jp/Purely-Functional-Structures-Chris-Okasaki/dp/0521663504), [Google Books](http://books.google.co.jp/books?id=SxPzSTcTalAC&dq=%22Purely+Functional+Data+Structures%22&printsec=frontcover&source=bn&hl=ja&ei=xCc2S_2uDcGHkQX--_mDCQ&sa=X&oi=book_result&ct=result&resnum=4&ved=0CCQQ6AEwAw#v=onepage&q&f=false)
  * 関数型言語でimmutableで性能の良いデータ構造をどうデザインするか
  * Listなど、同等の実装がScalaでも使われている
  * Amortized complexity (償却計算量) の考え方が基本

* [Introuction to Algorithms. Third Edition for Kindle](http://www.amazon.com/Introduction-Algorithms-Third-Edition-ebook/dp/B007CNRCAO/ref=tmm_kin_title_0)
  * アルゴリズムの代表的教科書。研究室でも読書会を行っている
  * この本を一通り読めば、十分な基礎力が付く
  * あとは現場の問題（生物学など）に応用あるのみ

### Other resources
* [SBT (simple build tool)](https://github.com/harrah/xsbt/wiki)　- Scalaのコードをビルドするのに（今のところ）一番便利。
* [ScalaTest](http://www.scalatest.org/): Tools for unit testing and tests by specifications. - Scalaでテストコードを書く。unit testingから、behaviour driven development (BDD), tests by specificationsなど、最近流行している形態のテストコードをサポートしている。
* How to use Git: [ProGit](http://progit.org/)
* [Git Cheat Sheet](http://help.github.com/git-cheat-sheets/)


