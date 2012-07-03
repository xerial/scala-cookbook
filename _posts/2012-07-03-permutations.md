---
layout: post
title: "順列、組み合わせ、冪集合を生成する"
description: 
category: recipes
tags: [collections]
---
{% include JB/setup %}

Scalaでは集合を並び替えた順列(permutation)や、組み合わせ（combination, 重複を含まない）、冪集合(power set)などを手軽に生成できる。

## 順列の生成

    scala> val l = List("A", "B", "C", "D")
    l: List[java.lang.String] = List(A, B, C, D)
    
    scala> l.permutations
    res0: Iterator[List[java.lang.String]] = non-empty iterator
    
    scala> res0.map(_.mkString(",")).mkString("\n")
    res1: String =
    A,B,C,D
    A,B,D,C
    A,C,B,D
    A,C,D,B
    A,D,B,C
    A,D,C,B
    B,A,C,D
    B,A,D,C
    B,C,A,D
    B,C,D,A
    B,D,A,C
    B,D,C,A
    C,A,B,D
    C,A,D,B
    C,B,A,D
    C,B,D,A
    C,D,A,B
    C,D,B,A
    D,A,B,C
    D,A,C,B
    D,B,A,C
    D,B,C,A
    D,C,A,B
    D,C,B,A

## 組み合わせの生成

    scala> l.combinations(3)
    res2: Iterator[List[java.lang.String]] = non-empty iterator
    
    scala> res2.map(_.mkString(",")).mkString("\n")
    res3: String = 
    A,B,C
    A,B,D
    A,C,D
    B,C,D

## 冪集合の生成

冪集合(power set)を生成する関数はないが、冪集合を生成する関数は以下のように簡単に書ける。

    scala> def powerSet[A](s:TraversableOnce[A]) = 
         |     s.foldLeft(Set(Set.empty[A])) {
         |        (set, element) => set union (set map (_ + element))
         |     }
    powerSet: [A](s: TraversableOnce[A])scala.collection.immutable.Set[scala.collection.immutable.Set[A]]
    
    scala> powerSet(l.toSet)
    res5: scala.collection.immutable.Set[scala.collection.immutable.Set[java.lang.String]] = Set(Set(A, D), Set(), Set(A, B), Set(B, C), Set(B), Set(A, B, C), Set(C), Set(A, B, C, D), Set(C, D), Set(A, C), Set(B, C, D), Set(A, C, D), Set(B, D), Set(A), Set(D), Set(A, B, D))

* 参考：[Scala Expressiveness](http://thinkmeta.wordpress.com/2010/06/28/scala-expressiveness/)

