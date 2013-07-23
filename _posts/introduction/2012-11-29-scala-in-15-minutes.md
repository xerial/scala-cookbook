---
layout: post
title: "15分で始めるScala"
description: ""
category: recipes
tags: [introduction, setup]
---
{% include JB/setup %}

これはScalaを使った開発環境を素早く整え、実際の開発の雰囲気を感じてもらうための文章です。

## Update
 * 2013-07-23: Scala 2.10.2に対応しました

## ここでできるようになること

* Scalaプロジェクトの作成
* 簡単なScalaコードの作成
* テストコードの実行
  * 開発しながらテストを行なう
  * ログの表示
  * コードの実行時間の計測
* システムにインストールできる形のパッケージを作成

## 準備

* UNIX環境(Linux、 Mac OS X、あるいは [Cygwin](http://cygwin.com) をWindowsでセットアップする)
* javaコマンドが使えること (環境変数PATHの設定など)
* その他、curl, GNU makeなどのコマンド
* インターネット接続

(ここから15分です)

## Scalaプロジェクトの作成

Scalaプロジェクトの必要最低限のひな形をGitHub上に[scala-min](https://github.com/xerial/scala-min)として作成してあります。以下のようにダウンロードしながら展開します。

  	$ mkdir myproject
    $ cd myproject
    $ curl -L https://github.com/xerial/scala-min/archive/master.tar.gz | tar xvz --strip-components=1

**備考**: [GitHub](http://github.com)はオープンソースでのコード開発を支援するサービスです。Scala自身の開発もここで行われています。

## テストコードの実行

Scalaのプログラムは、[sbt(Simple Build Tool)](http://www.scala-sbt.org)を使って開発するのが標準になっています。上でダウンロードしたプロジェクト内で、

	$ ./sbt test

とすると、まずScala関連のライブラリのダウンロードが始まります。最初の1回は時間がかかりますが、2回目以降は`$HOME/.ivy2` 以下にダウンロードされたライブラリを使用するので動作が速くなります。

もしメモリの少ないマシンを利用してエラーが出る場合は、以下のように`-mem`オプションでメモリ使用量を制限できます。

    $ ./sbt -mem 512 test  # 512 MBのメモリを使用する

ダウンロードが終了すると、Scalaコードのコンパイルが始まり、テストコードが実行されます。

![scalamin-test]({{BASE_PATH}}/capture/2012-11/scalamin-test.png)


## フォルダ構成

	./sbt              # sbtの実行スクリプト
	src/main/scala     # ソースコード用のフォルダ
	src/test/scala     # テストコード用のフォルダ
	project            # プロジェクトの設定ファイル（プロジェクト名、ライブラリ、プラグイン等の設定）
	target             # コンパイルされたファイルの置き場（削除しても構わない）

## Scalaのコードを編集する

Scalaプログラムの入口は`main`関数で、以下のように定義されます。

`src/main/scala/Hello.scala`

    package scalamin
    
    object Hello {
      def main(args:Array[String]) = {
        println("Hello World!!")
      }
    }

コマンドラインの引数がargsに渡され、`main`のコードが実行されます。`main`の中身を書き換えてみましょう。


## テストコードを作成し、実行する

自分で作ったプログラムの動作を確認するために、それを動かすテストコードを作成します。

`src/test/scala/HelloTest.scala`

    class HelloTest extends MySpec {
    
      "Hello" should {
        "have main" in {
          Hello.main(Array.empty)
        }
		
	    // (その他のテストコードを同様に書いていく）
      }
	}  

英語による表記でどのようなテストを実行しているかわかるように書き下せるスタイルになっています。

* このテストコードでは[ScalaTest](http://www.scalatest.org/)ライブラリを使っています。

### 開発しながらテストを繰り返し実行する

プログラミングでは、`テストコードを作成` -> `ソースコードを修正` -> `コンパイル` -> `テストを実行`をサイクルとして行います。この支援をする機能が`sbt`に備わっています。

以下のようにテストコードを実行します。

	$ ./sbt "~test"

このコマンドはテストコードが実行された後も終了せず、ソースコードの変更があるたびに、コンパイル、テストの実行を行ってくれます。Scalaでの開発時間の短縮に重宝します。

### タグ付けしたテストのみを実行

tagを付けることで特定のテストのみを繰り返し実行できるようになります。

`src/test/scala/HelloTest.scala`

    "add a tag to test" taggedAs("test1") in {
      debug("test1 is running")
    }

**実行例**

	$ ./sbt "~test-only *HelloTest -- -n test1" -Dloglevel=debug
    Using C:\Users\leo\.sbt\0.12.0 as sbt dir, -sbt-dir to override.
    [info] Loading global plugins from C:\Users\leo\.sbt\0.12.0\plugins
    [info] Loading project definition from C:\Users\leo\work\tmp\myproject\project
    [info] Set current project to scala-min (in build file:/C:/Users/leo/work/tmp/myproject/)
    [HelloTest] test1 is running
    [info] HelloTest:
    [info] Hello
    [info] - should add a tag to test
    [info] Passed: : Total 1, Failed 0, Errors 0, Passed 1, Skipped 0
    [success] Total time: 1 s, completed 2012/11/29 12:06:24
    1. Waiting for source changes... (press enter to interrupt)


### 特定のクラスにあるテストをすべて実行

タグ指定せずに以下の用に入力すると、HelloTestクラスの中にあるすべてのテストが繰り返し実行されます。wildcard(`*`)が使えます。

	./sbt "~test-only *HelloTest"


## ログを表示する

プログラムを開発するときに、変数の内容やどの部分のコードが実行されているかなどの情報をログとして表示できると便利です。IDEでブレークポイントなどを設定しなくてもコードのデバッグがしやすくなります。

`HelloTest.scala`のテストコード中にログを表示する例があります。

    "display log messages" in {
      // To see the log messages higher than the debug level,
      // launch test with`./sbt "~test-only *HelloTest" -Dloglevel=debug`
      trace("trace log")
      debug("debug log")
      info("info log")
      warn("warning")
      error("error")
      fatal("fatal error")
    }

    "display formatted logs" in {
      val w = "World"
      info(s"Hello $w!!")
      info(f"Floating point value: pi = ${math.Pi}%.10f, rad = ${math.toRadians(math.Pi)}%.3e")
    }

`printf`などによる表示では、ログを出力するコードを本番用コードで取り除く必要があって大変ですが、`trace` < `debug` < `info` < `warn` < `error` < `fatal` の順にログレベルを分けることで、例えば以下のようにログレベルを設定し、`debug`以上のログのみを表示することができます。

	$ ./sbt "~test" -Dloglevel=debug

デフォルトでは`info`以上のログが表示される設定になっています。


* ログの表示には、[xerial-core](https://github.com/xerial/xerial) ライブラリにある`Logger` traitが使われています。
* 文字列のformatに関しては[String interpolation]({{BASE_PATH}}/recipes/2013/01/30/string-interpolation/)を参考に。

## コードの実行時間を計測する

ScalaはJava VM(JVM)の上で動作する言語で、実行時にコード最適化(Just-in-time compile)が行われます。そのためコードの性能は実行順や繰り返し回数などに大きく影響されます。コードの性能を評価する際には、常に実行時間の平均をとり、hot/cold-runかどうかを意識する必要があります。


### 具体例
Scalaではスレッドを使った処理の並列化が容易なので、single coreを使った処理と、multi coreを使った処理の性能を比較してみましょう。以下の例では`time`, `block`で挟まれたコードブロックを繰り返して実行しています。

    "measure the parallel collection peformance" in {
	  // Intの配列を作成
      val a = Array.ofDim[Int](100000)
	  (0 until a.length).foreach { i => a(i) = i }
	  
      val R = 10

      def multiply(e:Int) = e * e

	  // 全体を10回繰り返して実行する
      time("array ops", repeat=10) {
		// single-coreで配列の各要素を倍にする。R回実行
        block("single-core", repeat=R) {
          a.map( multiply )
        }

        // multi-coreを使って配列の各要素を倍にする。並列化の指示はparを挟むだけ。R回実行
        block("multi-core", repeat=R) {
          a.par.map( multiply )
        }
      }
    }



**テストの実行結果 (4 coreのマシンでの例)**

    [HelloTest]
    -array ops      total:0.705 sec., count:   10, avg:0.071 sec., min:0.049 sec., max:0.159 sec.
      -single-core  total:0.447 sec., count:  100, avg:0.004 sec., min:0.003 sec., max:0.034 sec.
      -multi-core   total:0.253 sec., count:  100, avg:0.003 sec., min:0.001 sec., max:0.078 sec.

平均してmulti coreのコードが速いが、スレッドを立ち上げるオーバーヘッドがあるので、個々の実行で見ると必ずしも並列化した方が速いとは限らない(実行時間のmaxの値を参照)。

* 実行時間の計測には、[xerial-core](https://github.com/xerial/xerial) ライブラリにある`Timer` traitが使われています。


## Scalaコードのパッケージを作成する

十分にコードをテストできたら本番環境で実行するためのプログラムパッケージを作成します。

	$ ./sbt pack

このコマンド一つで`target/pack`フォルダ内にそのまま配布できる形のパッケージができあがります。

	$ target/pack/bin/hello
	Hello World!!

**備考** 

このようなパッケージの作成を手軽にするために、今回sbt用のプラグイン[sbt-pack](https://github.com/xerial/sbt-pack)を開発しました。


## Scalaで作ったコマンドをインストールする

	$ ./sbt pack
	$ cd target/pack; make install

`$HOME/local/bin` 以下helloコマンドがインストールされます。

	$ ~/local/bin/hello
	Hello World!!
	

* プログラムの名前を変更したい場合は、`project/Build.scala`内の`packMain`設定を変更してください。

## IntelliJ IDEAで開発する

Scalaのプログラムの開発環境(IDE)としては、[IntelliJ IDEA](http://www.jetbrains.com/idea/)にScala pluginをインストールして使うのがお薦めです。IntelliJとScala pluginをインストール後、

	$ ./sbt gen-idea
	
とすると、IntelliJ用のプロジェクトファイルが作成されます。プロジェクトを開くには`File`->`Open`で、今回作成したmyprojectフォルダを選択します。

* 参考 [IntelliJ （Scala開発に使えるIDE） のセットアップ]({{BASE_PATH}}/recipes/2012/06/27/scala-quick-start/)

## もっと学びたい人は

* [Scalaの文法]({{BASE_PATH}}/recipes/2012/06/28/grammar/)
* [Scalaのコレクションを使う]({{BASE_PATH}}/recipes/2012/06/28/using-scala-collections/)
* [Scala Cookbook]({{BASE_PATH}})には、Scalaの開発でつまづきやすい点についてのヒントがあります。
* [Scalaを学ぶ]({{BASE_PATH}}/recipes/2012/06/28/scala-references/) - Scalaを学ぶのにおすすめの資料をまとめてあります。

