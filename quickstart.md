---
layout: default
title: Scala Quick Start
tagline: 
---
{% include JB/setup %}

## Lesson 0: Scala Quick Start


    $ git clone git://github.com/xerial/scala-cookbook.git 
	Cloning into 'scala-cookbook'...
	remote: Counting objects: 181, done.
	remote: Compressing objects: 100% (149/149), done.
	remote: Total 181 (delta 18), reused 132 (delta 15)
	Receiving objects: 100% (181/181), 628.64 KiB | 224 KiB/s, done.
	Resolving deltas: 100% (18/18), done.
	$ cd scala-cookbook 
	$ git checkout lesson0
	Branch lesson0 set up to track remote branch lesson0 from origin.
	Switched to a new branch 'lesson0'
    $ make install
	bin/sbt  package-dist
	Downloading sbt launcher 0.11.3:
	From  http://typesafe.artifactoryonline.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.11.3/sbt-launch.jar
    To  bin/.lib/0.11.3/sbt-launch.jar
    ...

    [info] Done updating.
    [info] Compiling 1 Scala source to /glusterfs/leo/work/tmp/scala-cookbook/target/classes...
    [info] Packaging /glusterfs/leo/work/tmp/scala-cookbook/target/scala-cookbook-0.1-SNAPSHOT.jar ...
    [info] Done packaging.
    [info] output dir: /glusterfs/leo/work/tmp/scala-cookbook/target/dist
    [info] Copy libraries
    [info] Create bin folder
    [info] Generating version info
    [info] done.
    [success] Total time: 33 s, completed Jun 11, 2012 11:34:48 PM
    if [ -d "/home/leo/local/scala-cookbook/scala-cookbook-0.1-SNAPSHOT" ]; then rm -rf "/home/leo/local/scala-cookbook/scala-cookbook-0.1-SNAPSHOT"; fi
    install -d "/home/leo/local/scala-cookbook/scala-cookbook-0.1-SNAPSHOT"
    chmod 755 target/dist/bin/scala-cookbook
    cp -r target/dist/* /home/leo/local/scala-cookbook/scala-cookbook-0.1-SNAPSHOT
    ln -sfn "scala-cookbook-0.1-SNAPSHOT" "/home/leo/local/scala-cookbook/current"
    install -d "/home/leo/local/bin"
    ln -sf "../scala-cookbook/current/bin/scala-cookbook" "/home/leo/local/bin/scala-cookbook"
	$ ~/local/bin/scala-cookbook
    Hello Scala Cookbook!



