---
layout: post
title: "Type Class"
description: "アルゴリズムとデータの鎹（かすがい）"
category: recipes
tags: [design pattern]
---
{% include JB/setup %}

**Type Class**(型クラス) とは型の性質を表現するためのクラスで、Haskellなどの関数型言語では古くから使われています。型クラスは、アルゴリズムとアルゴリズム中で使うデータ型の結合を緩やかにする、あるいは、アルゴリズム中で使うデータと実際のデータ型の型合わせをするために使われます。

## 例題

例えば、区間データ(start, endのフィールドを持つ)を保持するための`IntervalHolder`を考えてみます。区間を保持するという意味では汎用的に書けそうなので、区間を`A`と置いてGenericなクラスとして表現します。

    class IntervalHolder[A] {
      private var holder = Map[Int, A]()
      def +=(a:A) {
        holder += a.start -> e  // コンパイルエラー。Aはstartを持つ型ではない
      }
    }

Aにはstartというパラメータは定義されていないので、Aに制約を加える必要があります。

## 型クラスを使わない場合（traitを使用）

IntervalHolderを任意のAではなく、区間を表すIntervalData traitを継承した型のみを受け付けるようにしてみます。

    trait IntervalData {
      def start: Int
      def end: Int
    }

    class IntervalHolder[A <: IntervalData] {
      private var holder = Map[Int, A]()
      def +=(e:A) {
　　    holder += e.start -> e  // コンパイルできるようになった
      }
    }

しかし実際には、区間データの表現にも以下のように様々な種類が考えられます。

    case class Interval(start:Int, end:Int)
    case class SelectedRange(name:String, left:Int, right:Int)

これらは必ずしもstartというパラメータを持つわけではないが、区間として「みなせる」ようなデータ構造です。IntervalHolderのコードを再利用したい場合、これらの区間データのクラスをすべてIntervalData traitを継承するように書き直す必要があります。しかし、第三者の作成したライブラリ中のクラスなど、自分で書き直すのが難しい場合にはこの方法は使えません。

## 型クラスを使って型を合わせる

ここで登場するのが型クラスです。型クラスは任意のオブジェクト`A`から必要なデータ(ここではstartとend)を取り出せるように表現します。区間の性質を表す型クラス IntervalTypeを定義します。

    trait IntervalType[A] {
      def start(a:A) : Int
      def end(a:A) : Int
    }

IntervallHolderを型クラスを使って書き直します。
	
    class IntervalHolder[A](implicit iv:IntervalType[A]) {
      private var holder = Map[Int, A]()
      def +=(e:A) {
        holder += iv.start(e) -> e  // 型クラス経由でパラメータにアクセスする
      }
    }

次に、implicit parameter `iv`をコンパイラに自動的に見つけさせるため、Interval, SelectedRangeのそれぞれについて、型クラスIntervalTypeの実装をIntervalHolderのコンパニオンオブジェクト内に作成します（コンパイラが見つけられるスコープ中にあれば他の場所に定義しても構いません）

    object IntervalHolder {
      // Intervalは、IntervalTypeとして扱えるという意味
      object StandardInterval extends IntervalType[Interval] {
        def start(a:Interval) = a.start
        def end(a:Interval) = a.end
      }

      // SelectedRangeもIntervalTypeとして扱えるという意味
      object SelectedRangeAsInterval extends IntervalType[SelectedRange] {
        def start(a:SelctedRange) = a.left
        def end(a:SelectedRange) = a.right
      }
    }
	
型クラスのインスタンスは１つあれば十分なのでobjectとして定義してあります。またimplicit paramterとして自動解決する場合にもobjectとしてあると都合がよいです。
	
### 使用例

    val holder = new IntervalHolder[Interval] 
    holder += Interval(1, 3)
	
    val rangeHolder = new IntervalHolder[SelectedRange]
    rangeHolder += SeletedRange("user input", 140, 180)

IntervalHolderの実装を２種類のデータ型に対して再利用することができました。今後区間を表すデータ型の種類が増えたときも、型クラスの実装を追加するだけでIntervalHolderを使えるようになります。

implicit parameterに代入される型クラスは、コンパニオンオブジェクト内に定義されているか（IntervalHolder, Interval, SelectedRangeのコンパニオンオブジェクトなどが検索対象に入る）、import文などでスコープに読み込んであれば、Aの型に合わせて対応するIntervalTypeの実装をコンパイラが見つけてきてくれます。

## 型クラスの応用例

Scalaに含まれている[Ordering](http://www.scala-lang.org/api/current/index.html#scala.math.Ordering)なども型クラスの一例です。`def lt(x:T, y:T): Boolean`など汎用的なT型の大小を比較するための関数が定義されている型クラスで、これを使うことによりsorting, min, maxなどを求めるコードを種々のデータに対して再利用しています。



