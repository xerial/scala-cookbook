---
layout: page
title: Scala Cookbook
tagline: Tutorial of the Scala Programming Language
---
{% include JB/setup %}

C++/Javaなどは授業で習う機会が多いようですが、Scalaなど比較的新しい言語は学ぶ機会に乏しいのが現状です。
この文章は主に研究室に入ってきた学生さんのスキルアップのために作成しています。そして、願わくば一人で多数の計算機を使いこなし、100人分の仕事ができるようになってくれれば幸いです。

文章の修正、追加等はgithubの[pull request](https://help.github.com/articles/using-pull-requests)でお願いします。Scala Cookbookのソースコードは[こちら](https://github.com/xerial/scala-cookbook/tree/gh-pages).

### Contents

* [**Lesson 0**: Scala Quick Start](quickstart.html)
   * サンプルコードをgitで取得
   * Scalaコードの実行
   * IntelliJのインストール
   * ScalaTestによるテストコードの作成とテストの実行
   * テストを実行しながら開発(sbt)
   
* [**Lesson 1**: Scala Collections](lessons/lesson1.html)
   * Array, Map, Set,  Tuple
   * Option
   * 要素を連結した文字列の作成(mkString)
   * immutable（変更できない）コレクションを使う
   * Builderでコレクションを作成する
   * コレクション内を探索する (map, foreach, flatMap, reduce, fold)
   * 一部の要素を取り出す (filter, collect)
   * 要素を並び替える (sort)
   * マルチコアでコレクションを並列処理する (parallel collection, par)

* **Lesson** sbt
   * Scalaでコードを書き始めるための最小プロジェクト

* **Lesson** : 制御構造
   * 特定の要素を見つける (find, exists)
   * List
   * pattern matching
   * while
   * for comprehension
   * recursion
* **Lesson** : クラスとオブジェクト
   * パラメーターの定義
   * alternative constructor
   * 変数のスコープ
   * 初期化コード
　 * lazy parameter
   * case class

   
* **Lesson** - Trait
   * インターフェースの定義
   * 実装の共有
   * 多重継承時のlinearization


* **Lesson** : デバッグ手法
   * ScalaTestを用いて、コードの挙動を確認する
   * Loggerを用いたデバッグ
   * timerで、コードの実行時間を計測する
   * Traitでfixtureを作成し、テストコードの共通設定を使い回す

* **Lesson** : 入出力 
   * ファイルの読み書き
   * 文字列の操作
   * 正規表現   
   * 外部プロセスの実行
* **Lesson** - 字句解析、構文解析
   * Combinator parsers
   * コマンドライン引数をparseする
   
* **Lesson** ゲノム情報処理
   * FASTAファイルからゲノム配列情報を取り出す
   * BEDファイルから遺伝子データの読み込み
   * WIGファイルでエピゲノム情報の管理
   * SAM/BAMファイルの読み書き
   * DPによるアラインメント (SmithWaterman(-Gotoh), Banded-alignment, Myer)
   * suffix arrayの構築 (induced sorting, parallel dc3)
   
* **Lesson** アルゴリズムとデータ構造
   * グラフ(連結リスト)とその探索
   * Union Findで同値類をまとめる
   * 区間の交差判定

* **Lesson** グラフの作成
   * jfreechart + iTextで、PDFチャートを作成
   * ヒストグラム
   * line chart
   * box chart
   * Rscriptを呼び出し、グラフの作成

* **Lesson** グラフィックス
   * 画像の作成

* **Lesson** データベース
   * データベースの検索
   * データベースの更新
   * テキスト検索

* **Lesson** 分散処理
   * Akkaのremote actorを使った分散処理
   * objectのserialization/deserialization

* [Scalaの文法](cheetsheet.html)

* IntelliJのカスタマイズ
   * 使用可能メモリ量を増やす



