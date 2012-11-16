---
layout: post
title: "Remote branchを削除する"
description: ""
category: recipes
tags: [git]
---
{% include JB/setup %}

	# localでブランチを削除
	git branch -d <branch name>
	
	# remoteブランチを削除
	git push :<branch name>

	# git 1.7 以上は以下のコマンドでリモートブランチを削除できる
	git push origin --delete <branch name>

### 参考
[How do I delete a Git branch both locally and in Github? - stackoverflow](http://stackoverflow.com/questions/2003505/how-do-i-delete-a-git-branch-both-locally-and-in-github)
	
