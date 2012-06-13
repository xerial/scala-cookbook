---
layout: page
title: Computer Skills
tagline: 研究を進める上でぜひ身につけてほしい計算機のスキル
---
{% include JB/setup %}


### プログラミング
* 文法、コンパイル方法などに不自由なく使える言語を最低一つ身につける
  * C++, Java（今ならScalaでも良い)など、コードの動作が速い、静的型付け言語から最低１つ
  * Perl, Ruby, Python, シェルスクリプトの書き方も知っていて損はない
* 開発環境
  * C++ : Visual Studio (Windows), X Code (Mac)など
  * Java/Scala: Eclipse, IntelliJ (Win/Mac/Linux)
  * 使い方に慣れるまで時間がかかるので、普段から使えるようにしておく
* テストコードの作成
  * テスト作成ー動作の確認−コーディング、というスタイルで開発サイクルの短縮
  * Unit testing, TDD, BDD, extreme programmingなど
* タイプ速度 （上げておく方が何かと良い）
* コードライブラリの作成、リンク
  * gccの-l -Lオプションなど
  * Java/Scalaでは、jarファイルの作成、他人の作ったライブラリの使い方
     * maven, ivy, sbtなど

### 論文執筆
* 英語の読み書き
* グラフの書き方
  * 必ず軸のラベル、単位を添える
  * 読みやすい大きなフォントで (Arial系が好まれる)
* Word
  * スタイルの設定
* TeX 
  * Bioinformatics系ではTeXを使えると数式が書きやすい
  * 数式の書き方
  * PDF画像の埋め込み
  * Windows: MikTeXが便利
  * omake + pdflatexでPDFを直接生成
      * Acrobatより、Skim(Mac), Sumatra PDF(Windows) がファイルロックされないので便利
  * bibtexの書き方
* 参考文献管理
  * bibtex, Papers2 (mekentosj), EndNoteなど

### ツール
* バージョン管理システムを使えるようにする
  * Git, Mercurial(hg) のどちらか一方は必ず
     * Windowなら TortoiseGit, TortoiseHGなどのツールを入れると良い
  * Subversion
  * GitHub, BitBucketなど公開用リポジトリの使い方
* 画像の加工
  * Illustrator (ポスター作成に重宝)
  * PhotoShop (ロゴ、アイコン作成など)
  * IrfanView (Windows)、Preview (Mac)で、画像の縮小、切り貼り
* グラフ、チャートを描くツール
  * GNU plot
  * R （Rは統計処理などにも使える）
  * Excel -> PDFの作成
  * Java(Scala)なら、JFreeChart, iTextでPDFのグラフ作成。フォントの埋め込み
* データベースの作成
  * PostgreSQL, MySQLなどRDBMSがどう動いているかの知識
  * sqlite3で、データベースを作成、SQLで検索

### UNIX環境に慣れる
* SSHの設定
 * 公開鍵認証, 多段SSH, port forwarding
   * windowsなら、cygwin, puttyなど
 * ssh-agent
   * keychain,  pagent (puttyの場合)
* (Windows) cygwin
   * setup.exeでインストール
   * mintty
* データの圧縮、伸張
   * tar  (tvfz, cvfz, cvfj などオプションの組み合わせの意味がわかるように)
   * gzip
* データの転送
   * scp
   * rsync (-avn, -avオプションの意味)
* シェルの設定
   * bash: .bashrc, .bash_profile
   * zsh: .zshrc, .zprofile, .zlogin
   * exportによる環境変数の設定
   * sourceコマンド
* シェルの活用
   * キーバインド(Ctrl+a, e, r, s, b, fなど)
　 * コマンド入力履歴の表示
   * パイプ | 
   * リダイレクト >, >>, 2>&1 などの意味
   * バックグラウンドjobの実行、nohup
   * ジョブのsuspend (ctrl+z), ジョブの再開方法(%...), バックグラウンドに移行(bg)
   * screenを使い、ログアウト後もジョブを実行し続けられるようにする
* プロセスの確認
   * top (1, q, uなどのキーバインド)
   * ps (aux, auwxなどのオプション)
   * qstat (グリッドエンジン用)
   * watch (watch -d ls -l などでファイルの更新状況を確認)
* テキストの編集
   * emacs, vi のどちらかは使えるように
* ファイルの設定
   * chmod (user, group, others などの意味, sticky bitなど)
   * chown 
* ファイルの検索
   * find (find . -name "*.cpp", find . -type dなど)
   * xargs
* テキスト処理
   * grep (n, i, w, A, B, Cなどのオプション)
   * sed
   * awk 
   * cut (-f, -d) 
   * paste
   * sort (-k1,1 -k2,2n  など並べ方の指定オプションに慣れる)
   * tail　
   * head
   * more, less
   * md5sum
* リモートのデスクトップに接続
   * startx (GNOME, KDEなど)で立ち上げる　(ssh -Xでログインが必要)
   * VNCで接続
* サーバー管理用
   * sudo 
   * /etc 以下のファイル群
   * rpmを使ったパッケージ管理
   * su ユーザーの切り替え
* その他
   * cron
* Makefile
   * ./configure prefix= ..  ; make; make install
   * 解析のパイプラインの記述ができるように
   * make -n でdry run
   * GXPを使った分散Make
* グリッドエンジン
   * ジョブの投入の仕方 (qsub), ジョブ実行スクリプトの書き方
   * ジョブの削除 (qdel)
* Webページの作成 
  * HTML, CSS
  * Apacheの設定 (.htaccess, mod_rewrite)
  * Apacheのログの見方 (/var/log/httpd/... )
  * Tomcat + Java(Scala)ならサーブレットの書きかた
    (あるいはUTGBを使う)
  
### ゲノム情報処理関連
* 既存のデータベースの存在について知る
  * NCBI, Ensembl, UCSC, ENCODE/modENCODE(NHGRI)にあるもの
  * SGD, FlyBase, WormBaseなどモデル生物ごとのデータベース
* アラインメント
  * BLASTのコマンドラインオプション、出力フォーマットの意味
  * bwa, bowtie2, BLASRなどFM-index系のアライナー
  * lastzなど比較ゲノム用
* データフォーマット
  * BED, WIG, GFF, FASTA, FASTQ, SAM/BAM
  * SAM/BAMファイルの読み込み (picard, samtools)
  * BioMartでアクセスできるもの
* SNP call
  * samtools mpileup, GATK, などなど
* アセンブリ
  * ABySS, ALLPATH-LGなどde Bruijinグラフ系

