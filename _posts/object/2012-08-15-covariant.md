---
layout: post
title: "共変 covariant な型を使う"
description: "型間の代入を柔軟に"
category: recipes
tags: [object]
---
{% include JB/setup %}

Scalaのコレクションクラス（List, Seqなど）は、`List[+A]`と型名の前に+を付けてcovariant(共変)な型を許すように定義されています。covariantとは、Aのクラスを拡張したクラスBがあれば、List[B]はList[A]として代入できることを意味します。例えば以下のように、クラス階層を定義したときに、List[Cat]をList[Animal]として代入できます。

     abstract class Animal 
	 case class Cat(name:String) extends Animal
	 case class Dog(name:String) extends Animal

     val c : List[Cat] = List(Cat("A"), Cat("B"))
     // ListはList[+A]として定義されている
	 val a : List[Animal] = c  	 // OK.
	 // List[+A]はcovariantなので、List[B <: A]（Aクラスから派生したクラスのList)を代入できる
	 
また、List[Cat]とList[Dog]を足し合わせて、List[Animal]を作ることもできます。	 
	 
	 val l : List[Animal] = Dog("D") :: c // OK. List(Dog(D), Cat(A), Cat(B)) 

## Optionクラスでのcovariantの利用

Optionも、covariantを使ってOption[+A]として定義されており、NoneはOption[Nothing]から拡張して定義されています。

    object None extends Option[Nothing] {
      def map[B](f: Nothing => B) : Option[B] = None
      def flatMap[B](f: Nothing => Monad[B]) : Option[B] = None
    }

Nothingはあらゆる型の親になれるようにScalaで定義されているので、None、すなわちOption[Nothing]は、Option[A]やOption[B]などに代入できます。この工夫により、None[A], None[B]などと宣言する必要はなく、Noneとだけコード中に書けば良くなっています。

