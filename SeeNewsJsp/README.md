说明
=======
此仓库保存笔者的新闻爬虫项目。

本项目地址 [https://github.com/studychen/java/](https://github.com/studychen/android/)

个人博客，欢迎交流 [http://blog.csdn.net/never_cxb](http://blog.csdn.net/never_cxb)

##使用示例：

###获取各个栏目的新闻列表（本科通知、研究生信息、就业招聘等）

链接 http://seenews.applinzi.com/jsp/list.jsp?index=5

<img src="http://7xqo2w.com1.z0.glb.clouddn.com/newslist.png" width="50%" height="80%" alt="blog.csdn.net/never_cxb" align="center">

###获取某条新闻的详情内容（适配移动端）

链接 http://seenews.applinzi.com/jsp/detail.jsp?num=7909

<img src="http://7xqo2w.com1.z0.glb.clouddn.com/detail.png" width="50%" height="80%" alt="blog.csdn.net/never_cxb" align="center">

##思路

APP根据新闻id访问jsp -> jsp爬取学院新闻 ->抓取的内容返回给 APP

优点：

- 省流量（服务端的 jsp 过滤了原网页的无用信息）
- 适配手机端阅读，为了方便分享到朋友圈阅读

缺点：

- 每次都需要两次请求，先请求 jsp，再请求学院网站
- 数据没有缓存，后期打算将请求的新闻数据保存到 Mysql 中
