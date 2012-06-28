---
layout: post
title: "プログラムの挙動をテストする"
description: "sbtでテストを実行しながら開発"
category: recipes
tags: [debug]
---


プログラムが正しく動くかどうかを検証するには以下の方法が考えられます：

* 型が合っているかを確認する (Scalaのコンパイラが検査してくれる)
* あらゆる入力に対して正しい結果を得るコードであることを証明する (soundness)。
* そのコードで必要な結果をすべて計算できる(completeness)の検証。

最後の検証方法を毎回行うのは大変なので(アルゴリズムの論文を書くときには必須ですが)、手軽な方法として

* コードを動かしてみて結果を確認する

このアプローチが経験的にうまくいくことが知られています。

## ScalaTestによるテストコードの作成

Scalaでのテストコード作成には

 * [ScalaTest](http://www.scalatest.org/)
 * [Specs2](http://etorreborre.github.com/specs2/)
 
が有名です。今回は構文が比較的わかりやすいScalaTestについて紹介します。


## テストコードの実行

[Scalaのプロジェクトの作成]({{BASE_PATH}}/recipes/2012/06/28/create-a-scala-project/)の例では、ScalaTestをすぐ使えるようになっています。```libraryDepenedencies```に、
	
    "org.scalatest" %% "scalatest" %% "2.0.M1" % "test"

を追加する記述があるのを確認してください。


	# テストコードを実行
	$ bin/sbt test

	# ソースコードのの更新がある度にテストコードを繰り返し実行
	$ bin/sbt "~test"
	
## テストコードの作成

ScalaTestではいろいろなスタイルでテストコードが作成できます。以下は、[WordSpec](http://www.scalatest.org/scaladoc/1.8/#org.scalatest.WordSpec)での例：

	// よく使う機能の組み合わせをtraitにまとめておくと便利
	trait MySpec extends WordSpec with ShouldMatcher
	
	class FASTATest extends MySpec {
	  "A parser" should {
		 "read .gz fasta files" in {
		   ...
		 }
	     "read tar.gz fasta files" taggedAs(Tag("debug")) in {
	       ...
	     }
	  }
	}

特定のテストコードを繰り返して実行

    $ bin/sbt "~test-only *FASTATest"
	
## タグを付けたテストコードのみを繰り返して実行

ScalaTestのテストコードには、`taggedAs(Tag("tagname"))`でタグを付けるこ
とができます。テストを実行する際に、以下のように指定されたタグが付いた
テストのみを実行することができます。一部のコードに集中してデバッグした
いときに便利。

	$ bin/sbt "~test-only *FASTATest -- include(debug)
	
	
	
