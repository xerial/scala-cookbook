---
layout: page
title: Lesson1 - Scala Collections
tagline: Data structures in Scala
---
{% include JB/setup %}

### コードの取得

	$ cd scala-cookbook
	$ git fetch
	$ git checkout lesson1

### IntelliJプロジェクトの更新

	$ make idea

### テストコードの実行

	# bin/sbt -Dloglevel=debug "~test" のコマンドを実行
	$ make debug test="~test"
    Using C:\Users\leo\.sbt\0.11.3 as sbt dir, -sbt-dir to override.
    [info] Loading project definition from C:\Users\leo\work\git\scala-cookbook\project
    [info] Set current project to scala-cookbook (in build file:/C:/Users/leo/work/git/scala-cookbook/)
    [info] Compiling 1 Scala source to C:\Users\leo\work\git\scala-cookbook\target\test-classes...
    [Lesson1Test] 3, 2, 4, 5
    [Lesson1Test] 3, 2, 10, 5
    [Lesson1Test] 0, 1, 4, 9, 16, 25, 36, 49, 64, 81
    [info] Lesson1Test:
    [info] Lesson1
    [info] - should create arrays
    [info] - should build arrays
    [info] Passed: : Total 2, Failed 0, Errors 0, Passed 2, Skipped 0
    [success] Total time: 10 s, completed 2012/06/13 18:01:08
    1. Waiting for source changes... (press enter to interrupt)

Windows (DOS) プロンプトで実行する場合

    scala-cookbook>bin\sbt -Dloglevel=debug "~test"

### 課題

以下の作業をするコードを作成

* UCSCのDownloadページから遺伝子情報(Annotation database -> refGene.gz) のファイルをダウンロード
* ファイルに保存(保存する位置を決めておく。二回目以降は、ファイルがなければダウンロードするコードに)
* gzipを解凍しながらtab区切りのテキストをparseして、collectionに格納

#### データセット
ヒトゲノムの遺伝子ファイル：```http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refFlat.txt.gz```
サンプルデータ:

    C17orf76-AS1	NR_027160	chr17	+	16342300	16345340	16345340	16345340	5	16342300,16342894,16343498,16344387,16344681,	16342728,16343017,16343567,16344444,16345340,


### テストコードの実行

[```src/test/scala/Lesson1Test.scala```](https://github.com/xerial/scala-cookbook/blob/lesson1/src/test/scala/Lesson1Test.scala)

	# bin/sbt "~test-only *Lesson1Test" -Dloglevel=debugを実行
	$ make debug Lesson1Test

### 遺伝子データをダウンロードする

[```src/main/scala/Lesson1.scala```](https://github.com/xerial/scala-cookbook/blob/lesson1/src/main/scala/Lesson1.scala) を参考に

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

### ファイルを一行ずつ読み込む

	for (line <- Source.fromInputStream(gunzipStream(refFlat)).getLines) {
	   // do something here
	}

### 遺伝子を表すクラスを定義

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

上記は二段ループの例。Scalaのforループでは、[flatMap](http://www.scala-lang.org/api/current/index.html#scala.collection.GenTraversableLike)が使われている。
	
	for(x <- list) { ... } 

は、

	list.flatMap(x => ...)   

と同等。要素の中に、Optionの値が含まれている場合、

	for(line <- list) { ... }

は、

	list.collect(case Some(x) => ... )

の用に実行され、Someの値のみがループ中で処理される。Noneの要素は無視される。collectの中身はpartial function(一部分の入力のみに対して定義される関数)となる。つまり、不正な入力に大して例外処理を書く手間を省ける。

### Sorting

	def sortGenes(in:Array[UCSCGene]) : Array[UCSCGene] = {
		in.sortBy(gene => (gene.chr, gene.start))
	}

sortBy, sortedなどが使える。 sortByでは、sort keyとして使うデータをTupleにして返す。

### map

遺伝子名だけを取り出す例

    val genes = loadUCSCGene
    val geneNames = genes.map(g => g.name)

### filter

21番染色体のデータのみを取り出す

    val genesInChr21 = genes.filter(g => g.chr == "chr21")
    debug("genes in chr21:\n%s", genesInChr21.take(5).mkString("\n"))


### reduce

Exonの数をカウントする。以下はすべて同じ結果になる

    val exonCount = genes.map(_.exonCount).reduce(_ + _)
    val exonCount2 = genes.map(_.exonCount).reduce((a, b) => a + b)
    val exonCount3 = genes.map(_.exonCount).sum

### fold

遺伝子ごとのExonの数の平均を計算する

    val (count, sum) = genes.foldLeft((0, 0))((s, gene) => (s._1 + 1, s._2 + gene.exonCount))

### group

遺伝子を染色体ごとにグループ分け

	val geneTable = genes.groupBy(_.chr)
	// geneTable("chr21") などでアクセスできる

### Map

遺伝子名 -> UCSCGene の索引を作る

    val geneIndex = {
      val b = Map.newBuilder[String, UCSCGene]
      for(g <- genes) {
        b += g.name -> g
        b += g.refSeqName -> g
      }
      b.result
    }
    val hox1 = geneIndex("HOXA1")


### Set

染色体名の集合

    val chrSet = {
      val b = Set.newBuilder[String]
      for(g <- genes) b += g.chr
      b.result
    }

特定の染色体名のみを取り出す
	
    val chrNamePattern = """chr([0-9]+|[XY])""".r.pattern
    val commonChrSet = chrSet.filter(chr => chrNamePattern.matcher(chr).matches())


### Tuple

遺伝子情報の一部だけを取り出す

	val tuples = genes.map(g => (g.name,  g.chr, g.strand, g.start, g.end))

Tupleはあえてクラスを作るまでもないときに、簡易データ構造として使われる
   

### Parallel collection

遺伝子データをテキストに書き出し

single coreで実行

	genes.map(_.toString)
    
multi core で実行    

	genes.par.map(_.toString)

実行時間を比較してみよう

### apply, update

	val a = Array(0, 1, 3)

```a(0)``` は```a.apply(0)```のsyntax sugar
```a(0)=10```は```a.update(0, 10)```のsyntax sugar


