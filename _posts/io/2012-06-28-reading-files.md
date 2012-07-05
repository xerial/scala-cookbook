---
layout: post
title: "ファイルを読む"
description: "バッファを経由、一行ずつ、あるいはIteratorを使う"
category: recipes
tags: [I/O]
---
{% include JB/setup %}

## バッファを経由して読む

[Loan pattern]({{BASE_PATH}}/recipes/2012/06/27/loan-pattern)を使う。ファイルの入出力の機能はJavaのライブラリから借りてくる。

	def open(fileName:String)(body:InputStream => Unit) : Unit = {
		// ディスクへの細かなアクセスを避けるため、バッファを介してファイルを読む
		val in = new BufferedInputStream(new FileInputStream(fileName))
		try
			body(in)
		finally 
			in.close  // 開けたら閉じる
	}
	
	open("myfile.txt") { f =>
		val buf = new Array[Byte](8192)  // 8kのバッファを用意
		def loop {
	       val readBytes = f.read(buf)  // bufferにデータを読み込む
		   if(readBytes != -1) {  // -1が返るとこれ以上データはない
		      // use read data here
			  loop  
	       }
		}
		loop
	}
	

## 一行ずつ読み

```BufferedReader```を使うと良い。

	def open(fileName:String)(body:BufferedReader => Unit) : Unit = {
		// ディスクへの細かなアクセスを避けるため、バッファを介してファイルを読む
		val in = new BufferedReader(new FileReader(fileName))
		try
			body(in)
		finally 
			in.close  // 開けたら閉じる
	}

	open("myfile.txt") { f =>
		def loop {
	       val line = f.readLine  // 一行ずつ読む
		   if(line != null) {  // nullが返ると読み込み終了
		      // use read data here
			  loop  
	       }
		}
		loop
	}


## よりScalaらしい書き方

上記のコードでは*null*の扱いに気をつける必要があり、安全ではない。*null*の使用をクラスの内部に閉じ込め、使う側は*null*を気にせず一行ずつ操作できる```Iterator[String]```を提供する形に書き換える。

	// 一行ずつ読み込むiteratorを定義
	class LineItertor(in:BufferedReader) extends Iterator[String] {
		private var nextLine : String = null
		def hasNext = {
			if(nextLine == null)
				nextLine = in.readLine
			nextLine != null
		}
		def next : String = {
			if(hasNext) {
				val line = nextLine
				nextLine = null
				line
			}
			else
				Iterator.empty.next
		}
	}

	def open(fileName:String)(body:Iterator[String] => Unit) : Unit = {
		val in = new BufferedReader(new FileReader(fileName))
		try
			body(new LineIterator(in)) // Iteratorを返す
		finally 
			in.close  // 開けたら閉じる
	}

	open("myfile.txt") { f =>
		for(line <- f) { // Iterator#foreachが使える
			// 一行ずつ処理する
		}
	}

Itertorの使用を途中で止めても、loan patternに閉じ込めて実行しているのでファイルがきちんと閉じられる。
