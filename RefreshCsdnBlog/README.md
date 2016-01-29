说明
=======
RefreshCsdnBlog 自动刷新 CSDN 博客访问量

主要思路如下

- 获取博客列表共有几页，比如共有11页 

- 随机选取某页，比如第2页 [http://blog.csdn.net/never_cxb/article/list/2](http://blog.csdn.net/never_cxb/article/list/2)  获取第2页上所有文章

- 对该页上的博客分别进行模拟 get 请求，[http://blog.csdn.net/never_cxb/article/details/50556797](http://blog.csdn.net/never_cxb/article/details/50556797) 

- 为了防止封号，建议刷新博客的时间间隔取随机数

项目地址 [https://github.com/studychen/java/](https://github.com/studychen/android/)

个人博客，欢迎交流 [http://blog.csdn.net/never_cxb](http://blog.csdn.net/never_cxb)
 
