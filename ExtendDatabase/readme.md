## 说明 ##


一个小工具，基于 `jconn4.jar`，实现扩展 ASE 数据库的 data 空间和 log 空间。

> 代码中 hadrcode 了扩展空间大小，默认为增加 40G 的 data 和 10G 的 log。

本仓库地址 [https://github.com/studychen/java/](https://github.com/studychen/android/)

个人博客，欢迎交流 [http://blog.csdn.net/never_cxb](http://blog.csdn.net/never_cxb)


本工程需要 Sybase JDBC 驱动，这儿有 CSDN 的一个下载链接 http://download.csdn.net/detail/chawsh/7308555）。

安装包里面也可能附带了这个驱动。


笔者在项目中使用该工程的实践如下：
		
- 将本Java工程导出为`Runnable JAR file`
- 使用命令行方法快捷扩展数据库 `java -jar extendb.jar <ip> <asePort> <sid>`
  