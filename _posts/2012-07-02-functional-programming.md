---
layout: post
title: "関数型言語の特徴"
description: "副作用を避ける"
category: recipes
tags: [introduction]
---
{% include JB/setup %}

## コーディングのスタイル
* コードを逐次実行しながら副作用（変数の内容の書き換え）を起こす[命令型 (imperative programming)](http://en.wikipedia.org/wiki/Imperative_programming)のコードをなるべく排除する
* 副作用を避けるために
  * **immutable**（変更不可能）なデータを中心に使う
  * 値の変化は、関数に「immutableなデータを入力 -> 新しいデータを出力」という形で行う
* 関数そのものも、関数の引数として渡す
  * 関数がfirst-class citizen、という言い方をよくする
  * C++、Javaでも関数に関数へのポインタ（リファレンス）などを渡せるが、関数が定義されたコンテキストの情報（変数の値など）までも含めて他の関数に渡すのは大変。

## **Q**. 副作用を避けるのは何故か？ 

**A**. プログラミングを簡単にするため。

例えば、文字列型 String のデータが変更可能だったとする。

    val key1 = "Apple"
    val key2 = "Banana"
    val set = SortedSet(key1, key2)  // "Apple", "Banana"の順に並び替えを維持するデータ構造
    
    // もしkey1の内容を以下の用に書き換えられるとしたら。。。
    key1(0) = "Z"    // key1は"Zpple"になる
    // ここでSortedSetの中身はどうなっている？ "Banana", "Zapple"の順番になっていてほしいが。。。


keyの値が外部で変化すると、SortedSetの中で保管しているkeyの並び順も更新する必要がある。
これに対処する方法として、

 1. SortedSetに格納されているデータの変更を探知して、並び順を更新する
 1. SortedSetに格納する際に、データのコピーを作成して、コピーをSortedSetに格納する
 1. SortedSetにアクセスするたびに並び替えを行う

などが考えられる。

1番目の方法はひどく実装が大変になる。keyの値の変更を探知する[Observer](http://en.wikipedia.org/wiki/Observer_pattern)をkey毎に用意し、Stringが更新されるたびに並び順を変更する。もし、マルチスレッドプログラミ
ングを行っている場合、SortedSetへのアクセスとSortedSetの内容の更新が衝突しないように排他制御を行う必要がある。2番目の方法は簡単だがデータのコピーのコストが重い。3番目の方法になるようでは、SortedSetのようにO(log n)でデータを検索できるデータ構造を使う意味を失う。

また、このようなコードをデバッグするのは困難を極める。
global変数(プログラムのどこからでも変更できる)を用いたコードが今日では衰退しているのは、
変数の内容の変化を起こすコードと、その変化のタイミングを管理するのが大変だったことによる。

### 解決策

Stringをimmutableにすればよい。

immutableにすることで、上記のようなことで悩む必要はなくなる。実際、
Scala/JavaのStringはimmutableになっている。また、SortedSet などの実装はimmutableなデータが格納されることを前提にしており、
性能のためにデータのコピーを避け、文字列へのリファレンスのみを格納している。
parallel/concurrent programmingにおいても、StringやSortedSetの内容がimmutableであることが保証されていると、ロックなどを取得する必要がなく、
コードの性能も良くなる上、コードの実行の度に動作が違うバグなどに悩まされなくなる。

## immutableなデータを使い、初期化忘れを防ぐ

Scalaではclassでデータ構造を作成する際も、パラメータはすべてimmutableにし、初期化を必ず行うように強制できる。

    class Book(id:Int, title:String, publisher:String) 
	
	val b = new Book(1, "Programming in Scala", "Artima Press")


Javaではimmutableであることに注意しないと、以下のようなコードを書いてしまう。

	// このような書き方は避けたい
	class Book {
	   public int id;
	   public String title;
	   public String publisher;
	}
	
	Book b = new Book();
	b.id = 1;
	b.title = "Programming in Scala";
	// publisherの情報を設定するのを忘れてしまった!!

	System.out.println(b.publisher); // NullPointerExceptionが発生

Javaで安全にクラスの初期化を行えるようにするには以下のようにする。

	class Book {
		// finalを付けると、初期化時以外変更不能な変数になる
		public final int id;
		public final String title;
		public final String publisher;
		
		public Book(int id, String title, String publisher) {
			this.id = id;
			this.title = title;
			this.publisher = publisher;
		}
	}

Scalaでは、immutableなデータを好んで使ってもらえるよう配慮されており、以下の一行で済む。

	class Book(id:Int, title:String, publisher:String) 

### 補足

JavaではBeans（データベースやJSONなどのデータをもとに、クラスを初期化する）などを使う場合、
止むを得ず上記のようにpublicなフィールドを使って、
安全でない書き方をすることがある。[Builder pattern](http://en.wikipedia.org/wiki/Builder_pattern)を使うなど、
いくつか初期化の安全性を確保する方法があるが、楽な書き方とは言いがたい。
	


 
 


