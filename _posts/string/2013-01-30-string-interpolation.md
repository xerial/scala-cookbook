---
layout: post
title: "文字列に式を埋め込んで整形する"
description: "String interpolation"
category: recipes
tags: [string, scala2.10]
---
{% include JB/setup %}

Scala2.10より、[String Interpolation](http://docs.scala-lang.org/overviews/core/string-interpolation.html)の機能が追加され、文字列中に式を埋め込むのが容易になりました。

## 使い方

### s String

double quotationの前に`s`を付けると、文字列中にある`$(変数名)`が置き換わる。

	val w = "World"
	val message = s"Hello $w!"
	println(message)  // Hello World! と表示される

`{}`で囲むと、任意の式を含めることもできる。

	println(s"2 * 3 = ${2 * 3}")  // 2 * 3 = 6 と表示される
	
	
### f String
	
文字列に`f`をつけると[printf](http://docs.oracle.com/javase/1.6.0/docs/api/java/util/Formatter.html#detail)の構文が使えるようになる。
	
	val dollarToYenRate = 80.0
	val budget_d = 10000000
	println(f"Currency conversion: $budget_d%d to ${budget_d * dollarToYenRate}%,.2f yen")
	
f Stringでは、フォーマット指定を省くと文字列(`%s`と同じ）と扱われる。

フォーマット指定と実際の変数の型が異なると、コンパイル時にエラーがでる(type safeになる)ので、printf実行中のエラーを防げるようになった。
	
## formatによく使うシンボル

|               |       
| ------        | ------          
|  `%s`         |  文字列  
|  `%d`         |  整数  
|  `%f`         |  浮動小数点数  
|  `%e`         |  科学計算用の指数を含んだ数字。e-10などが付く 


### flagの例
`%`と上記のシンボルの間にflagを複数個挟むことができる。


|               |       
| ------        | ------          
|`,`   |３桁ごとにcommaを挟んで表示 
|`(数字)`  |数字で指定された分のスペースを使って表示。`-`を付けると右寄せ
|`.2`    | 小数点以下二桁を表示 
	
