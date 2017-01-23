---
layout: post
title: "文字列のformat"
description: "変数を埋め込んで文字列を作成"
category: recipes
tags: [string]
---
{% include JB/setup %}

文字列を整形して出力したい。

**Scala2.10より、[String interpolation]({{BASE_PATH}}/recipes/2013/01/30/string-interpolation)が使えるようになりtype safeな文字列の整形ができるようになりました。こちらがおすすめ　(2013年1月)**


## 
String.formatを使う。

	"Hello %s!".format("World!") //  Hello World!
	"Elapsed time: %.2f".format(43.5345) // 43.54
	"Read %,d entries".format(100000000000L) // Read 100,000,000,000 entries
	
	try {
	  ...
	}
	catch {
	  case e:Exception => 
	    System.err.println("[%s] Error: %s".format(this.getClass.getName, e.getMesssages))
	}

デバッグ用の文字列を出力するのに重宝。type safeでない（`%f`に文字列の値を渡してしまうと、`IllegalFormatException`が発生してしまう）のだが、他によい代替品が現れるまでは使う機会も多いだろう。

## formatによく使うシンボル

|               |       
| ------        | ------          
|  `%s`         |  文字列  
|  `%d`         |  整数  
|  `%f`         |  浮動小数点数  
|  `%e`         |  科学計算用の指数を含んだ数字。e-10などが付く 


### flagの例
%とこれらのシンボルの間にflag(s)を挟むことができる。

|               |       
| ------        | ------          
|`,`   |３桁ごとにcommaを挟んで表示 
|`(数字)`  |数字で指定された分のスペースを使って表示
|`.2`    | 小数点以下二桁を表示 



## 関連
　* [java.util.Formatter](http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html)　より詳細な仕様はこちら
