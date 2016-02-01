说明
=======
此仓库保存笔者的新闻爬虫项目。

本项目地址 [https://github.com/studychen/java/](https://github.com/studychen/android/)

个人博客，欢迎交流 [http://blog.csdn.net/never_cxb](http://blog.csdn.net/never_cxb)

使用示例：

###获取各个栏目的新闻列表（本科通知、研究生信息、就业招聘等）

链接 http://seenews.applinzi.com/jsp/list.jsp?index=5

![list](http://7xqo2w.com1.z0.glb.clouddn.com/Screen%20Shot%202016-02-01%20at%204.46.51%20PM.png)

###获取某条新闻的详情内容（适配移动端）

链接 http://seenews.applinzi.com/jsp/detail.jsp?num=7909

![detail](http://7xqo2w.com1.z0.glb.clouddn.com/Screen%20Shot%202016-02-01%20at%204.50.40%20PM.png)

思路：APP根据新闻id访问jsp -> jsp爬取学院新闻

优点：

- 省流量（服务端的 jsp 过滤了原网页的无用信息）
- 适配手机端阅读，为了方便分享到朋友圈阅读，
