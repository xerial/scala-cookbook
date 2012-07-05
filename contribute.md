---
layout: page
title: How to Contribute
tagline: "Scala Cookbookの改善に貢献する"
disqus: true
---
{% include JB/setup %}


## セットアップ
インストールするツール: git, ruby rubygems, rake jekyll, rdiscount

### Macの場合

[Homebrew](http://mxcl.github.com/homebrew/)をインストール後、
	
	$ brew install git

Ruby Gemsでjekyll等をインストール。

	$ sudo gem install rake
    $ sudo gem install jekyll
    $ sudo gem install rdiscount

## GitHubレポジトリのフォークを作成

Scala Cookbookの[GitHubページ](https://github.com/xerial/scala-cookbook)に行き、Forkボタンを押す。

	$ git checkout (フォークしたリポジトリ) -b gh-pages
	$ cd scala-cookbook

## ページの編集

	$ jekyll --server

[http://localhost:4000/](http://localhost:4000) を開いて結果を確認。

## Cookbookレシピの追加

	$ rake post title="(レシピのタイトル)" tag="(レシピの分類)"
`_posts/(レシピの分類)/2012-06-30-(レシピのタイトル).md`というファイルが作成される。

### ページのヘッダー
	---
	layout: page
	title: （タイトル)
	description: (記事の見出し)
	category: recipies   // カテゴリはrecipesに
	tags:[collections]   // レシピを分類する先 collections, setup, debugなど
	---
	{% include JB/setup %}

	(mark downで内容を記述)
	
## Pull Requestを送る

更新をコミットし、クローンしたリポジトリにpushする。
	$ git commit ...
	$ git push (cloned repository)

その後、[Pull requestを開く](https://github.com/xerial/scala-cookbook/pulls)

