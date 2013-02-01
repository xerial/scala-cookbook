---
layout: page
title: Scala Cookbook ToDo
tagline: 今後充実させる内容
disquss: true
---
{% include JB/setup %}

このページのソース <https://github.com/xerial/scala-cookbook/blob/gh-pages/todo.md>

## Scalaのデザインパターン（イディオム）
 * Option monadなどは記述済み
 * (他にはどんなパターンが？）
 * 関数型パラダイム (http://en.wikipedia.org/wiki/Functional_programming)
  * 純粋関数
  * 再帰
  * 非正格評価（「遅延評価」言うな？）
  * 代数的データ型 (algebraic data types)
  * （モで始まるアレ）
  * 他にもいろいろありそう
 * 型クラス (type class)
 * 高カインド型を扱うときの諸々
  * { type l[A] = SomeType[T, A] }#l
  * http://eed3si9n.com/ja/essence-of-iterator-pattern
 * Cake Pattern
 * Loan Pattern
 * DynamicVariable を使うやつ
 * Phantom Type
 * Magnet Pattern
 * （ミックスインや型方面のパターンがごっそり抜けてそう。補完求む）

## 機能
 * Future
 * akka
 * parallel collection
 * などなど
 * 型推論
 * 糖衣構文（演算子と中置記法、apply）
 * タプル
 * コレクションライブラリ（不変vs可変、各種操作）
 * アクセサメソッド（統一形式アクセスの原則）
 * case クラス
 * パターンマッチ
 * mix-in
 * 第一級関数、関数リテラル、高階関数
 * クロージャ（自由変数、束縛変数）
 * 名前付き引数、デフォルト引数
 * 部分関数
 * lazy val
 * 部分適用、カリー化
 * 末尾再帰最適化
 * 名前渡し
 * 変位（共変、反変、非変）
 * 型境界 (Upper, Lower)
 * 自分型アノテーション
 * 構造的部分型
 * for内包表記
 * 抽出子
 * アクター
 * パーサーコンビネータ
 * 正規表現
 * XML リテラル
 * generalized type constraints

## リンク集
 * [Scala Cookbook](http://xerial.org/scala-cookbook)
 * [Functional Programming Principles in Scala - Martin Odersky](https://www.coursera.org/course/progfun) 
    * Scalaの作者 Martin OderskyによるScalaのビデオ講義（英語）。充実しています。
