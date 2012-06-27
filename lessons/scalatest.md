---
layout: page
title: Writing Test Codes
tagline: テストコードの作成
---

# テストコード

プログラムが正しく動くかどうかを検証するには以下の方法が考えられます：

* 型が合っているかを確認する (Scalaのコンパイラが検査)
* あらゆる入力に対して正しい結果を得るコードであることを証明する (soundness, completenessの検証。アルゴリズムの論文を書くときには必須)

最後の検証方法を毎回行うのは大変なので、手軽な方法として

* コードを動かしてみて結果を確認する

アプローチが経験的にうまくいくことが知られています。

## ScalaTestによるテストコードの作成

Scalaでのテストコード作成には

 * [ScalaTest](http://www.scalatest.org/)
 * [Specs2](http://etorreborre.github.com/specs2/)
 
が有名です。今回はScalaTestについて紹介します。


## テストコードの実行

[Scalaのプロジェクトの作成](lessons/min-project.html)の例では、ScalaTestをすぐ使えるようになっています。```libraryDepenedencies```に、
	
    "org.scalatest" %% "scalatest" %% "2.0.M1" % "test"

の記述があります。


	# テストコードを実行
	$ bin/sbt test

	# テストコードを繰り返し実行
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
	     "read tar.gz fasta files" taggedAs(Tag("debugging")) in {
	       ...
	     }
	  }
	}

特定のテストコードを繰り返して実行

    $ bin/sbt "~test-only *FASTATest"
	
タグを付けたテストコードのみを繰り返して実行

	$ bin/sbt "~test-only *FASTATest -- include(debugging)
	
	
	
