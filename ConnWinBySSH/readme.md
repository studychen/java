## 说明 ##

本工程基于 JSCH（官网链接 [http://www.jcraft.com/jsch](http://www.jcraft.com/jsch)  ），实现连接远程 Windows 机器并在该 Windows 机器上执行一些命令。

项目地址 https://github.com/studychen/java/

个人博客，欢迎交流 http://blog.csdn.net/never_cxb

笔者在项目中使用该工程的实践如下：
		
- 将本Java工程导出为`Runnable JAR file`，放于一台 Linux 机器上。
- 把 Windows 上的操作（cmd、vb 脚本等）全都封装进一个 python 文件里。
- 然后在 Linux 上通过刚导出的 JAR 文件调用Windows 的 Python 脚本。


Jar 包下载地址 http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.jcraft%22%20AND%20a%3A%22jsch%22

或者使用 Maven Gradle 等包管理器
```
<dependency>
	<groupId>com.jcraft</groupId>
	<artifactId>jsch</artifactId>
	<version>0.1.53</version>
</dependency>
```

```
@Grapes(
	@Grab(group='com.jcraft', module='jsch', version='0.1.53')
)
```

笔者项目组使用的Windows 版本是windows server 2008，Linux 版本是 openSUSE 11。

本工程适当修改也可以连接开启了 SSH 服务的 Linux 机器。

>Note
>请保证远程机器上已经开启 SSH 服务，Windows 可以尝试freeSSHD或者openSSH。
>
>如果想通过 SSH 调用图形化程序，可以尝试`ssh -X` 或是 `ssh -Y`。

## JSCH 一些示例代码 ##

- [JSch - Examples](http://www.jcraft.com/jsch/examples/)
- http://www.jcraft.com/jsch/examples/JTAJSch.java
- [Jsch or SSHJ or Ganymed SSH-2?](http://stackoverflow.com/questions/5097514/jsch-or-sshj-or-ganymed-ssh-2)
- [any good jsch examples?](http://stackoverflow.com/questions/2405885/any-good-jsch-examples)
- http://www.cs.ucsb.edu/~cappello/290b-2009-Spring/projects/reports/diana/290project/src/ssh/MyUserInfo.java
