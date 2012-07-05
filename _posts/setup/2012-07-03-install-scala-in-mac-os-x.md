---
layout: post
title: "ScalaをMac OS Xにインストールする"
description: "Homebrewで高速に開発環境をセットアップ"
category: recipes
tags: [setup]
---
{% include JB/setup %}

Mac OS Xユーザーの場合、[Homebrew](http://mxcl.github.com/homebrew/) をインストールしておくと、高速にScalaの開発環境を整えることができる。

	
	$ brew install scala
	$ brew install sbt
	
また、git、mercurialなども手軽にインストールできる。

	$ brew install git
	$ brew install mercurial
	
	
ちなみにGNU MakeなどのコマンドラインツールはApp StoreでXCodeをインストール後、`Preferences`->`Downloads`->`Command Line Tools` を選択してインストールする。
	
	
