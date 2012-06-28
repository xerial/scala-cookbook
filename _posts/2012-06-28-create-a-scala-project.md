---
layout: post
title: Scalaプロジェクトの作成
description: "sbtを使って配布可能なコードを作成する"
category: recipes
tags: [setup]
---
{% include JB/setup %}

## sbt

Scalaのコンパイル、テストの実行にはsbt (Simple Build Tool) (全然simpleではないが!) を使うのが2012年現在でのbest practice. IDEではあまりコンパイルしない。

* [SBT Getting Started Guilde](https://github.com/harrah/xsbt/wiki/Getting-Started-Welcome) 

### sbtでできること

* Scalaのコンパイル
* ライブラリの自動ダウンロード
* コードライブラリの作成
  * Scala, Javaで動くものはすべて使える
* 作成したライブラリを公開サーバーにアップロードする
* テストの実行

## sbtを使ったおすすめの最小構成

	# min-projectブランチをmyprojectフォルダ内に取得
	$ git clone git://github.com/xerial/scala-cookbook -b min-project myproject
	$ cd myproject
	# プロジェクトに必要なファイルをダウンロード、コンパイル、実行
	$ bin/sbt run

### ファイル・フォルダ構成

	bin/sbt             sbtを実行するスクリプト (Windowsの場合は、sbt.bar)
	bin/sbt-launch.jar  sbt本体
	src/main/scala      Scala/Javaのソースコード置き場
	src/main/resources  プログラム中で必要なデータファイルなど
	src/test/scala      テストコード置き場
	src/test/resources  テスト時に必要なサンプルデータファイルなど
	project/Build.scala プロジェクトの設定
	project/Project.scala  配布可能なパッケージを作成する
	project/build.sbt   sbtのプラグインの設定
    lib                 mavenなどで見つからないライブラリ(jar)の置き場
	.gitignore          gitで管理しないファイルの設定
	

### プロジェクトの定義

```project/Build.scala```

	import sbt._
	import Keys._

	object ProjectBuild extends Build {
    lazy val root = Project(
         id ="sample-project",  // Set your project name here (artifact-id)
         base = file("."),
         settings = 
           Defaults.defaultSettings 
           ++ Seq(PackageTask.packageDistTask) 
           ++ PackageTask.distSettings 
           ++ Seq(
           	  scalaVersion := "2.9.2",
    	      organization := "org.utgenome.sample", // groupidを設定
           	  version := "1.0-SNAPSHOT",
           	  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
        	  parallelExecution := true,
        	  crossPaths := false,
           	  libraryDependencies ++= Seq(
    	         "org.codehaus.plexus" % "plexus-classworlds" % "2.4",
                 "org.scalatest" %% "scalatest" % "2.0.M1" % "test"
    	      // Add other libraries here
    	  )
         )
       )
    }


### ライブラリの追加
* [Maven Repository Search](http://search.maven.org/) で必要なライブラリの、group id, artifact id, version名を調べる。
* Build.scalaの```libraryDependencies```に追加
  
例：[sqlite-jdbc](http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC) (SQLiteデータベースをJava/Scalaで使うライブラリ)を追加
	
	libraryDependencies ++= Seq(
	   (他のライブラリ...), 
	   "org.xerial" % "sqlite-jdbc" % "3.7.2"
	) 


### ライブラリの作成

	$ bin/sbt publish-local
	
```$HOME/.ivy2/local/(group id)/(artifact id)-(version)```以下に、コードライブラリ(jar, javadoc, source codeのjarなど)が作成される。作成されたものにテストコードは含まれない。

group idは、自分の持っているドメイン名に対応するものを使うのが慣習。```utgenome.org```を保有しているなら、```org.utgenome```がgroup id。 


### IntelliJのプロジェクトの作成

	$ bin/sbt gen-idea
	




