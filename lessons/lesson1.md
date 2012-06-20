---
layout: page
title: Lesson1 - Scala Collections
tagline: Data structures in Scala
---
{% include JB/setup %}

### コードの取得

	$ git checkout git://github.com/xerial/scala-cookbook.git
	$ cd scala-cookbook
	$ git fetch
	$ git checkout lesson1

### IntelliJプロジェクトの更新

	# bin/sbt gen-ideaを実行
	$ make idea

### テストコードの実行

	# bin/sbt -Dloglevel=debug "~test-only *Lesson1Test" のコマンドを実行
	$ make debug test="Lesson1Test"


Windows (DOS) プロンプトで実行する場合

    scala-cookbook>bin\sbt -Dloglevel=debug "~test-only Lesson1Test"

上記のコマンドで、ソースコードに変更を加えるたびに、コンパイル、テストを実行を自動的に行ってくれる。

### 課題

以下の作業をするコードを作成

* UCSCのDownloadページから遺伝子情報(Annotation database -> refGene.gz) のファイルをダウンロード
* ファイルに保存(保存する位置を決めておく。二回目以降は、ファイルがなければダウンロードするコードに)
* gzipを解凍しながらtab区切りのテキストをparseして、collectionに格納
* 作成した遺伝子リストを使って、Array, Map, Set, Tupleの使い方を学ぶ

#### データセット
ヒトゲノムの遺伝子ファイル：```http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refFlat.txt.gz```
サンプルデータ:

    C17orf76-AS1	NR_027160	chr17	+	16342300	16345340	16345340	16345340	5	16342300,16342894,16343498,16344387,16344681,	16342728,16343017,16343567,16344444,16345340,

各行が、commonName, refSeqName, chr, strand, start, end, cdsStart, cdsEnd, exonCount, exonStarts, exonEnds の順にtab区切りで並んでいる。 exonStarts, exonEndsはmicro formatになっており、comma区切り。ただし、commaが最後に１つ余計についているので注意が必要。

### テストコードの実行

[```src/test/scala/Lesson1Test.scala```](https://github.com/xerial/scala-cookbook/blob/lesson1/src/test/scala/Lesson1Test.scala)

	# bin/sbt "~test-only *Lesson1Test" -Dloglevel=debugを実行
	$ make debug test=Lesson1Test

### 今回のサンプルコード

[```src/main/scala/Lesson1.scala```](https://github.com/xerial/scala-cookbook/blob/lesson1/src/main/scala/Lesson1.scala) 

コード中の```debug```, ```info```, ```time```などのメソッドはScalaコードのデバッグ用に私が普段使っているもの。

### ScalaのAPI

* [Scala API](http://www.scala-lang.org/api/current/index.html) 

### 遺伝子データをダウンロードする

ほとんどJavaの道具を使っている。チャンネルを開いて、省メモリでファイルに書き出す。

    val input = Channels.newChannel(new URL(url).openStream)
    val out = new FileOutputStream(outputFile).getChannel
    try {
      out.transferFrom(input, 0, Integer.MAX_VALUE)
    }
    finally {
      input.close
      out.close
    }

### gzipを解凍する

	def gunzipStream(file: File) = new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)))

それぞれのクラスについては、Java APIを参照のこと。

### ファイルを一行ずつ読み込む

	for (line <- Source.fromInputStream(gunzipStream(refFlat)).getLines) {
	   // do something here
	}

### 遺伝子を表すクラスを定義

遺伝子はパラメータ数が多いので、クラスを定義すると良い。クラスの定義には変数：型のリストを引数として与えるだけでよい。

	class UCSCGene(val name: String,
                 val refSeqName: String,
                 val chr: String,
                 val strand: String,
                 val start: Int,
                 val end: Int,
                 val cdsStart: Int,
                 val cdsEnd: Int,
                 val exonCount: Int,
                 val exonStarts: Array[Int],
                 val exonEnds: Array[Int]) {
	    override def toString = "name:%s refSeqName:%s, %s, %s:%d-%d".format(name, refSeqName, strand, chr, start, end)
	}

クラス定義の詳細については次回以降に。```toString```メソッドを定義し、自分の好みのメッセージを表示をさせるようにすると、loggerやIDEでデバッグするときにオブジェクトの内容が表示されるので便利。

### 遺伝子データの構文解析

	object UCSCGene {
        def parse(line: String): Option[UCSCGene] = {
          def splitByComma(s: String): Array[Int] = {
            val ss = if (s.endsWith(",")) s.slice(0, s.length - 1) else s
            ss.split(",").map(x => x.toInt)
          }
          val c = line.split("\\t")
          if (c.length != 11) {
            error("Wrong number of columns:%d\n%s", c.length, line)
            // Report None instead of issuing an error
            None
          }
          else {
            Some(new UCSCGene(c(0), c(1), c(2), c(3), c(4).toInt, c(5).toInt, c(6).toInt, c(7).toInt, c(8).toInt, splitByComma(c(9)), splitByComma(c(10))))
          }
        }
	}

parseする際、データにエラーがあっても例外を飛ばしていない。[Option](http://www.scala-lang.org/api/current/index.html#scala.Option)を使うことで、コードの流れを妨げないようにできる。
* 正しく読めた -> Some(parseした結果)
* 正しく読めなかった -> None 

### コレクションを構築する
サイズの小さいデータなら、以下のように作成できる。

	Array(1, 3, 5)
	List("A", "C", "G", "T")
	Map(0 -> "A", 1 -> "C", 2 -> "G", 3 -> "T", 4 -> "N")

手で書き下せない大きなデータに大しては、Builderを用いてコレクションを作成する

### mkStringで、コレクションの内容の表示

	Array(1, 3, 5).mkString(", ") //  "1, 3, 5"

問題：mkStringと同等の関数を作成してみよ。StringBuilderを使うと良い。

	val b = new StringBuilder
	b += ... 

### Array

遺伝子情報の配列を作成。Builderに```+=```で要素を追加していき、最後に```result```で配列を取り出す。

	// Create an array of genes
    val b = Array.newBuilder[UCSCGene]

    // Read the unzipped file line by line
    for (line <- Source.fromInputStream(gunzipStream(refFlat)).getLines;
         gene <- UCSCGene.parse(line)) {  // pattern match is used
      b += gene
    }
    b.result

#### for loop
上記は二段ループの例。Scalaのforループでは、[flatMap](http://www.scala-lang.org/api/current/index.html#scala.collection.GenTraversableLike)が使われている。
	
	for(x <- list) { ... } 

は、

	list.flatMap(x => ...)   

と同等。

#### Option 
要素の中に、Optionの値が含まれている場合、

	for(line <- list) { ... }

は、

	list.collect(case Some(x) => ... )

の用に実行され、Someの値のみがループ中で処理される。Noneの要素は無視される。[collect](http://www.scala-lang.org/api/current/index.html#scala.collection.GenTraversableLike)の中身はpartial function(一部分の入力のみに対して定義される関数)となる。つまり、不正な入力に対して例外処理を書く手間を省ける。


### Sorting

	def sortGenes(in:Array[UCSCGene]) : Array[UCSCGene] = {
		in.sortBy(gene => (gene.chr, gene.start))
	}

sortBy, sortedなどが使える。 sortByでは、sort keyとして使うデータをTupleにして返す。sortedを使うと、２つの要素を比較するOrdering関数(全順序を定義するもの)を使える。

* 参考 [ArrayOpsで定義されているsort関連の関数](http://www.scala-lang.org/api/current/index.html#scala.collection.mutable.ArrayOps)

### map

コレクションのそれぞれの要素に対して関数を適用。

遺伝子名だけを取り出す例:

    val genes = loadUCSCGene
    val geneNames = genes.map(g => g.name)

### filter

コレクションの要素から条件に合うものだけを取り出す：

21番染色体のデータのみを取り出す:

    val genesInChr21 = genes.filter(g => g.chr == "chr21")
    debug("genes in chr21:\n%s", genesInChr21.take(5).mkString("\n"))


### reduce

要素と要素を合わせてコレクションを縮めていく(reduce).

Exonの数をカウントする。以下はすべて同じ結果になる

    val exonCount = genes.map(_.exonCount).reduce(_ + _)
    val exonCount2 = genes.map(_.exonCount).reduce((a, b) => a + b)
    val exonCount3 = genes.map(_.exonCount).sum

### fold

初期値を与え、それと各々の要素を折り畳んでいく(fold)

遺伝子ごとのExonの数の平均を計算する:

    val (count, sum) = genes.foldLeft((0, 0))((s, gene) => (s._1 + 1, s._2 + gene.exonCount))

### groupBy

遺伝子を染色体ごとにグループ分け

	val geneTable = genes.groupBy(_.chr)
	// geneTable("chr21") などでアクセスできる

### Map

key, valueのペアによるデータ構造。keyの値による検索をO(log N)に。

遺伝子名 -> UCSCGene の索引を作る:

    val geneIndex = {
      val b = Map.newBuilder[String, UCSCGene]
      for(g <- genes) {
        b += g.name -> g
        b += g.refSeqName -> g
      }
      b.result
    }
    val hox1 = geneIndex("HOXA1")

Scalaに限らず一般のプログラミングでも、mutable（変更可能）な変数はなるべく表に出さずに使うのがバグを減らす秘訣。builderクラスの中身は変化しうるので、```geneIndex```の作成時には、コードブロックで囲んで、builderクラスの使用を外側から隠すようにしている。

### Set

重複を許さない集合を扱うときに使う。

染色体名の集合を計算:

    val chrSet = {
      val b = Set.newBuilder[String]
      for(g <- genes) b += g.chr
      b.result
    }

特定の染色体名のみを取り出す:
	
    val chrNamePattern = """chr([0-9]+|[XY])""".r.pattern
    val commonChrSet = chrSet.filter(chr => chrNamePattern.matcher(chr).matches())


### Tuple

遺伝子情報の一部だけを取り出す

	val tuples = genes.map(g => (g.name,  g.chr, g.strand, g.start, g.end))

Tupleはあえてクラスを作るまでもないときに、簡易データ構造として使われる。データ操作が入り組んでくると、クラスを作成しパラメータに名前を与えてあげる方が可読性が良くなることも多い。

tupleの各要素には、_1, _2, ...などでアクセスし、要素をコレクションとして辿るには```tuple.productIterator```を呼び出す。

### Parallel collection

Scalaでのマルチコア並列化は驚くほど簡単。コレクションの```par```関数を呼び出すだけ。

遺伝子データをテキストに書き出す例：

single coreで実行

	genes.map(_.toString)
    
multi core で実行    

	genes.par.map(_.toString)

実行時間を比較してみよう。

* Java での実行時間の計測は、JVMの実行時最適化や、GCなどの影響により、コードの実行順に大きく左右される。ベンチマークを取るときは、コードの実行順を入れ替える、何十回か計算を繰り返して平均を取るなどの工夫が必要。サンプルコード中の```time(...)```, ```block(...)```はそのようなベンチマークを取る手助けをしてくれる。repeat回数を指定できる。

コードの実行時間を計測。3回繰り返す。

	time("gene report", repeat=3) {
      block("single core") {
        val geneReport = genes.map(_.toString)
      }
      block("parallel") {
        val geneReport = genes.par.map(_.toString)
      }
    }

計測結果 (4 coresの場合)
    [TimeMeasure$] 
    [gene report]	total:1.529 sec., count:3, avg:0.510 sec., min:0.322 sec., max:0.873 sec.
      [single core]	total:1.173 sec., count:3, avg:0.391 sec., min:0.236 sec., max:0.697 sec.
      [parallel]	total:0.350 sec., count:3, avg:0.117 sec., min:0.086 sec., max:0.171 sec.

### apply, update

	val a = Array(0, 1, 3)

```a(0)``` は```a.apply(0)```のsyntax sugarで、```a(0)=10```は```a.update(0, 10)```のsyntax sugarになっている。


