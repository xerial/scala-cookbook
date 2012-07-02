---
layout: post
title: "Scalaの利点"
description: "関数型言語として。オブジェクト指向言語として"
category: recipes
tags: [introduction]
---
{% include JB/setup %}

バイオインフォマティクスの分野では、現場で動くコードを書けるプログラミング言語でないと使えません。
Scalaが本当に現場で使えるかどうかわかるまで、実際にコードを書いてみたり、開発環境、ライブラリを調べるなど調査に費やした時間も膨大でした。
そのときの自分に教えてあげるつもりでScalaの利点をここにまとめていきます。

## 関数型言語であるが、関数型言語でない
Scalaでは関数型言語のスタイルに固執する必要がない。命令型、副作用のあるコードも書けるので、慣れるに従い関数型のスタイルに近づければ良い。
C++, Java、Perlなど命令型のコードが多い言語に慣れていると、最初のうちは関数型のコードをどう書けば良いかわからないことが多いと思うが、
[Scala API](http://www.scala-lang.org/api/current/index.html)にあるライブラリの使い方に習熟してくると、これらをどう組み合わせてコードを書けばよいかが見えてくるようになる。

[Martin Odersky](http://lampwww.epfl.ch/~odersky/)のProgramming in Scalaの本にも、_well-trained eyes_ (訓練された目では) という表現がよく出てくるが、
私自身も、Scalaを覚えたての頃と、Scalaに慣れた現在ではコードを見る目、書き方がずいぶんと変わった。

## 簡潔にコードを書ける

型推論や構文の工夫により、Rubyなど動的型付け言語と同じくらいコーディングがしやすくなっている。

	// Mapの作成
	val m = Map(1 -> "A", 2 -> "B", 3 -> "C") 
	for((key, value) <- m) {
		...
	}

これを敢えて冗長に書くと、、、

	val m : Map[Int, String] = 
		Map.apply[Int, String](Seq[Tuple2[Int, String]]
		(new Tuple2[Int, String](1, "A"), new Tuple2[Int, String](2, "B"), new Tuple2[Int, String](3, "C"))
	// 型名をすべて補って書き下すと...
	m.foreach[Tuple2[Int, String]]{ (entry:Tuple2[Int, String])  => 
	   entry match {
		   case (key:Int, value:String) => ...
	   }
	}

となるが、このような詳細をコーディング時に気にする必要がない。(もちろん詳細を知っておくと、Scalaでコードライブラリを開発するときの力になる)


## 開発環境、ライブラリの充実

[IntelliJ](http://www.jetbrains.com/idea/), [sbt](https://github.com/harrah/xsbt/)などが
コミュニティでよく使われており、大きなプロジェクトの開発にもScalaは実用的に使えるようになってきた。

[Eclipse](http://www.eclipse.org/)で[Scala IDE for Eclipse](http://scala-ide.org/)を使っても一応開発できるが、2012年7月の時点では、
IntelliJ + Scalaプラグインの方が使い勝手(syntax highlight, type inferenceによる文法エラーの検知)が良い。

sbtで特筆すべき点は、コードの更新をモニターして、変更があればすぐ再コンパイルを行い、テストコードの実行までを自動で行ってくれる。
Scalaのコードはコンパイルに多少時間がかかるが、この機能により開発時のストレスが少ない。

Javaで一般的な[Maven](http://maven.apache.org/)による開発スタイルを踏襲することもできるが、sbtを使う方が良い。
mavenにできてsbtにできないこともたくさんあるが、sbtにできてmavenにできないことを実装する方が大変に思う。
sbtの拡張はsbtのソースコードとにらめっこして、Scalaで書けばよい。作成したプラグインもGitHubに置くなどの手段が使える。一方mavenプラグインでは、XMLによる仕様の記述、クラスの階層関係の把握、maven centralにdeployするなど、一筋縄ではいかない箇所が多くある。

Javaのコードとの親和性に関しても、Scala 2.8でcollectionクラスの大幅な改善により、格段に使い勝手が良くなった。
Scala2.9では、並列処理のためのコレクションの拡張が行われており、マルチコアのための計算も簡単になっている。
実際、私自身も10CPU以上を使った演算などを日常的に行えるようになって助かっている。


## 練られた言語設計

ScalaはJavaと同様JVMの上で動く言語であるが、オブジェクト指向言語としてだけ見ても、Javaと比較して改善されている点が多々ある。

* implicit conversionによる機能の追加

例えば、Stringにはformatというメソッドはないが、[StringOps](http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.StringOps)などに自動的に変換して機能を追加できる。

	"Hello %s!".format("World") 

	// Javaでは以下のように書かなければならなかった
	String.format("Hello %s", "World")

* Java言語のリリース頻度は、使っているユーザー数が多いために慎重になっているためか、非常に遅い。新しいリリースを待つくらいならScalaでコードを書き始める方が良い
* 例えば以下のような機能が待望されているが、Javaで使えるようになるのがいつになることやら…
  * コードブロック (clojure, lambda function)
  * try catch with resources (Scalaなら[loan pattern]({{BASE_PATH}}/recipes/2012/06/27/loan-pattern)を自分で実装すればよい)
  * traitによるmixin(多重継承の特殊なケース)の実現  
    * コードの再利用、拡張がより広く可能になった
	* 例えば、[Iterator](http://www.scala-lang.org/api/current/index.html#scala.collection.Iterator)を継承してメソッドを２つ(hasNext, next)実装するだけで、foreach, map, foldなどcollectionクラスで使える便利なメソッドがすべて追加される。
    * linearlizationにより、C++などの多重継承で問題だった階層関係の順番の曖昧さを解決

* Covariance, contravarianceの導入により、自然な型のマッピングが可能に

Covarianceの例 (List[Banana], List[Apple]はList[Fruit]のsubtypeとして扱える)

	trait Fruit
	class Apple extends Fruit
	class Banana extends Fruit
	// ListはList[+A] (covariance)として定義されている
	val l : List[Fruit] = List[Banana](new Banana, new Banana) ++ List[Apple](new Apple)


* 関数に渡す関数の型なども、covariance, contravarianceのおかげで汎用的になり、コードの再利用性が高まっている. `Function2[-A, +B]`など。
* Checked exceptionの廃止
   * Javaでは`method(..) throws xxException`という形でexceptionの型までメソッドに指定しなくてはならないために、汎用的なライブラリを書く障害になっていた。例えば同じようなコードを再利用できる場所でも、DBException, IOExceptionなど内部で発生する例外の型が違うために、APIでは親クラスのthrows Exceptionを使うように設計しなくてはならず、APIを使う側では何のエラーだかわからないExceptionをcatchするコードを大量に書く必要があった。Scalaでは、throws ... と書かなくても良くなり、programのmain関数内など、必要最低限の位置で例外をcatchすれば良いようになっている。

* Pattern matchingの機能
   * [パターンマッチ](http://www.artima.com/scalazine/articles/pattern_matching.html)が実装されているおかげで、[Visitorパターン](http://en.wikipedia.org/wiki/Visitor_pattern)をもう書かなくてもいいと思うだけでありがたい。

* Type erasureへの対応
  * Scalaのクラスファイルには、実は詳細な型情報を記したsignatureが埋め込まれており、JVMでtype erasureにより実行時に失われてしまうような型情報も、Scalaでは実行時に取り出すことができる（ただしScalaで書かれたクラスに限る）

### Scalaの言語デザインについてためになる記事

Scalaを作ったMartin Odersky氏への以下のインタビュー記事を読むと、なぜScalaの言語が今のようなデザインになっているのかがよくわかる。妥協もあり、積極的に関数型言語、オブジェクト指向言語の融合をはかった部分もあり。

* [The Origins of Scala - A Conversation with Martin Odersky](http://www.artima.com/scalazine/articles/origins_of_scala.html)
* [The Goals of Scala's Design - A Conversation with Martin Odersky](http://www.artima.com/scalazine/articles/goals_of_scala.html)
* [The Purpose of Scala's Type System - A Conversation with Martin Odersky](http://www.artima.com/scalazine/articles/scalas_type_system.html)
