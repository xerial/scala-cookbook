---
layout: post
title: "Reflectionで型情報を取得"
category: recipes
tags: [object, Scala 2.10]
---
{% include JB/setup %}

Scalaでは[Reflection](http://docs.scala-lang.org/overviews/reflection/overview.html)を使うとgenericsの型情報など詳細な型情報を取得することができます。

## クラス情報を取得 (Scala2.10以前)

Scala2.10以前では、以下のように型情報を取得できます。

	case class Person(id:Int, name:String, age:Option[Int])
	val c = classOf[Person] // Class[Person]を取得
	c.getSimpleName // Person
	c.getName // Personクラスのパッケージ名を含むfull path

しかし、`Person`クラスにどのような型の変数が定義されているを知りたい場合、

1. javaのReflection機能を使う。`Class.getDeclaredFields/Methods`など
1. javapや[ASM](http://asm.ow2.org/)などでコンパイル後のバイトコードを直接参照
1. ScalaSigを使う

など大変でした。また、1や2の方法では`age:Option[Int]`などgenericなクラスの型パラメータ（この場合は`Int`）までは取得できません。なぜなら、コンパイル後のバイトコードでは`Option[Int]`は`Option[java.lang.Object]`と型情報を削られた形で表現されてしまうからです（**type erasure**と呼ばれます）。そのためJava(少なくともJava1.7の時点)では`Option[Int]`の型を正確にプログラム中から調べることは不可能でした。

このtype erasureを克服するため、Scalaではsignatureと呼ばれる情報がコンパイル後のクラスファイルにこっそり埋め込まれています。これにアクセスするのが3の方法ですが、Scalaの型の取り扱いについての深い知識が要求され、すぐに使いこなすのは難しいでしょう。

## TypeTagを使う (Scala2.10の新機能)

Scala2.10ではTypeTagが導入されsignatureへのアクセスが比較的容易になりました。

### 準備

reflectの機能はScalaの本体とは別になっているので、sbtの`libraryDependencies`に以下の設定を追加します。

	"org.scala-lang" % "scala-reflect" % "2.10.0"

### 型情報を取得するコード例

`case class Person(id:Int, name:String, age:Option[Int])`を定義して、パラメータの型情報を取り出します。

	def getType[A : TypeTag](obj:A) : Type = typeOf[A]

のように書くと、コンパイラが`obj:A`の型情報(TypeTag)を生成し、`typeOf[A]`でコード中に型情報を取り出せるようになります。

#### コード
	// この2行でScala2.10のreflectionの機能が使えるようになる
	import scala.reflect.runtime.{universe => ru}
	import ru._

	object ExtractTypeInfo extends Logger {

		// 任意のオブジェクトからTypeTagを取得。取得できない場合はコンパイルエラーになる
		def getType[A : TypeTag](obj:A) : Type = typeOf[A]

        // TypeからClass[_]情報を取得するためのミラー
		val mirror = ru.runtimeMirror(Thread.currentThread.getContextClassLoader)

	    // Type情報を再帰的に解決
		def resolveType[T](tpe:T) : String = tpe match {
		　// TypeRefから型情報を抜き出す
          case tr @ TypeRef(prefix, symbol, typeArgs) => 
			// Typeに対応するClassを取得
            val cl = mirror.runtimeClass(tr)
            var className = 
              if(typeArgs.isEmpty) 
	            cl.getSimpleName
	          else // 型パラメータを持っている場合、各パラメータの型を解決
                s"${cl.getName}[${typeArgs.map(resolveType(_)).mkString(", ")}]"
    
           // コンストラクタで定義されているパラメータを取得
           val cc = tr.declaration(ru.nme.CONSTRUCTOR)
           if(cc.isMethod) {　// コンストラクタの有無をチェック
			  // コンストラクタの最初の括弧内のパラメータ情報を取り出す
              val fstParen = cc.asMethod.paramss.headOption.getOrElse(Seq.empty)
    	  　　val params = for(p <- fstParen) yield { 
    	        val name = p.name.decoded  // パラメータ名を取得
    	        val t = resolveType(p.typeSignature) // パラメータの型を取得し解決
    	        s"$name:${t}"
              } 
              if(!params.isEmpty)
                  className += s"(${params.mkString(", ")})"
           }
           className
        }
    
    	case class Person(id:Int, name:String, age:Option[Int])
	
    	def main(args:Array[String])  {
      　　val p = Person(1, "leo", None)
      　　val tpe = getType(p)
      　　val t = resolveType(tpe)
      　　println(t)
    　　}
	}
	
### 実行結果

	Person(id:int, name:String, age:scala.Option[int])

上記のコードでは、`Option[T]`の型パラメータまで調べることができ、ScalaのInt型などは実際にはJavaのprimitive型のintになっていることがわかります。

## 型の比較

`typeOf[A]`で取得した`Type`は、`=:=`を使って以下のように比較できます。

	val tpe = typeOf[Int]
	
	tpe match {
      case t if t =:= typeOf[Short] =>  "is short type"
      case t if t =:= typeOf[Boolean] => "is boolean type"
      case t if t =:= typeOf[Byte] => ...
      case t if t =:= typeOf[Char] => 
      case t if t =:= typeOf[Int] => 
      case t if t =:= typeOf[Float] => 
      case t if t =:= typeOf[Long] => 
      case t if t =:= typeOf[Double] => 
	  case t if t =:= typeOf[String] =>
    }

## Class[A]からsignatureを取得

コンパイル時にTypeTagが得られない場合（例えばクラス名だけからオブジェクトを動的に作成する場合など）、Class[A]の情報からmirrorを経由してsignatureを取り出すこともできます。

### コード例
	
	val cl = classOf[Person] 
	val mirror = ru.runtimeMirror(Thread.currentThread.getContextClassLoader)
	// クラス名からClassSymbol (Type)情報を取り出す
    val classSymbol : ru.ClassSymbol = mirror.staticClass(cl.getCanonicalName)
	// コンストラクタを調べる
    val cc = classSymbol.typeSignature.declaration(ru.nme.CONSTRUCTOR)
    val params = if(cc.isMethod) {
      val fstParen = cc.asMethod.paramss.headOption.getOrElse(Seq.empty)
      for(p <- fstParen) yield {
        val name = p.name.decoded
        val tpe = resolveType(p.typeSignature) // 上記のコードを呼び出す
		s"$name:$tpe"
      }
    } 
	else Seq.empty
	println(params.mkString(", ")) // 	id:int, name:String, age:scala.Option[int]


## xerial-lens：型情報を取得するライブラリ

Scala2.10のreflectionの機能は強力ですが、上記のように再帰的な処理が必要となるなどやや不便なところがあります。これを解決するために`xerial-lens`というライブラリを作成しました。

sbtの`libraryDependencies`に、

	"org.xerial" % "xerial-lens" % "3.1" 

を追加すると、型情報を取り出す[ObjectType](https://github.com/xerial/xerial/blob/develop/xerial-lens/src/main/scala/xerial/lens/ObjectType.scala)が使えるようになります。

内部ではTypeTagだけではなくScalaSigにアクセスして詳細な型情報を取り出すなどの工夫がされています。

### 使用例

	import xerial.lens.{ObjectType,StandardType,Primitive, MapType}

	val ot = ObjectType(classOf[Person])
	println(ot) // Person
	// パターンマッチで型による場合分けが可能
    val params = ot match { 
	  case s:StandardType(cl) => s.constructorParams.mkString(", ")
      case p:Primitive.Int => "int type"
	  case m:MapType(cl, keyType, valueType) => s"Map[$keyType, $valueType]"
	  case _ => "no params"
	}
	println(params) // id:Int, name:String, age:Option[Int]

xerial-lensには、その他にもメソッドやアノテーションの情報を取り出す[ObjectSchema](https://oss.sonatype.org/service/local/repositories/releases/archive/org/xerial/xerial-lens/3.1/xerial-lens-3.1-javadoc.jar/!/index.html#xerial.lens.ObjectSchema)や、それを利用してコマンドラインプログラムの作成を簡単にする[Launcher](https://oss.sonatype.org/service/local/repositories/releases/archive/org/xerial/xerial-lens/3.1/xerial-lens-3.1-javadoc.jar/!/index.html#xerial.lens.cui.Launcher)などが含まれています。

* ソースコードはこちら [xerial at github](https://github.com/xerial/xerial)


## 関連

* [Scala本家によるReflection機能の紹介](http://docs.scala-lang.org/overviews/reflection/overview.html)

