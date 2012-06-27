---
layout: page
title: Lesson1 - Scala Collections
tagline: Data structures in Scala
---
{% include JB/setup %}

### コードの取得

	$ git clone git://github.com/xerial/scala-cookbook.git
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

### Array



### List


### Map


### Set


### Tuple

   
### Immutableとmutable

Immutable（変更できない）

### apply, update

syntax sugar

