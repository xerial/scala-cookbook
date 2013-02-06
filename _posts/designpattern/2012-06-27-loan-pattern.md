---
layout: post
title: "Loan Pattern"
description: "借りたら返す"
category: recipes
tags: [design pattern]
---
{% include JB/setup %}


プログラミングでは、「リソースを取得したら解放する」パターンが頻出します。例えば、

* ファイルを開いたら閉じる
* dbへのコネクションを開いたら閉じる
* ロックを取得したら、リリースする

などがあります。コード中でこの「借りたら返す」を確実に行うのが**Loan Pattern**です。

## 例外安全でない例

	def query(sql:String) = {
	   val connection = db.getConnection
	   val result = connection.query(sql)  // もしクエリの実行中に例外が発生したら？
	   connection.close       // このコードが確実に呼ばれる保障がない
	   result
	}

## 例外安全だがコードの再利用がしにくい書き方

try ... finallyで囲むと例外(exception)に強いコードが書ける。

	def query(sql:String) = {
	   val connection = db.getConnection
	   try 
		   connection.query(sql)  // クエリの実行中に例外が発生しても。。。
	   finally	   
		   connection.close       // このコードは確実に呼ばれる
	}

しかし、実際にはqueryだけでなくupdateなどもしたいはず。

	def update(sql:String) = {
	   val connection = db.getConnection
	   try 
		   connection.update(sql)  
	   finally	   
		   connection.close       
	}

同じようなコードなのに、繰り返して書かなくてはいけない。

## Loan Pattern

Scalaではコードブロック（つまり関数）を引数として渡せるので、パターンの再利用が可能

	// loan pattern
	def open[A](body:Connection => A) : A = {
		val connection = db.getConnection
		try 
			body(connection)
		finally 
			connection.close
	}

	// loan patternの利用
	def query(sql:String) = open(_.query(sql))
	def update(sql:String) = open(_.update(sql))


## 関連項目

C++で似たようなパターンを実現するには以下の手法がある

* [scoped_ptr](http://www.boost.org/doc/libs/1_39_0/libs/smart_ptr/scoped_ptr.htm)は、ブロックから出たときにリソースの解放を行う。constructorで初期化、ブロックを出たときにdestructorが呼ばれるのでリソースの解放ができる。
* [Wikipedia: RAII (Resource Acquisition Is Initialization)](http://en.wikipedia.org/wiki/Resource_Acquisition_Is_Initialization)




