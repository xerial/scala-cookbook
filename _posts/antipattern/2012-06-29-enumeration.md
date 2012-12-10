---
layout: post
title: "ScalaのEnumerationは使うな"
description: "Scalaで列挙型を定義するには"
category: recipes
tags: [AntiPattern]
---
{% include JB/setup %}

## ScalaのEnumerationは使いにくい

Scalaには列挙型として[Enumeration](http://www.scala-lang.org/api/current/index.html#scala.Enumeration)が用意されているが、以下の理由で使いにくい。

 * 値にメソッドを定義できない
 * DNAというEnumerationを定義しても、個々の値は、DNA.Value型として扱わなければならないため、コードが不自然になる。

### Enumerationを使ったコード例

	object DNA extends Enumeration {
		val A, C, G, T, N = Value
	}
	
	val base : DNA.Value = DNA.A
 
ここでDNA.Value型を拡張することが許されていないので、ラベルとしての機能しか持たせることができない。

## 解決策

ScalaではJavaのコードが使えるので、Javaの[enum](http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html)を使うのが簡便だが、Scalaのコードだけで同様の機能を実装するには、objectを使うと良い。

## コード例

DNAの塩基を表すコード。[genome-weaverのDNA.scala](https://github.com/xerial/genome-weaver/blob/develop/lens/src/main/scala/utgenome/weaver/lens/DNA.scala)より抜粋。

	object DNA {
      // objectで定義するとsingletonになる
	  case object A extends DNA(0)
	  case object C extends DNA(1)
	  case object G extends DNA(2)
	  case object T extends DNA(3)
	  case object N extends DNA(4)

      // DNAの文字列をすべて並べる。
      val values = Array(A, C, G, T, N)
	  // 用途によって別の集合を定義することもできる
	  val exceptN = Array(A, C, G, T)

	  private val codeTable = Array(A, C, G, T, N, N, N, N)
	  
      def complement(code:Int) : DNA = codeTable((~code & 0x03) | (code & 0x04))
	}
	
	// sealedを付けると、DNAを拡張したクラスはこのファイル内でしか定義できない
	// abstractを付けると、DNAを拡張したクラスはA, C, G, T, N以外にないことを保証できるので
	// match文がexhaustive(すべてのケースを網羅)になる
	sealed abstrat class DNA(val code:Int) {
	    // case objectを実装すると、クラス名を表示するtoStringが実装される
		val name = toString
		// DNAクラスには自由にメソッドを定義できる
		def complement = DNA.complement(code)
	}

このように定義すると、パターンマッチが問題なく使えるし、complementなど機能を充実させることもできる。

	val l : DNA = DNA.G
	
	l match {
	  case DNA.A => ...
	  case DNA.C => ...
	  case DNA.G => ...
	  case DNA.T => ...
	  case DNA.N => ...
	}
 
