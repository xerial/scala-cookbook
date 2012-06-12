---
layout: default
title: Scala Quick Start
tagline: 
---
{% include JB/setup %}

## Lesson 0: Scala Quick Start


    # サンプルコードの取得
	$ git clone git://github.com/xerial/scala-cookbook.git 
	$ cd scala-cookbook 
	# lesson0 branchのコードを取り出す
	$ git checkout lesson0
	# scalaコードの実行に必要なライブラリをダウンロードし、インストール
    $ make install
	# scalaコードを実行するスクリプトを起動
	$ ~/local/bin/scala-cookbook
    Hello Scala Cookbook!


### Install IntelliJ IDEA

[IntelliJ IDEA](http://www.jetbrains.com/idea/) Community Edition をダウンロード。2012年6月の時点で最強のScala開発環境。

* ```File``` -> ```Settings``` -> ```Plugins``` -> ```Scala``` にチェックを入れる

[plugin](capture/lesson0/plugin.png)

Eclipseを使いたい場合は、[Scala IDE for Eclipse](http://scala-ide.org/)をインストールすると良い。


### Create IntelliJ project files

	$ make idea
	
File -> Open Project で、scala-cookbookのフォルダを選択

### scala-cookbookの中身
```src/main/scala/ScalaCookbook.scala```

	object ScalaCookbook {
	  def main(args:Array[String]) {
	   println("Hello Scala Cookbook!")
	  }
	}

上記のコードを実行するスクリプトが```$HOME/local/bin/scala-cookbook```としてインストールされる。

main関数の中身を書き換えて、make installを実行すると再コンパイルされる。
	$ make install
	$ ~/local/bin/scala-cookbook


### テストコードの作成

毎回、make installを実行するのは手間なので、テストコードを作成し、そこからプログラムの挙動を確認する。

* 初期設定: IntelliJで　```Settings``` -> ```Compiler``` -> ```Scala Compiler``` -> ```Project FSC``` -> scala-2.9.2 (version 2.9.2)を選択。FSCはFast Scala Compiler. コンパイル時間を短縮してくれる。

[ScalaTest](http://www.scalatest.org/) を使う。

新しいテストを作成。
![scalatest](capture/lesson0/newtest.png)
テストを作成するフォルダは、```src/test/scala```を選択。プログラム本体とテストコードを分離しておくのが慣習。

Testing libraryではScalaTestを選択する。
![scalatest](capture/lesson0/scalatest.png)


#### テストコード例

```src/test/scala/ScalaCookbookTest.scala```

	import org.scalatest.FunSuite
    
    class ScalaCookbookTest extends FunSuite {
      test("run cookbook") {
        ScalaCookbook.main(Array.empty)
      }
    }

#### テストコードの実行

テストしたいコードの上で右クリック -> Run ... を選択
![runtest](capture/lesson0/runtest.png)

