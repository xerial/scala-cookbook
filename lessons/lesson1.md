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

* UCSCのDownloadページから遺伝子情報(Annotation database -> refGene.txt.gz) のファイルをダウンロード
* ファイルに保存(保存する位置を決めておく。二回目以降は、ファイルがなければダウンロードするコードに)
* gzipを解凍しながらtab区切りのテキストをparseして、collectionに格納

#### データセット
ヒトゲノムの遺伝子ファイル：```http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refFlat.txt.gz```
データベーススキーマ：```http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refFlat.sql```

    CREATE TABLE `refGene` (
      `bin` smallint(5) unsigned NOT NULL,
      `name` varchar(255) NOT NULL,
      `chrom` varchar(255) NOT NULL,
      `strand` char(1) NOT NULL,
      `txStart` int(10) unsigned NOT NULL,
      `txEnd` int(10) unsigned NOT NULL,
      `cdsStart` int(10) unsigned NOT NULL,
      `cdsEnd` int(10) unsigned NOT NULL,
      `exonCount` int(10) unsigned NOT NULL,
      `exonStarts` longblob NOT NULL,
      `exonEnds` longblob NOT NULL,
      `score` int(11) default NULL,
      `name2` varchar(255) NOT NULL,
      `cdsStartStat` enum('none','unk','incmpl','cmpl') NOT NULL,
      `cdsEndStat` enum('none','unk','incmpl','cmpl') NOT NULL,
      `exonFrames` longblob NOT NULL,
      KEY `chrom` (`chrom`,`bin`),
      KEY `name` (`name`),
      KEY `name2` (`name2`)
    ) ENGINE=MyISAM DEFAULT CHARSET=latin1;

### Builder

collectionを作成する

### Array

* 遺伝子情報の配列

### Sorting

* 並び替える

### map, reduce, fold

* 遺伝子の位置情報だけを取り出す
* 遺伝子長の平均をとる


### group

* 遺伝子を染色体ごとにグループ分け


### List



### Map

遺伝子名 -> Gene の索引を作る

### Set

染色体名の集合

### Tuple

遺伝子情報の一部だけを取り出す

   
### Immutableとmutable

Immutable（変更できない）

### apply, update

syntax sugar

### Parallel collection

遺伝子データをテキストに書き出し

* single coreで実行
* multi coreで実行

